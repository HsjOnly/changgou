package com.itheima.changgou.goods.dao;

import com.itheima.changgou.goods.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuDao extends Mapper<Sku> {
    void insertSkuList(List<Sku> skus);

    void updateSkuByIdAndSaleNum(@Param("skuId") Long skuId, @Param("saleNum") Integer saleNum);
}
