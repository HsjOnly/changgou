package com.itheima.changgou.search.feign;

import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Sku;
import org.apache.lucene.util.fst.Util;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/5 10:49
 */
@FeignClient("service-goods")
public interface GoodsSkuClient {

    @GetMapping("/sku/listSearchSkus")
    Result<List<Sku>> listSearchSkus();
}
