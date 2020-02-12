package com.itheima.changgou.seckill.pojo;

import java.io.Serializable;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/25 15:04
 */
public class SeckillOrderStatus implements Serializable {

    // 用户名
    private String username;
    // 秒杀时间段
    private String dateMenu;
    // 秒杀商品Id
    private Long seckillGoodsId;
    // 秒杀下单结果（0 正在处理中，1 创建订单成功， 2 创建订单失败）
    private String orderStatus;
    // 记录创建订单失败原因
    private String orderFailReason;
    // 生成的订单信息
    private SeckillOrder seckillOrder;

    public SeckillOrderStatus() {
    }

    public SeckillOrderStatus(String username, String dateMenu, Long seckillGoodsId, String orderStatus, SeckillOrder seckillOrder, String orderFailReason) {
        this.username = username;
        this.dateMenu = dateMenu;
        this.seckillGoodsId = seckillGoodsId;
        this.orderStatus = orderStatus;
        this.seckillOrder = seckillOrder;
        this.orderFailReason = orderFailReason;
    }

    @Override
    public String toString() {
        return "SeckillOrderStatus{" +
                "username='" + username + '\'' +
                ", dateMenu='" + dateMenu + '\'' +
                ", seckillGoodsId=" + seckillGoodsId +
                ", orderStatus='" + orderStatus + '\'' +
                ", seckillOrder=" + seckillOrder +
                ", orderFailReason='" + orderFailReason + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateMenu() {
        return dateMenu;
    }

    public void setDateMenu(String dateMenu) {
        this.dateMenu = dateMenu;
    }

    public Long getSeckillGoodsId() {
        return seckillGoodsId;
    }

    public void setSeckillGoodsId(Long seckillGoodsId) {
        this.seckillGoodsId = seckillGoodsId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderFailReason() {
        return orderFailReason;
    }

    public void setOrderFailReason(String orderFailReason) {
        this.orderFailReason = orderFailReason;
    }

    public SeckillOrder getSeckillOrder() {
        return seckillOrder;
    }

    public void setSeckillOrder(SeckillOrder seckillOrder) {
        this.seckillOrder = seckillOrder;
    }
}
