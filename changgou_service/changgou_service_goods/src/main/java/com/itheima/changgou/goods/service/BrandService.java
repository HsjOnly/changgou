package com.itheima.changgou.goods.service;

import com.github.pagehelper.PageInfo;
import com.itheima.changgou.goods.pojo.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> listAllBrand();

    Brand getBrandById(Integer id);

    void insertBrand(Brand brand);

    void updateBrand(Brand brand);

    void deleteBrand(Brand brand);

    List<Brand> listBrandByCondition(Brand brand);

    PageInfo<Brand> listBrandByPage(Integer currentPage, Integer pageSize);

    PageInfo<Brand> listBrandByConditionAndPage(Brand brand, Integer currentPage, Integer pageSize);

    List<Brand> listBrandByCategoryID(Integer categoryId);
}

