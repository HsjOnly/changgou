package com.itheima.changgou.seckill.service;

import com.itheima.changgou.seckill.pojo.SeckillGoods;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/24 17:42
 */
public interface SeckillGoodsService {
    List<SeckillGoods> getSeckillGoodsByDateMenu(String dateMenu);

    SeckillGoods getSeckillGoodsById(String dateMenu, Long seckillGoodsId);
}
