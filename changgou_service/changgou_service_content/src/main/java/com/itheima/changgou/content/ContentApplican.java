package com.itheima.changgou.content;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/4 17:41
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.itheima.changgou.content.dao")
public class ContentApplican {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplican.class, args);
    }
}
