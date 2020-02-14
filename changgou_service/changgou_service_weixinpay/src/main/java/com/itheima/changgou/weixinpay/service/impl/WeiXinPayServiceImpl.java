package com.itheima.changgou.weixinpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.itheima.changgou.util.HttpClient;
import com.itheima.changgou.weixinpay.service.WeiXinPayService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/21 20:31
 */
@Service
public class WeiXinPayServiceImpl implements WeiXinPayService {

    @Value("${weixin.appid}")
    private String appId;

    @Value("${weixin.mchid}")
    private String mchId;

    @Value("${weixin.partnerkey}")
    private String partnerKey;

    @Value("${weixin.notifyurl}")
    private String notifyUrl;

    @Value("${weixin.unifiedorderurl}")
    private String unifiedOrderUrl;

    @Value("${weixin.orderqueryurl}")
    private String orderQueryUrl;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public String createPayOrder(Map<String, String> parameters) throws Exception {
        // 查询商品订单 todo，需要区分普通订单和秒杀订单

        // 生成请求支付订单参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", appId);
        paramMap.put("mch_id", mchId);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("body", "畅购商品");
        // 添加附加参数
        paramMap.put("attach", JSON.toJSONString(paramMap));
        paramMap.put("out_trade_no", parameters.get("orderId"));
        paramMap.put("total_fee", 1 + ""); //支付金额 todo 需要从订单中取出
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", notifyUrl);// 通知地址
        paramMap.put("trade_type", "NATIVE");

        // 参数Map转为XML
        String paramXml = WXPayUtil.generateSignedXml(paramMap, partnerKey);

        // 发送订单请求
        HttpClient client = new HttpClient(unifiedOrderUrl);
        client.setHttps(true);//是否是https协议
        client.setXmlParam(paramXml);//发送的xml数据
        client.post();//执行post请求
        String resultXml = client.getContent(); //获取结果

        // 参数XML转为MAP
        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

        // 获取二维码连接
        String codeUrl = resultMap.get("code_url");

        // 返回订单结果
        return codeUrl;

    }

    // 普通订单队列RoutingKey
    @Value("${mq.commonorder.routingkeyname}")
    private String mqCommonOrderRoutingKeyName;

    // 秒杀订单RoutingKey
    @Value("${mq.seckillorder.routingkeyname}")
    private String mqSeckillOrderRoutingKeyName;

    @Value("${mq.exchangename}")
    private String mqExchangeName;


    @Override
    public void detectPayStaus(Long orderId) throws Exception {
        // 封装查询参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", appId);
        paramMap.put("mch_id", mchId);
        paramMap.put("out_trade_no", orderId + "");
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        String paramXml = WXPayUtil.generateSignedXml(paramMap, partnerKey);

        // 查询支付情况
        HttpClient client = new HttpClient(orderQueryUrl);
        client.setHttps(true);//是否是https协议
        client.setXmlParam(paramXml);//发送的xml数据
        client.post();//执行post请求
        String resultXml = client.getContent(); //获取结果

        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);


        // 判断通信是否成功
        if (!resultMap.get("return_code").equalsIgnoreCase("success")) {
            throw new RuntimeException(resultMap.get("return_msg"));
        }
        // 判断业务结果是否成功
        if (!resultMap.get("result_code").equalsIgnoreCase("success")) {
            throw new RuntimeException(resultMap.get("err_code_des"));
        }
        // 判断交易状态
        if (!resultMap.get("trade_state").equalsIgnoreCase("success")) {
            throw new RuntimeException(resultMap.get("trade_state"));
        }

        // 获取附加信息，转为Map
        String attachJson = resultMap.get("attach");
        Map<String, String> attachMap = (Map<String, String>) JSON.parse(attachJson);
        // 根据订单类型发送消息到MQ
        sendMessageAccordingToOrderCategory(resultMap, attachMap);

    }

    private void sendMessageAccordingToOrderCategory(Map<String, String> resultMap, Map<String, String> attachMap) {
        // 获取订单类型
        String orderCategory = attachMap.get("orderCategory");

        switch (orderCategory) {
            case "0":
                // 如果是普通订单，发送修改订单状态请求到MQ普通订单队列
                rabbitTemplate.convertAndSend(mqExchangeName, mqCommonOrderRoutingKeyName, JSON.toJSONString(resultMap));
                break;
            case "1":
                // 如果是秒杀订单，发送修改订单状态请求到MQ秒杀订单队列
                rabbitTemplate.convertAndSend(mqExchangeName, mqSeckillOrderRoutingKeyName, JSON.toJSONString(resultMap));
                break;
            default:
                throw new RuntimeException("订单类型有误");
        }
    }

    /**
     * 用于接收微信的交易通知，成功后才会收到，所以此处不需要判断交易状态
     *
     * @Return java.lang.String
     * @Author narcissu
     * @Date 2020/1/29 13:22
     **/
    @Override
    public String recieveResultNotify(String resultXml) throws Exception {
        // 将Xml转为Map
        Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
        // 校验签名是否有效，略
        // 判断通信是否成功
        if (!resultMap.get("return_code").equalsIgnoreCase("success")) {
            throw new RuntimeException(resultMap.get("return_msg"));
        }
        // 判断业务结果是否成功
        if (!resultMap.get("result_code").equalsIgnoreCase("success")) {
            throw new RuntimeException(resultMap.get("err_code_des"));
        }

        // 获取附加信息，转为Map
        String attachJson = resultMap.get("attach");
        Map<String, String> attachMap = (Map<String, String>) JSON.parse(attachJson);

        // 根据订单类型发送消息到不同的消息队列
        sendMessageAccordingToOrderCategory(resultMap, attachMap);

        // 封装响应Map
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("return_code", "SUCCESS");
        responseMap.put("return_msg", "OK");
        // 转为Xml
        String responseXml = WXPayUtil.mapToXml(responseMap);
        return responseXml;
    }

}
