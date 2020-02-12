package com.itheima.changgou.goods.service.impl;

import com.itheima.changgou.goods.dao.CategoryDao;
import com.itheima.changgou.goods.dao.ParaDao;
import com.itheima.changgou.goods.pojo.Category;
import com.itheima.changgou.goods.pojo.Para;
import com.itheima.changgou.goods.service.ParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ParaServiceImpl implements ParaService {

    @Autowired
    private ParaDao paraDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Para> listParaByCategoryID(Integer categoryID) {
        // 先查出CategoryID所对应的Category
        Category category = categoryDao.selectByPrimaryKey(categoryID);
        // 使用Category中封装的TempalteID查询所对应的Para
        Example example = new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId", category.getTemplateId());
        return paraDao.selectByExample(example);
    }
}
