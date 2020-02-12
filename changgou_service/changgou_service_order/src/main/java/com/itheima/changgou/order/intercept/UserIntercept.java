package com.itheima.changgou.order.intercept;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/20 10:56
 */
@Component
public class UserIntercept implements HandlerInterceptor {

    private static final String JWT_KEY = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 取出认证信息
        String jwt = request.getHeader(JWT_KEY).replace("bearer ", "");
        String[] splitedJwt = jwt.split("\\.");
        // 获取用户信息
        String payload = new String(Base64.getDecoder().decode(splitedJwt[1]));
        Map<String, String> payloadMap = JSON.parseObject(payload, Map.class);
        String username = payloadMap.get("username");
        // 存入request
        request.setAttribute("username", username);
        return true;
    }
}
