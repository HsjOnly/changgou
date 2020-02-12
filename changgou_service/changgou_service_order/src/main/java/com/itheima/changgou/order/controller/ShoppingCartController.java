package com.itheima.changgou.order.controller;

import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.order.pojo.OrderItem;
import com.itheima.changgou.order.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/17 12:14
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 此方法用于向购物车中添加商品
     *
     * @Return com.itheima.changgou.entity.Result
     * @Author narcissu
     * @Date 2020/1/17 12:23
     **/
    @PostMapping("/addGoods")
    public Result addGoods(Long skuId, Integer num, HttpServletRequest request) {
        // 获取用户信息
        String username = (String) request.getAttribute("username");
        // 调用Service处理添加商品
        shoppingCartService.addGoods(skuId, num, username);
        // 返回结果
        return new Result(true, StatusCode.OK, "操作成功");
    }

    @GetMapping("/listGoods")
    public Result listGoods(HttpServletRequest request) throws Exception {
        // 获取登入用户信息
        String username = (String) request.getAttribute("username");
        // 调用service
        List<OrderItem> orderItemList = shoppingCartService.listGoods(username);
        // 返回查询结果
        return new Result<>(true, StatusCode.OK, "查询购物车商品成功", orderItemList);
    }
}
