package com.itheima.changgou.order.service;

import com.itheima.changgou.order.pojo.Order;

import java.text.ParseException;
import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/21 11:04
 */
public interface OrderService {
    void insertOrder(String username, Order order);

    void updateOrder(Map<String, String> map) throws Exception;
}
