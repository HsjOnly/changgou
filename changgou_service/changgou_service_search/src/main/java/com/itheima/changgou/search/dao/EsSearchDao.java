package com.itheima.changgou.search.dao;

import com.itheima.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/5 19:56
 */
@Repository
public interface EsSearchDao extends ElasticsearchRepository<SkuInfo, Long> {
}
