package com.itheima.changgou.order.service.impl;

import com.itheima.changgou.goods.feign.SkuFeign;
import com.itheima.changgou.goods.pojo.Sku;
import com.itheima.changgou.goods.pojo.Spu;
import com.itheima.changgou.goods.feign.SpuFeign;
import com.itheima.changgou.order.constant.OrderConstants;
import com.itheima.changgou.order.pojo.OrderItem;
import com.itheima.changgou.order.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/17 12:23
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Override
    public void addGoods(Long skuId, Integer num, String usernmae) {
        if (num <= 0) {
            // 如果数量小于等于0，删除商品
            redisTemplate.boundHashOps(OrderConstants.REDIS_SHOPPING_CART_PREFIX + usernmae).delete(skuId);
            return;
        }

        // 如果数量大于0
        if (redisTemplate.boundHashOps(OrderConstants.REDIS_SHOPPING_CART_PREFIX + usernmae).hasKey(skuId)) {
            // 商品已经存在，增加数量
            OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps(OrderConstants.REDIS_SHOPPING_CART_PREFIX + usernmae).get(skuId);
            Integer resultNum = orderItem.getNum();
            orderItem.setNum(resultNum+num);
            redisTemplate.boundHashOps(OrderConstants.REDIS_SHOPPING_CART_PREFIX + usernmae).put(skuId,orderItem);
            return;
        }
        // 如果不存在，新增商品
        // 根据商品ID查询Sku信息
        Sku sku = skuFeign.getSkuById(skuId).getData();
        if (sku == null) {
            throw new RuntimeException("您所添加的商品不存在");
        }
        Spu spu = spuFeign.getSpuById(sku.getSpuId()).getData();

        // 将Sku和Spu封装成OrderItem
        OrderItem orderItem = createOrderItem(num, sku, spu);

        // 商品信息存入用户的购物车(redis)
        redisTemplate.boundHashOps(OrderConstants.REDIS_SHOPPING_CART_PREFIX + usernmae).put(orderItem.getSkuId(), orderItem);
    }

    private OrderItem createOrderItem(Integer num, Sku sku, Spu spu) {
        // 封装购物车商品信息(订单商品信息)
        OrderItem orderItem = new OrderItem();
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(sku.getPrice() * num);
        orderItem.setImage(sku.getImage());
        return orderItem;
    }

    @Override
    public List<OrderItem> listGoods(String username) {
        List<OrderItem> orderItemList = redisTemplate.boundHashOps(OrderConstants.REDIS_SHOPPING_CART_PREFIX + username).values();
        return orderItemList;
    }
}
