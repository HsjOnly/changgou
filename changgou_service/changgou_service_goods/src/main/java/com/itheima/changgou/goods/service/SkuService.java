package com.itheima.changgou.goods.service;

import com.itheima.changgou.goods.pojo.Sku;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/5 11:16
 */
public interface SkuService {
    List<Sku> listSearchSkus();

    Sku getSkuById(Long skuId);

    void updateSkuByIdAndSaleNum(Long skuId, Integer saleNum);
}
