package com.itheima.changgou.order;

import com.itheima.changgou.util.IdWorker;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tk.mybatis.spring.annotation.MapperScan;

import java.text.SimpleDateFormat;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/17 12:59
 */
@SpringBootApplication
@EnableFeignClients({"com.itheima.changgou.goods.feign", "com.itheima.changgou.feign"})
@EnableDiscoveryClient
@MapperScan("com.itheima.changgou.order.dao")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
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

    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyyMMddHHmmss");
    }

    @Value("${mq.commonorder.queuename}")
    private String mqCommonOrderQueueName;

    @Bean
    public Queue queue(){
        return QueueBuilder.durable(mqCommonOrderQueueName).build();
    }

}
