package com.itheima.changgou.goods.service;

import com.itheima.changgou.goods.pojo.Goods;
import com.itheima.changgou.goods.pojo.Spu;

import java.math.BigInteger;

public interface SpuService {
    void insetOrUpdateSpu(Goods goods) throws Exception;

    Goods getSpuBySpuID(BigInteger spuID);

    void deleteSpuBySpuID(BigInteger spuID);

    void recoverSpuBySpuId(BigInteger spuId);

    void foreverDeleteSpuBySpuId(BigInteger spuId);

    int putawaySpusBySpuIds(BigInteger[] spuIds);

    int offShelvesSpusBySpuIds(BigInteger[] spuIds);

    Spu getSpuById(Long spuId);
}
