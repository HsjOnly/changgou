package com.itheima.changgou.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.canal.pojo.Content;
import com.itheima.changgou.canal.feign.ContentClient;
import com.itheima.changgou.entity.Result;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/4 11:42
 */
@Component
@CanalEventListener
public class CanalDataListener {

    @Autowired(required = false)
    private ContentClient contentClient;

    @Autowired
    private RedisTemplate redisTemplate;

    /*
     * @Description 该方法用于监听表的UD操作，将最新的数据存入Redis
     * @Param [eventType, rowData]
     * @Return void
     * @Author narcissu
     * @Date 2020/1/4 16:35
     **/
    @ListenPoint(table = "tb_content", eventType = {CanalEntry.EventType.INSERT, CanalEntry.EventType.DELETE})
    public void listenInsertAndDelete(CanalEntry.EventType eventType, CanalEntry.RowData rowData) throws Exception {
        Long changeType = null;
        // 获取变化的行
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            // 获取变化的行的所属广告类型
            if ("category_id".equalsIgnoreCase(column.getName())) {
                changeType = Long.valueOf(column.getValue());
            }
        }
        // 调用Feign，查询该类广告内容的最新信息
        Result<List<Content>> result = contentClient.listContentsByCategoryId(changeType);

        // 将result转为字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String resultJson = objectMapper.writeValueAsString(result);

        // 将最新的信息存入Redis
        redisTemplate.opsForValue().set("category_id=" + changeType, resultJson);
    }

    @ListenPoint(table = "tb_content", eventType = {CanalEntry.EventType.UPDATE})
    public void listenUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) throws JsonProcessingException {
        Long beforeCategoryId = null;
        Long afterCategoryId = null;
        // 获取更新前的行
        for (CanalEntry.Column beforeColumn : rowData.getBeforeColumnsList()) {
            if ("category_id".equalsIgnoreCase(beforeColumn.getName())) {
                beforeCategoryId = Long.valueOf(beforeColumn.getValue());
            }
        }
        // 获取更新后的行
        for (CanalEntry.Column afterColumn : rowData.getAfterColumnsList()) {
            if ("category_id".equalsIgnoreCase(afterColumn.getName())) {
                afterCategoryId = Long.valueOf(afterColumn.getValue());
            }
        }

        // 先查询改变前的分类
        Result<List<Content>> result = contentClient.listContentsByCategoryId(beforeCategoryId);

        // 将result转为字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String resultJson = objectMapper.writeValueAsString(result);

        // 将最新的信息存入Redis
        redisTemplate.opsForValue().set("category_id=" + beforeCategoryId, resultJson);

        // 改变分类了，补充查询改变后的分类
        if (beforeCategoryId != afterCategoryId) {
            result = contentClient.listContentsByCategoryId(afterCategoryId);

            // 将result转为字符串
            resultJson = objectMapper.writeValueAsString(result);

            // 将最新的信息存入Redis
            redisTemplate.opsForValue().set("category_id=" + afterCategoryId, resultJson);

        }

    }
}
