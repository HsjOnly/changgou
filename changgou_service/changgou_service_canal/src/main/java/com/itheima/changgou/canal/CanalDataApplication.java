package com.itheima.changgou.canal;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/4 11:33
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableCanalClient
@EnableFeignClients(basePackages = "com.itheima.changgou.canal.feign")
public class CanalDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanalDataApplication.class, args);
    }
}
