package com.itheima.changgou.order.intercept;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/20 13:16
 */
@Component
public class FeignIntercept implements RequestInterceptor {

    private static final String JWT_KEY = "Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String jwtHeader = attributes.getRequest().getHeader(JWT_KEY);
        if (jwtHeader != null) {
            requestTemplate.header(JWT_KEY, jwtHeader);
        }
    }
}
