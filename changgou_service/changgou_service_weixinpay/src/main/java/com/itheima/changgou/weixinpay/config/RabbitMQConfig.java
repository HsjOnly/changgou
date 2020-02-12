package com.itheima.changgou.weixinpay.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/23 16:25
 */
@Configuration
public class RabbitMQConfig {

    @Value("${mq.exchangename}")
    private String mqExchangeName;


    /**
     * 路由交换机
     *
     * @Return org.springframework.amqp.core.DirectExchange
     * @Author narcissu
     * @Date 2020/1/29 12:11
     **/
    @Bean
    public DirectExchange directExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(mqExchangeName).build();
    }

    // 普通订单队列RoutingKey
    @Value("${mq.commonorder.routingkeyname}")
    private String mqCommonOrderRoutingKeyName;

    // 普通订单队列名
    @Value("${mq.commonorder.queuename}")
    private String commonOrderQueueName;

    /**
     * 普通订单队列
     *
     * @Return org.springframework.amqp.core.Queue
     * @Author narcissu
     * @Date 2020/1/29 12:11
     **/
    @Bean
    public Queue commonOrderQueue() {
        return QueueBuilder.durable(commonOrderQueueName).build();
    }

    /**
     * 普通订单与交换机绑定
     *
     * @Return org.springframework.amqp.core.Binding
     * @Author narcissu
     * @Date 2020/1/29 12:12
     **/
    @Bean
    public Binding commonOrderBinding(@Qualifier("commonOrderQueue") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(mqCommonOrderRoutingKeyName);
    }

    // 秒杀订单队列名
    @Value("${mq.seckillorder.queuename}")
    private String mqSeckillOrderQueueName;

    // 秒杀订单RoutingKey
    @Value("${mq.seckillorder.routingkeyname}")
    private String mqSeckillOrderRoutingKeyName;

    @Bean
    public Queue seckillOrderQueue(){
        return QueueBuilder.durable(mqSeckillOrderQueueName).build();
    }

    @Bean
    public Binding seckillOrderBinding(@Qualifier("seckillOrderQueue")Queue queue, DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with(mqSeckillOrderRoutingKeyName);
    }
}
