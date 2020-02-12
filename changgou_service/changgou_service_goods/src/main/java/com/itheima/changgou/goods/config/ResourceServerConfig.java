package com.itheima.changgou.goods.config;

import com.itheima.changgou.goods.GoodsServiceApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @Description 用于配置Jwt令牌的校验
 * @Author narcissu
 * @Date 2020/1/16 19:46
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /**
     * 用于读取公钥
     * @Return org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     * @Author narcissu
     * @Date 2020/1/16 19:44
     **/
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        // 获取公钥 字节流 -> 字符流 -> 字符缓冲流
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(GoodsServiceApplication.class.getClassLoader().getResourceAsStream("public.key")));
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setVerifierKey(bufferedReader.lines().collect(Collectors.joining("\n")));
        return jwtAccessTokenConverter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers()
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
