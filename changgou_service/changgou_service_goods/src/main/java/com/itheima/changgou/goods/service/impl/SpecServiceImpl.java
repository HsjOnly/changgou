package com.itheima.changgou.goods.service.impl;

import com.itheima.changgou.goods.dao.CategoryDao;
import com.itheima.changgou.goods.dao.SpecDao;
import com.itheima.changgou.goods.pojo.Category;
import com.itheima.changgou.goods.pojo.Spec;
import com.itheima.changgou.goods.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecServiceImpl implements SpecService {

    @Autowired
    private SpecDao specDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Spec> listSpecByCategoryID(Integer categoryID) {
        // 先查询categoryID所对应的category，从中获取TemplateID
        Category category = categoryDao.selectByPrimaryKey(categoryID);
        // 使用templateID查询对应的SPEC
        Example example = new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId", category.getTemplateId());
        return specDao.selectByExample(example);
    }
}
