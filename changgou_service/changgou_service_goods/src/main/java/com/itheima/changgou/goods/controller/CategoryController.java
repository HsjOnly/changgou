package com.itheima.changgou.goods.controller;

import com.itheima.changgou.costant.MessageConstants;
import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Category;
import com.itheima.changgou.goods.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /*
     * 根据父ID查询分类
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/30 16:46
     */
    @GetMapping("/list/{pid}")
    public Result<List<Category>> listCategoryByParentID(@PathVariable("pid") Integer pid) {
        List<Category> categories = categoryService.listCategoryByParentID(pid);
        return new Result<>(true, StatusCode.OK, MessageConstants.LIST_CATEGORY_BY_PARENT_ID_SUCCESS, categories);
    }
}
