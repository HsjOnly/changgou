package com.itheima.changgou.goods.controller;

import com.itheima.changgou.costant.MessageConstants;
import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Spec;
import com.itheima.changgou.goods.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/spec")
@RestController
public class SpecController {

    @Autowired
    private SpecService specService;
    /*
     * 此方法根据类型ID获取Spec
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/30 19:50
     */
    @GetMapping("/category/{categoryID}")
    public Result<List<Spec>> listSpecByCategoryID(@PathVariable("categoryID") Integer categoryID) {
        List<Spec> specs = specService.listSpecByCategoryID(categoryID);
        return new Result<>(true, StatusCode.OK, MessageConstants.LIST_SPEC_BY_CATEGORY_ID_SUEECEE, specs);

    }
}
