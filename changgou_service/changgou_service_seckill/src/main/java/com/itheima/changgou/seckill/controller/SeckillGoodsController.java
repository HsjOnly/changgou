package com.itheima.changgou.seckill.controller;

import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.seckill.pojo.SeckillGoods;
import com.itheima.changgou.seckill.service.SeckillGoodsService;
import com.itheima.changgou.util.DateUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/24 17:29
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    /**
     *  此方法用于获取秒杀的时间段
     * @Return com.itheima.changgou.entity.Result<java.util.List < java.lang.String>>
     * @Author narcissu
     * @Date 2020/1/24 17:35
     **/
    @GetMapping("/getDateMenus")
    public Result<List<String>> getDateMenus(){
        // 获取秒杀时间段开始时间
        List<Date> dateMenus = DateUtil.getDateMenus();
        // 将秒杀时间段开始时间转换为字符串，存入集合
        ArrayList<String> dateMenuStrings = new ArrayList<>();
        for (Date dateMenu : dateMenus) {
            String dateMenuString = simpleDateFormat.format(dateMenu);
            dateMenuStrings.add(dateMenuString);
        }
        return new Result<>(true, StatusCode.OK, "获取秒杀时间段成功", dateMenuStrings);
    }

    /**
     * 此方法用于根据秒杀时间段获取秒杀商品
     * @Return com.itheima.changgou.entity.Result<java.util.List < com.itheima.changgou.seckill.pojo.SeckillGoods>>
     * @Author narcissu
     * @Date 2020/1/24 17:40
     **/
    @GetMapping("/getSeckillGoodsByDateMenu")
    public Result<List<SeckillGoods>> getSeckillGoodsByDateMenu(String dateMenu) {
        List<SeckillGoods> seckillGoodsList = seckillGoodsService.getSeckillGoodsByDateMenu(dateMenu);
        return new Result<>(true, StatusCode.OK, "根据秒杀时间段获取秒杀商品成功", seckillGoodsList);
    }

    /**
     * 此方法用于根据秒杀商品Id获取秒杀商品
     * @Return com.itheima.changgou.entity.Result<com.itheima.changgou.seckill.pojo.SeckillGoods>
     * @Author narcissu
     * @Date 2020/1/24 19:05
     **/
    @GetMapping("getSeckillGoodsById")
    public Result<SeckillGoods> getSeckillGoodsById(@RequestParam(required = true) String dateMenu, @RequestParam(required = true) Long seckillGoodsId){
        SeckillGoods seckillGoods = seckillGoodsService.getSeckillGoodsById(dateMenu, seckillGoodsId);
        return new Result<>(true, StatusCode.OK, "根据秒杀商品Id获取秒杀商品成功", seckillGoods);
    }
}
