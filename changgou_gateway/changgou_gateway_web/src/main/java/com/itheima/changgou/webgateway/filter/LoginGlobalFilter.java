package com.itheima.changgou.webgateway.filter;

import com.itheima.changgou.webgateway.util.JwtUtils;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/13 16:47
 */
@Component
public class LoginGlobalFilter implements GlobalFilter, Ordered {

    private static final String JWT_KEY = "Authorization";

    private static final String LOGIN_PAGE = "http://localhost:9998/user/login";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 判断用户请求的资源，如果为登入页放行，非登入页则校验登入信息
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (path.startsWith("/user/user/login")) {
            return chain.filter(exchange);
        }

        // 为了保证兼容各种情况先从请求头中获取jwt
        String jwt = request.getHeaders().getFirst(JWT_KEY);

        // 如果没有从参数中获取
        if (StringUtils.isEmpty(jwt)){
            jwt = request.getQueryParams().getFirst(JWT_KEY);
        }

        // 如果没有从cookie中获取
        if (StringUtils.isEmpty(jwt)) {

            // 获取令牌Cookie
            HttpCookie jwtCookie = request.getCookies().getFirst(JWT_KEY);

            if (jwtCookie == null) {
                // 没有该cookie，重定向到登入页
                return authenticationFail(exchange,request);
            }

            // 取出Cookie中的jwt
            jwt = jwtCookie.getValue();
        }

        // 将jwt信息存入请求头中，放行
        request.mutate().header(JWT_KEY, "bearer " + jwt);
        return chain.filter(exchange);

//        try {
//        // 解析Jwt，如果成功说明已经认证，放行
//        JwtUtils.parseJWT(jwt);
//        return chain.filter(exchange);
//    } catch (Exception e) {
//        e.printStackTrace();
//        // 认证失败重定向
//        return authenticationFail(exchange);
//    }
}

    private Mono<Void> authenticationFail(ServerWebExchange exchange, ServerHttpRequest request) {
        // 获取想登入的页面，用于完成登入后重定向
        String path = request.getURI().toString();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location", LOGIN_PAGE+"?from="+path);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
