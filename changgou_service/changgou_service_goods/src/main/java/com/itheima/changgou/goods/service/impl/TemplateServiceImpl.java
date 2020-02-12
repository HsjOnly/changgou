package com.itheima.changgou.goods.service.impl;

import com.itheima.changgou.goods.dao.CategoryDao;
import com.itheima.changgou.goods.dao.TemplateDao;
import com.itheima.changgou.goods.pojo.Category;
import com.itheima.changgou.goods.pojo.Template;
import com.itheima.changgou.goods.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateDao templateDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public Template getTemplateByCategoryID(Integer categoryId) {
        // 先通过categoryID查询到分类信息
        Category category = categoryDao.selectByPrimaryKey(categoryId);
        // 使用分类中的templateID查询相应的模板
        return templateDao.selectByPrimaryKey(category.getTemplateId());
    }
}
