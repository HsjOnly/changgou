package com.itheima.changgou.seckill.timer;

import com.itheima.changgou.seckill.constant.SeckillConstants;
import com.itheima.changgou.seckill.dao.SeckillGoodsDao;
import com.itheima.changgou.seckill.pojo.SeckillGoods;
import com.itheima.changgou.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/24 12:30
 */
@Component
public class SeckillTimer {

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SimpleDateFormat simpleDateFormat;


    @Scheduled(cron = "0/10 * * * * ?")
    public void importSeckillGoods(){
        // 获取需要展示的秒杀时间段的开始时间
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date startTime : dateMenus) {
            // 根据开始时间获取该时间段内的秒杀商拼
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            // 审核已通过
            criteria.andEqualTo("status", "1");
            // 有库存
            criteria.andGreaterThan("stockCount", 0);
            // 处在该时间段内(商品秒杀时间必须处于该秒杀时间段内，可与秒杀时间段不同步开始）
            criteria.andGreaterThanOrEqualTo("startTime", startTime);
            criteria.andLessThan("endTime", DateUtil.addDateHour(startTime, 2));
            // 取出所有已经存入redis中的秒杀商品
            Set keySet = redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_GOODS_KEY_PREFIX + simpleDateFormat.format(startTime)).keys();
            if (keySet != null && keySet.size()>0) {
                criteria.andNotIn("id", keySet);
            }

            List<SeckillGoods> seckillGoodsList = seckillGoodsDao.selectByExample(example);

            // 将之前未存入redis的存入redis中
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                    redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_GOODS_KEY_PREFIX + simpleDateFormat.format(startTime)).put(seckillGoods.getId(), seckillGoods);
            }
            // 给秒杀的Hash设置过期时间
            redisTemplate.boundHashOps(SeckillConstants.REDIS_SECKILL_GOODS_KEY_PREFIX + simpleDateFormat.format(startTime)).expireAt(DateUtil.addDateHour(startTime, 2));
        }
    }
}
