package com.itheima.changgou.content.controller;

import com.itheima.canal.pojo.Content;
import com.itheima.changgou.content.service.ContentService;
import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/4 17:09
 */

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/listContentsByCategoryId/{categoryId}")
    public Result listContentsByCategoryId(@PathVariable("categoryId") Long categoryId) {
        List<Content> contents = contentService.listContentsByCategoryId(categoryId);
        return new Result(true, StatusCode.OK, "根据categoryId查询Content成功", contents);
    }
}
