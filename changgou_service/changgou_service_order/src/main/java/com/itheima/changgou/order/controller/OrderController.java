package com.itheima.changgou.order.controller;

import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.order.pojo.Order;
import com.itheima.changgou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/21 10:57
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/insertOrder")
    public Result insertOrder(@RequestBody Order order, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        orderService.insertOrder(username, order);
        return new Result(true, StatusCode.OK, "创建订单成功，请及时支付");
    }

}
