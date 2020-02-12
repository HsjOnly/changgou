package com.itheima.changgou.goods.controller;


import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Goods;
import com.itheima.changgou.goods.pojo.Spu;
import com.itheima.changgou.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RequestMapping("/spu")
@RestController
public class SpuController {

    @Autowired
    private SpuService spuService;

    /*
     * 此方法用于新增/修改商品
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/31 12:54
     */
    @PostMapping
    public Result insetOrUpdateSpu(@RequestBody Goods goods) throws Exception {
        spuService.insetOrUpdateSpu(goods);
        return new Result(true, StatusCode.OK, "新增商品成功");
    }

    /*
     * 此方法用于根据SpuID获取Spu
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/31 19:39
     */
    @GetMapping("/{spuID}")
    public Result<Goods> getSpuBySpuID(@PathVariable("spuID") BigInteger spuID) {
        Goods goods = spuService.getSpuBySpuID(spuID);
        return new Result<>(true, StatusCode.OK, "根据spuID查询商品信息成功", goods);
    }

    /*
     * 此方法用于逻辑删除商品
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2020/1/1 15:59
     */
    @DeleteMapping("/{spuID}")
    public Result deleteSpuBySpuID(@PathVariable("spuID") BigInteger spuID) {
        spuService.deleteSpuBySpuID(spuID);
        return new Result(true, StatusCode.OK, "根据spuID删除商品成功");
    }

    /*
     * 此方法用于恢复逻辑删除的商品
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2020/1/1 17:50
     */
    @PutMapping("/recover/{spuId}")
    public Result recoverSpuBySpuId(@PathVariable("spuId") BigInteger spuId) {
        spuService.recoverSpuBySpuId(spuId);
        return new Result<>(true, StatusCode.OK, "根据spuId恢复商品成功");
    }

    /*
     * @Description 此方法用于永久删除商品
     * @Param [spuId]
     * @Return com.itheima.changgou.entity.Result
     * @Author narcissu
     * @Date 2020/1/1 19:22
     **/
    @DeleteMapping("/delete/{spuId}")
    public Result foreverDeleteSpuBySpuId(@PathVariable("spuId") BigInteger spuId) {
        spuService.foreverDeleteSpuBySpuId(spuId);
        return new Result(true, StatusCode.OK, "永久删除商品成功");
    }

    /*
     * @Description 此方法用于批量下架产品
     * @Param [spuIds]
     * @Return com.itheima.changgou.entity.Result
     * @Author narcissu
     * @Date 2020/1/2 9:14
     **/
    @PutMapping("/putaway")
    public Result auditSpusBySpuIds(@RequestBody BigInteger[] spuIds) {
        int count = spuService.putawaySpusBySpuIds(spuIds);
        return new Result(true, StatusCode.OK, "已上架"+ count +"个商品成功");
    }

    @PutMapping("/offshelves")
    public Result offShelvesSpusBySpuIds(@RequestBody BigInteger[] spuIds) {
        int count = spuService.offShelvesSpusBySpuIds(spuIds);
        return new Result(true, StatusCode.OK, "已下架"+ count +"个商品成功");
    }

    @GetMapping("/getSpuById")
    public Result<Spu> getSpuById(@RequestParam("spuId") Long spuId) {
        Spu spu = spuService.getSpuById(spuId);
        return new Result<>(true, StatusCode.OK, "获取Spu成功", spu);
    }
}
