package com.itheima.changgou.seckill.service.impl;

import com.itheima.changgou.seckill.MultiThreadCreateOrder;
import com.itheima.changgou.seckill.constant.SeckillConstants;
import com.itheima.changgou.seckill.dao.SeckillOrderDao;
import com.itheima.changgou.seckill.pojo.SeckillOrder;
import com.itheima.changgou.seckill.pojo.SeckillOrderStatus;
import com.itheima.changgou.seckill.service.SeckillOrderService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/24 19:25
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private MultiThreadCreateOrder multiThreadCreateOrder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void addIntoQueue(String username, String dateMenu, Long seckillGoodsId) throws Exception {
        // 判断用户是否重复抢单
        Long isRepeated = redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_ORDER_REPEAT_KEY).increment(username + "_" + dateMenu + "_" + seckillGoodsId, 1);
        if (isRepeated > 1) {
            throw new Exception("重复抢单了");
        }

        SeckillOrderStatus seckillOrderStatus = new SeckillOrderStatus(username, dateMenu, seckillGoodsId, "0", null, null);

        // 在Redis使用队列记录用户下单信息
        redisTemplate.boundListOps(SeckillConstants.REDIS_SECKILL_ORDER_QUEUE).leftPush(seckillOrderStatus);

        // 使用多线程生成订单
        multiThreadCreateOrder.createOrder();
    }

    @Override
    public SeckillOrderStatus queryOrderStatus(String username, String dateMenu, Long seckillGoodsId) {
        return (SeckillOrderStatus) redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_ORDER_STATUS).get(username + "_" + dateMenu + "_" + seckillGoodsId);
    }

    @Autowired
    private SimpleDateFormat payResultSimpleDateFormat;

    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Override
    public void completeSeckillOrder(Map<String, String> map) throws Exception {
        // 判断返回状态
        if (!map.get("return_code").equalsIgnoreCase("success")) {
            // 返回状态失败
            return;
        }
        // 判断业务结果
        if (!map.get("return_code").equalsIgnoreCase("success")) {
            // 业务结果失败，取消订单，回滚库存
            return;
        }
        // 判断交易状态(若为微信主动通知则为null)
        if (map.get("trade_state") != null && !map.get("trade_state").equalsIgnoreCase("success")) {
            // 业务结果失败，取消订单，回滚库存
            return;
        }
        // 交易成功
        // 获取支付订单号
        Long outTradeNo = Long.parseLong(map.get("out_trade_no"));
        String timeEnd = map.get("time_end");
        String transactionId = map.get("transaction_id");
        // 修改订单状态
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_ORDER_KEY).get(outTradeNo);
        seckillOrder.setPayTime(payResultSimpleDateFormat.parse(timeEnd));
        seckillOrder.setStatus("1");
        seckillOrder.setTransactionId(transactionId);
        // 将订单存入DB
        seckillOrderDao.insert(seckillOrder);
        // 删除Redis订单信息
        redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_ORDER_KEY).delete(outTradeNo);
        // 清除排队信息
    }

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void checkIfSeckillOrderPayTimeOut(SeckillOrder seckillOrder) {
        // 查看数据库是否有该订单
        SeckillOrder resultSeckillOrder = seckillOrderDao.selectByPrimaryKey(seckillOrder);

        if (resultSeckillOrder == null) {
            // 无，交易失败
            // 关闭微信订单
            // 库存回滚
            System.out.println("库存回滚了,记得要上锁哟~\r\n回滚时间:"+new Date());
            // 删除订单
            // 清除排队信息
        }

    }

}
