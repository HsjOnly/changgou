package com.itheima.changgou.seckill.constant;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/24 16:48
 */
public class SeckillConstants {

    // 存储用户订单信息的HashKey
    public static final String REDIS_SECKILL_ORDER_KEY = "seckill_order";

    // 每个时段秒杀商品存储的HashKey前缀
    public static final String REDIS_SECKILL_GOODS_KEY_PREFIX = "seckill_goods_";

    // 存储用户下单状态的HashKey
    public static final String REDIS_SECKILL_ORDER_STATUS = "seckill_order_status";

    // 存储订单状态的队列
    public static final String REDIS_SECKILL_ORDER_QUEUE = "seckill_order_queue";

    // 存储用户是否已秒杀该商品的信息
    public static final String REDIS_SECKILL_ORDER_REPEAT_KEY = "seclkill_order_repeat";
}
