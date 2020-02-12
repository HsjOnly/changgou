package com.itheima.changgou.seckill;

import com.itheima.changgou.seckill.constant.SeckillConstants;
import com.itheima.changgou.seckill.pojo.SeckillGoods;
import com.itheima.changgou.seckill.pojo.SeckillOrder;
import com.itheima.changgou.seckill.pojo.SeckillOrderStatus;
import com.itheima.changgou.util.IdWorker;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/25 14:28
 */
@Component
public class MultiThreadCreateOrder {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.exchangename}")
    private String mqExchangeName;

    // 检查订单超时的延时队列路由Key
    @Value("${mq.check.delay.routingkeyname}")
    private String mqCheckDelayRoutingKeyName;

    /**
     * 为了提升用户体验使用多线程生成订单
     *
     * @Return void
     * @Author narcissu
     * @Date 2020/1/25 14:18
     **/
    @Async
    public void createOrder() throws Exception {

        // 取出订单队列中的状态
        SeckillOrderStatus seckillOrderStatus = (SeckillOrderStatus) redisTemplate.boundListOps(SeckillConstants.REDIS_SECKILL_ORDER_QUEUE).rightPop();
        String username = seckillOrderStatus.getUsername();
        String dateMenu = seckillOrderStatus.getDateMenu();
        Long seckillGoodsId = seckillOrderStatus.getSeckillGoodsId();

        // 根据请求的参数取出下单的商品信息
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_GOODS_KEY_PREFIX + dateMenu).get(seckillGoodsId);

        // 判断商品是否存在
        if (seckillGoods == null) {
            updateCreateSeckillOrderStatus(seckillOrderStatus, "2", "商品不存在", username, dateMenu, null, seckillGoodsId);
            return;
        }

//        // 判断商品是否处于秒杀时间
//        Date now = new Date();
//        if (!(seckillGoods.getStartTime().before(now) && seckillGoods.getEndTime().after(now))) {
//            updateCreateSeckillOrderStatus(seckillOrderStatus, "2", "商品不处于秒杀时间", username, dateMenu, null, seckillGoodsId);
//            return;
//        }


        // 判断用户是否已有该商品的订单(每个订单的Key为username_dateMenu_seckillGoodsId)
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_ORDER_KEY).get(username + "_" + dateMenu + "_" + seckillGoodsId);
        if (seckillOrder != null) {
            updateCreateSeckillOrderStatus(seckillOrderStatus, "2", "已购买，不可重复购买", username, dateMenu, null, seckillGoodsId);
            return;
        }

        RLock stockCountLock = redissonClient.getLock("stock_count_lock");

        try {
            // 上锁
            stockCountLock.lock(10000, TimeUnit.SECONDS);
            // 判断并减少库存
            reduceStockCount(seckillOrderStatus, username, dateMenu, seckillGoodsId, seckillGoods);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stockCountLock.unlock();
        }

        // 构建订单
        seckillOrder = new SeckillOrder();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setSeckillId(seckillGoods.getId());
        seckillOrder.setMoney(seckillGoods.getPrice());
        seckillOrder.setUserId(username);
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setStatus("0");

        // 将订单存入redis
        redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_ORDER_KEY).put(seckillOrder.getId(), seckillOrder);

        // 记录订单生成成功
        updateCreateSeckillOrderStatus(seckillOrderStatus, "1", null, username, dateMenu, seckillOrder, seckillGoodsId);

        // 发送检查订单是否支付超时的消息
        rabbitTemplate.convertAndSend(mqExchangeName, mqCheckDelayRoutingKeyName, seckillOrder);
        System.out.println("超时检测消息发送了，发送时间:" + new Date()+"\r\n----------------------------------");
    }

    private void reduceStockCount(SeckillOrderStatus seckillOrderStatus, String username, String dateMenu, Long seckillGoodsId, SeckillGoods seckillGoods) {
        // 判断商品是否有库存
        Integer stockCount = seckillGoods.getStockCount();
        if (stockCount <= 0) {
            // 没有库存，更新订单状态
            updateCreateSeckillOrderStatus(seckillOrderStatus, "2", "商品已售罄", username, dateMenu, null, seckillGoodsId);
            return;
        }
        // 有库存，减少商品库存，后续生成订单
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_GOODS_KEY_PREFIX + dateMenu).put(seckillGoods.getId(), seckillGoods);
    }

    private void updateCreateSeckillOrderStatus(SeckillOrderStatus seckillOrderStatus, String orderStatus, String orderFailReason, String username, String dateMenu, SeckillOrder seckillOrder, Long seckillGoodsId) {
        // 如果下单不成功，清除抢单状态
        if (!orderStatus.equalsIgnoreCase("1")) {
            Long isRepeated = redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_ORDER_REPEAT_KEY).delete(username + "_" + dateMenu + "_" + seckillGoodsId);
        }

        // 更新订单生成状态
        seckillOrderStatus.setOrderStatus(orderStatus);
        seckillOrderStatus.setOrderFailReason(orderFailReason);
        seckillOrderStatus.setSeckillOrder(seckillOrder);
        // 存入Redis
        redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_ORDER_STATUS).put(username + "_" + dateMenu + "_" + seckillGoodsId, seckillOrderStatus);
    }
}
