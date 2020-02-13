package com.itheima.changgou.order.controller;

import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.order.pojo.Order;
import com.itheima.changgou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
        String username = getUserNameFromRequest(request);
        orderService.insertOrder(username, order);
        return new Result(true, StatusCode.OK, "创建订单成功，请及时支付");
    }

    private String getUserNameFromRequest(HttpServletRequest request) {
        return (String) request.getAttribute("username");
    }

    @GetMapping("/listOrderByUsername")
    public Result<List<Order>> listOrderByUsername(HttpServletRequest request) {
        String username = getUserNameFromRequest(request);
        List<Order> orderList = orderService.ListOrderByUsername(username);
        return new Result<>(true,StatusCode.OK, "获取订单成功", orderList);
    }

}
