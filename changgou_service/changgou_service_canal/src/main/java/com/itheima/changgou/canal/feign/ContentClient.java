package com.itheima.changgou.canal.feign;

import com.itheima.canal.pojo.Content;
import com.itheima.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/4 16:46
 */
@FeignClient("service-content")
public interface ContentClient {

    // 请求服务的地址
    @RequestMapping("/content/listContentsByCategoryId/{categoryId}")
    public Result<List<Content>> listContentsByCategoryId(@PathVariable("categoryId") Long categoryId);
}
