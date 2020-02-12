package com.itheima.changgou.goods.service.impl;

import com.itheima.changgou.goods.dao.CategoryDao;
import com.itheima.changgou.goods.pojo.Category;
import com.itheima.changgou.goods.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Category> listCategoryByParentID(Integer pid) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", pid);
        List<Category> categories = categoryDao.selectByExample(example);
        return categories;
    }
}
