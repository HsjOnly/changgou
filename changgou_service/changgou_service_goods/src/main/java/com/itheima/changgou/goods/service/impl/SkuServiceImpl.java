package com.itheima.changgou.goods.service.impl;

import com.itheima.changgou.goods.dao.SkuDao;
import com.itheima.changgou.goods.pojo.Sku;
import com.itheima.changgou.goods.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/5 11:16
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired(required = false)
    private SkuDao skuDao;

    @Override
    public List<Sku> listSearchSkus() {
        // 获取搜索展示的Sku(条件：非删除 status != 3)
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("status", "3");
        return skuDao.selectByExample(example);
    }

    @Override
    public Sku getSkuById(Long skuId) {
        return skuDao.selectByPrimaryKey(skuId);
    }

    @Override
    public void updateSkuByIdAndSaleNum(Long skuId, Integer saleNum) {
        skuDao.updateSkuByIdAndSaleNum(skuId, saleNum);
    }

}
