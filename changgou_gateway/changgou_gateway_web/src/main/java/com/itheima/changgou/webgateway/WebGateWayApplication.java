package com.itheima.changgou.webgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/12 10:57
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WebGateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebGateWayApplication.class, args);
    }

    /*
     * @Description KeyResolver用于从请求中获取限流的Key
     * @Param []
     * @Return org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
     * @Author narcissu
     * @Date 2020/1/12 11:48
     **/
    @Bean
    public KeyResolver ipKeyResolver() {
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                System.out.println(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
                return Mono.just("aa");
            }
        };
    }
}
