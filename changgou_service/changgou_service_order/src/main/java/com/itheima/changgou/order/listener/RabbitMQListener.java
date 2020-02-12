package com.itheima.changgou.order.listener;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/23 17:17
 */
@RabbitListener(queues = "${mq.commonorder.queuename}")
@Component
public class RabbitMQListener {

    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void  msgHandle(String  msg) throws Exception {
        Map<String, String> map = JSON.parseObject(msg, Map.class);
        orderService.updateOrder(map);
    }
}
