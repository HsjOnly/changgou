package com.itheima.changgou.goods.controller;

import com.itheima.changgou.costant.MessageConstants;
import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Para;
import com.itheima.changgou.goods.service.ParaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/para")
public class ParaController {

    @Autowired
    private ParaService paraService;

    @GetMapping("/category/{categoryID}")
    public Result<List<Para>> listParaByCategoryID(@PathVariable("categoryID") Integer categoryID) {
        List<Para> paras = paraService.listParaByCategoryID(categoryID);
        return new Result<>(true, StatusCode.OK, MessageConstants.LIST_PARA_BY_CATEGORY_ID_SUCCESS, paras);
    }
}
