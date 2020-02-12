package com.itheima.changgou.seckill.listen;

import com.alibaba.fastjson.JSON;
import com.itheima.changgou.seckill.pojo.SeckillOrder;
import com.itheima.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/29 14:22
 */
@Component
public class RabbtMqListener {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @RabbitListener(queues = "${mq.seckillorder.queuename}")
    public void seckillOrderHandle(String msg) throws Exception {
        // 将支付结果的JSON转为Map
        Map<String,String> map = (Map<String, String>) JSON.parse(msg);

        // 调用Service处理订单
        seckillOrderService.completeSeckillOrder(map);
    }

    /**
     * 检查订单是否超时未支付
     * @Return void
     * @Author narcissu
     * @Date 2020/1/31 19:48
     **/
    @RabbitListener(queues = "${mq.check.queuename}")
    public void checkIfSeckillOrderPayTimeOut(SeckillOrder seckillOrder){
        seckillOrderService.checkIfSeckillOrderPayTimeOut(seckillOrder);
    }

}
