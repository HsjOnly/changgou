package com.itheima.changgou.order.service;

import com.itheima.changgou.order.pojo.OrderItem;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/17 12:23
 */
public interface ShoppingCartService {
    void addGoods(Long skuId, Integer num, String usernmae);

    List<OrderItem> listGoods(String username);
}
