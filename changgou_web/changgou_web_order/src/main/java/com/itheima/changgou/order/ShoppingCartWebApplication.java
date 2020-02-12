package com.itheima.changgou.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/19 22:21
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(basePackages = "com.itheima.changgou.order.feign")
@EnableDiscoveryClient
public class ShoppingCartWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartWebApplication.class, args);
    }
}
