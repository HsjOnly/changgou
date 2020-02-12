package com.itheima.changgou.goods.feign;

import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/17 12:28
 */
@FeignClient("service-goods")
@RequestMapping("/sku")
public interface SkuFeign {

    @GetMapping("/getSkuById")
    Result<Sku> getSkuById(@RequestParam("skuId") Long skuId);

    @PostMapping("/updateSkuByIdAndSaleNum")
    void updateSkuByIdAndSaleNum(@RequestParam("skuId") Long skuId, @RequestParam("saleNum") Integer saleNum);
}
