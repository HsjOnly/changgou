package com.itheima.changgou.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.changgou.goods.dao.BrandDao;
import com.itheima.changgou.goods.pojo.Brand;
import com.itheima.changgou.goods.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired(required = false)
    private BrandDao brandDao;

    @Override
    public List<Brand> listAllBrand() {
        return brandDao.selectAll();
    }

    @Override
    public Brand getBrandById(Integer id) {
        return brandDao.selectByPrimaryKey(id);
    }

    @Override
    public void insertBrand(Brand brand) {
        brandDao.insertSelective(brand);
    }

    @Override
    public void updateBrand(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void deleteBrand(Brand brand) {
        brandDao.deleteByPrimaryKey(brand);
    }

    @Override
    public List<Brand> listBrandByCondition(Brand brand) {
        // 用于封装查询的表以及条件
        Example example = new Example(Brand.class);
        // 如果有条件，封装条件
        if (!ObjectUtils.isEmpty(brand)) {
            Example.Criteria criteria = example.createCriteria();
            // 有名称
            if (!StringUtils.isEmpty(brand.getName())) {
                criteria.andEqualTo("name", brand.getName());
            }
            // 有首字母
            if (!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andEqualTo("letter", brand.getLetter());
            }
        }
        return brandDao.selectByExample(example);
    }

    @Override
    public PageInfo<Brand> listBrandByPage(Integer currentPage, Integer pageSize) {
        // 开启分页工具
        PageHelper.startPage(currentPage, pageSize);
        // 执行查询
        List<Brand> brands = brandDao.selectAll();
        // 封装
        PageInfo<Brand> pageInfo = new PageInfo<>(brands, 10);
        return pageInfo;
    }

    @Override
    public PageInfo<Brand> listBrandByConditionAndPage(Brand brand, Integer currentPage, Integer pageSize) {
        // 开启分页
        PageHelper.startPage(currentPage, pageSize);
        List<Brand> brands = null;

        if (brand == null) {// 如果没有条件，则直接进行分页查询
             brands = brandDao.selectAll();

        } else {// 如果有条件
            // 用于封装查询的表以及条件
            Example example = new Example(Brand.class);
            if (!(brand.getId() == null)) { // 封装ID
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("id", brand.getId());
            }
            if (!(brand.getName() == null)) { // 封装名称
                Example.Criteria criteria = example.createCriteria();
                criteria.andLike("name", "%"+brand.getName()+"%");
            }
            if (!(brand.getLetter() == null)) { // 封装首字母
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("letter", brand.getLetter());
            }
            brands = brandDao.selectByExample(example);
        }

        PageInfo<Brand> pageInfo = new PageInfo<>(brands, 10);
        return pageInfo;
    }

    @Override
    public List<Brand> listBrandByCategoryID(Integer categoryId) {
        return brandDao.listBrandByCategoryID(categoryId);
    }
}
