package com.itheima.changgou.seckill.service.impl;

import com.itheima.changgou.seckill.constant.SeckillConstants;
import com.itheima.changgou.seckill.pojo.SeckillGoods;
import com.itheima.changgou.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/24 17:43
 */
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<SeckillGoods> getSeckillGoodsByDateMenu(String dateMenu) {
        return redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_GOODS_KEY_PREFIX+dateMenu).values();
    }

    @Override
    public SeckillGoods getSeckillGoodsById(String dateMenu, Long seckillGoodsId) {
        return (SeckillGoods) redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_GOODS_KEY_PREFIX+dateMenu).get(seckillGoodsId);
    }
}
