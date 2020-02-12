package com.itheima.changgou.goods.service;

import com.itheima.changgou.goods.pojo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listCategoryByParentID(Integer pid);
}

