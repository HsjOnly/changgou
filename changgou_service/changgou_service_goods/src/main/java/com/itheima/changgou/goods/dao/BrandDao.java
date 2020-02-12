package com.itheima.changgou.goods.dao;

import com.itheima.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandDao extends Mapper<Brand> {
    @Select("select b.* from tb_category_brand cb, tb_brand b where category_id = #{category} and cb.brand_id = b.id")
    List<Brand> listBrandByCategoryID(Integer categoryId);
}
