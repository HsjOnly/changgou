package com.itheima.changgou.order.service.impl;

import com.itheima.changgou.util.IdWorker;
import com.itheima.changgou.feign.UserFeign;
import com.itheima.changgou.goods.feign.SkuFeign;
import com.itheima.changgou.order.constant.OrderConstants;
import com.itheima.changgou.order.dao.OrderDao;
import com.itheima.changgou.order.dao.OrderItemDao;
import com.itheima.changgou.order.pojo.Order;
import com.itheima.changgou.order.pojo.OrderItem;
import com.itheima.changgou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/21 11:04
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private UserFeign userFeign;

    @Override
    public void insertOrder(String username, Order order) {
        // 完善订单基本信息
        order.setId(idWorker.nextId()+"");
        order.setCreateTime(new Date());
        order.setUsername(username);

        int totalNum = 0;
        int totalMoney = 0;
        // 从Redis中读取该用户的购物车
        List<OrderItem> orderItemList = redisTemplate.boundHashOps(OrderConstants.REDIS_SHOPPING_CART_PREFIX + username).values();
        // 逐个处理orderItem
        for (OrderItem orderItem : orderItemList) {
            // 补充orderItem信息
            orderItem.setId(idWorker.nextId()+"");
            orderItem.setOrderId(order.getId());

            // 扣减对应的sku的库存
            skuFeign.updateSkuByIdAndSaleNum(orderItem.getSkuId(), orderItem.getNum());

            // 存入数据
            orderItemDao.insertSelective(orderItem);

            // 统计order信息
            totalNum+= orderItem.getNum();
            totalMoney+= orderItem.getMoney();
        }

        // 完善订单金额信息
        order.setTotalNum(totalNum);
        order.setTotalMoney(totalMoney);

        // 完善订单状态
        order.setSourceType("1");
        order.setOrderStatus("0");
        order.setPayStatus("0");
        order.setConsignStatus("0");
        order.setIsDelete("0");
        orderDao.insert(order);

        // 增加用户积分
        userFeign.updateUserByUsernameAndPoints(username, 10);

        // 清空用户的购物车
        redisTemplate.delete(OrderConstants.REDIS_SHOPPING_CART_PREFIX + username);
    }

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Override
    public void updateOrder(Map<String, String> map) throws Exception {
        Order order = new Order();
        order.setId(map.get("out_trade_no"));
        order.setUpdateTime(new Date());
        order.setPayTime(simpleDateFormat.parse(map.get("time_end")));
        order.setEndTime(simpleDateFormat.parse(map.get("time_end")));
        order.setTransactionId(map.get("transaction_id"));
        order.setPayStatus("1");
        orderDao.updateByPrimaryKeySelective(order);
    }
}
