package com.itheima.changgou.goods.controller;

import com.itheima.changgou.costant.MessageConstants;
import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Template;
import com.itheima.changgou.goods.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;
    /*
     * 此方法用于根据Category的ID查询模板
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/30 19:19
     */
    @GetMapping("/category/{categoryID}")
    public Result<Template> getTemplateByID(@PathVariable("categoryID") Integer categoryID) {
        Template template = templateService.getTemplateByCategoryID(categoryID);
        return new Result<>(true, StatusCode.OK, MessageConstants.GET_TEMPLATE_BY_CATEGORY_ID_SUCCESS, template);
    }
}
