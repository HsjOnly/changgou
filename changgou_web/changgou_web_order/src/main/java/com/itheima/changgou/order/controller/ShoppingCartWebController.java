package com.itheima.changgou.order.controller;

import com.itheima.changgou.entity.Result;
import com.itheima.changgou.order.feign.ShoppingCartFeign;
import com.itheima.changgou.order.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/19 22:15
 */
@RestController
@RequestMapping("/shoppingCartWeb")
public class ShoppingCartWebController {

    @Autowired
    private ShoppingCartFeign shoppingCartFeign;

    @RequestMapping("/showShoppingCart")
    public ModelAndView showShoppingCart(){
        Result<List<OrderItem>> result = shoppingCartFeign.listGoods();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cart");
        modelAndView.addObject("result", result);
        return modelAndView;
    }
}
