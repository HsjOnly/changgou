package com.itheima.changgou.weixinpay.controller;

import com.alibaba.fastjson.JSON;
import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.weixinpay.service.WeiXinPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/21 20:18
 */
@RestController
@RequestMapping("/weiXinPay")
public class WeiXinPayController {

    @Autowired
    private WeiXinPayService weiXinPayService;

    /**
     * 此方法用于生成支付订单
     *
     * @Return com.itheima.changgou.entity.Result
     * @Author narcissu
     * @Date 2020/1/22 17:46
     **/
    @RequestMapping("/createOrder")
    public Result createCommOrder(@RequestParam Map<String, String> parameters) throws Exception {
        String codeUrl = weiXinPayService.createPayOrder(parameters);

        String orderCategory = parameters.get("orderCategory");

        if (orderCategory.equalsIgnoreCase("0")) {
            return new Result(true, StatusCode.OK, "生成普通订单支付订单成功", codeUrl);
        }

        if (orderCategory.equalsIgnoreCase("1")) {
            return new Result(true, StatusCode.OK, "生成秒杀订单支付订单成功", codeUrl);
        }

        return new Result(false, StatusCode.ERROR, "输入的参数信息有误");
    }

    @RequestMapping("/recieveResultNotify")
    public String recieveResultNotify(@RequestBody String resultXml) throws Exception {
        // 调用Service处理通知结果
        return weiXinPayService.recieveResultNotify(resultXml);
    }


    @RequestMapping("/detectPayStatus")
    public Result<String> detectPayStatus(@RequestParam("orderId") Long orderId) throws Exception {
        weiXinPayService.detectPayStaus(orderId);
        return new Result<>(true, StatusCode.OK, "订单已经支付成功了");
    }
}
