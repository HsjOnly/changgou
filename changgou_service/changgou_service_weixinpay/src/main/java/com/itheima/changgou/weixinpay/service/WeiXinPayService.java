package com.itheima.changgou.weixinpay.service;

import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/21 20:29
 */
public interface WeiXinPayService {
    String createPayOrder(Map<String, String> parameters) throws Exception;

    void detectPayStaus(Long orderId) throws Exception;

    String recieveResultNotify(String resultXml) throws Exception;

}
