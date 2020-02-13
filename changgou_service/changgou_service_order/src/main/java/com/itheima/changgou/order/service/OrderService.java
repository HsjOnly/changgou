package com.itheima.changgou.order.service;

import com.itheima.changgou.order.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/21 11:04
 */
public interface OrderService {
    void insertOrder(String username, Order order);

    void updateOrder(Map<String, String> map) throws Exception;

    List<Order> ListOrderByUsername(String username);
}
