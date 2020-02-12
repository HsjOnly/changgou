package com.itheima.changgou.seckill;

import com.itheima.changgou.util.IdWorker;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import tk.mybatis.spring.annotation.MapperScan;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/24 16:22
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.itheima.changgou.seckill.dao")
@EnableScheduling
@EnableAsync
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }

    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyyMMddHH");
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

    // 秒杀订单队列名
    @Value("${mq.seckillorder.queuename}")
    private String mqSeckillOrderQueueName;

    @Bean
    public Queue seckillOrderQueue(){
        return QueueBuilder.durable(mqSeckillOrderQueueName).build();
    }

    @Bean
    public SimpleDateFormat payResultSimpleDateFormat(){
        return new SimpleDateFormat("yyyyMMddHHmmss");
    }

    // 发送延时队列的交换机名
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

    // 检查订单超时的延时队列路由Key
    @Value("${mq.check.delay.routingkeyname}")
    private String mqCheckDelayRoutingKeyName;

    // 检查订单超时的延时队列名
    @Value("${mq.check.delay.queuename}")
    private String mqCheckDelayQueueName;

    // 转发延时消息的交换机名
    @Value("${mq.delayexchangename}")
    private String mqDelayExchangeName;

    // 检查订单超时的队列路由Key
    @Value("${mq.check.routingkeyname}")
    private String mqCheckRoutingKeyName;

    // 检查订单超时的队列名
    @Value("${mq.check.queuename}")
    private String mqCheckQueueName;

    @Bean
    public Queue checkDelayQueue(){
        return QueueBuilder.durable(mqCheckDelayQueueName)
                .withArgument("x-message-ttl", 30 * 1000) // 消息过期时间 30s
                .withArgument("x-dead-letter-exchange", mqDelayExchangeName) // 消息过期后转发的交换机
                .withArgument("x-dead-letter-routing-key", mqCheckRoutingKeyName) //消息过期后转发的路由Key
                .build();
    }

    @Bean
    public Binding bindingDirectExchangeAndCheckDelayQueue(DirectExchange directExchange, Queue checkDelayQueue){
        return BindingBuilder.bind(checkDelayQueue).to(directExchange).with(mqCheckDelayRoutingKeyName);
    }

    // 转发超时消息的交换机
    @Bean
    public DirectExchange delayExchange(){
        return (DirectExchange) ExchangeBuilder.directExchange(mqDelayExchangeName).build();
    }

    // 接收超时消息的队列
    @Bean
    public Queue checkQueue(){
        return QueueBuilder.durable(mqCheckQueueName).build();
    }

    // 绑定超时交换机和超时队列
    @Bean
    public Binding bindingDelayExchangeAndCheckQueue(DirectExchange delayExchange, Queue checkQueue) {
        return BindingBuilder.bind(checkQueue).to(delayExchange).with(mqCheckRoutingKeyName);
    }
}
