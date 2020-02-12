package com.itheima.changgou.search.controller;

import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.search.service.EsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/5 9:58
 */
@RestController
@RequestMapping("/search")
public class EsSearchController {

    @Autowired
    private EsSearchService esSearchService;

    @RequestMapping("/importSkuInfo")
    public Result importSkuInfo() {
        esSearchService.importSkuInfo();
        return new Result(true, StatusCode.OK, "更新ES中的SKU信息成功");
    }

    // 前端将所有的搜索参数以Json形式发送过来，在此封装为Map
    @GetMapping
    public Result<Map> search(@RequestParam(required = false) Map<String, String> paramMap){
        Map resultMap = esSearchService.search(paramMap);
        return new Result(true, StatusCode.OK, "搜索成功", resultMap);
    }


}
