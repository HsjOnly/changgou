package com.itheima.changgou.seckill.service;

import com.itheima.changgou.seckill.pojo.SeckillOrder;
import com.itheima.changgou.seckill.pojo.SeckillOrderStatus;

import java.text.ParseException;
import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/24 19:24
 */
public interface SeckillOrderService {
    void addIntoQueue(String username, String dateMenu, Long seckillGoodsId) throws Exception;

    SeckillOrderStatus queryOrderStatus(String username, String dateMenu, Long seckillGoodsId);

    void completeSeckillOrder(Map<String, String> map) throws Exception;

    void checkIfSeckillOrderPayTimeOut(SeckillOrder seckillOrder);
}
