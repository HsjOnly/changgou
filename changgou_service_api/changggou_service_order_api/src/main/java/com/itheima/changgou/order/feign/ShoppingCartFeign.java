package com.itheima.changgou.order.feign;

import com.itheima.changgou.entity.Result;
import com.itheima.changgou.order.pojo.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/19 22:25
 */
@FeignClient("service-order")
@RequestMapping("/shoppingCart")
public interface ShoppingCartFeign {

    @GetMapping("/listGoods")
    Result<List<OrderItem>> listGoods();
}
