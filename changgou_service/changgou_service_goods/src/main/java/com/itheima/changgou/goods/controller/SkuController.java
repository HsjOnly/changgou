package com.itheima.changgou.goods.controller;

import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Sku;
import com.itheima.changgou.goods.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/5 11:06
 */
@RestController
@RequestMapping("/sku")
public class SkuController {

    @Autowired
    private SkuService skuService;

    @GetMapping("/listSearchSkus")
    public Result<List<Sku>> listSearchSkus() {
        List<Sku> skus = skuService.listSearchSkus();
        return new Result<>(true, StatusCode.OK, "查询搜索展示的Sku成功", skus);
    }

    @GetMapping("/getSkuById")
    public Result<Sku> getSkuById(@RequestParam("skuId") Long skuId) {
        Sku sku = skuService.getSkuById(skuId);
        return new Result<>(true, StatusCode.OK, "查询sku成功", sku);
    }

    @PostMapping("/updateSkuByIdAndSaleNum")
    void updateSkuByIdAndSaleNum(@RequestParam("skuId") Long skuId, @RequestParam("saleNum") Integer saleNum){
        skuService.updateSkuByIdAndSaleNum(skuId, saleNum);
    }
}
