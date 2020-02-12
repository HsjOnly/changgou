package com.itheima.changgou.search.feign;

import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/10 16:43
 */

@FeignClient("service-search")
public interface SearchEsSearchClient {

    @GetMapping("/search")
    Result<Map> search(@RequestParam Map<String, String> paramMap);
}
