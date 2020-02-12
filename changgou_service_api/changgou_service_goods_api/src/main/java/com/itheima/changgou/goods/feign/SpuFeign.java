package com.itheima.changgou.goods.feign;

import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/19 16:46
 */
@FeignClient("service-goods")
@RequestMapping("/spu")
public interface SpuFeign {

    @GetMapping("/getSpuById")
    Result<Spu> getSpuById(@RequestParam("spuId") Long spuId);
}
