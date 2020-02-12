package com.itheima.changgou.weixinpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/21 20:24
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class WeiXinPayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeiXinPayApplication.class, args);
    }

}
