package com.itheima.changgou.seckill.controller;

import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.seckill.pojo.SeckillOrderStatus;
import com.itheima.changgou.seckill.service.SeckillOrderService;
import org.redisson.liveobject.resolver.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/24 19:16
 */
@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;


    @PostMapping("/addIntoQueue")
    public Result addIntoQueue(String dateMenu, Long seckillGoodsId) throws Exception {
        // 获取当前登入的用户名 todo 若已登入，拦截器将会存入request
        String username = "yezushe";
//        String username = UUID.randomUUID().toString();
        // 调用Service存入Redis排队
        seckillOrderService.addIntoQueue(username, dateMenu, seckillGoodsId);
        // 响应信息
        return new Result(true, StatusCode.OK, "已经加入队列，请稍等");
    }

    @GetMapping("/queryOrderStatus")
    public Result<SeckillOrderStatus> queryOrderStatus(String dateMenu, Long seckillGoodsId) {
        String username = "yezushe";
        SeckillOrderStatus seckillOrderStatus = seckillOrderService.queryOrderStatus(username, dateMenu, seckillGoodsId);
        return new Result(true, StatusCode.OK, "查询订单状态成功", seckillOrderStatus);
    }
}
