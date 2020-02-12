package com.itheima.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.itheima.changgou.search.pojo.SkuInfo;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;

/**
 * @Description 此类用于自定义ES结果集与JavaBean的映射
 * @Author narcissu
 * @Date 2020/1/9 20:19
 */
public class HightSearchResultMapper implements SearchResultMapper {
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

        // 获取所有查询结果的条目，并遍历
        SearchHits hits = searchResponse.getHits();

        ArrayList<SkuInfo> skuInfos = new ArrayList<>();
        // 结果条目不为空进行遍历
        if (hits != null && hits.totalHits > 0) {
            for (SearchHit hit : hits) {

                // 取出每个条目的内容(JSON)，转为JavaBean
                SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);

                // 获取高亮内容
                HighlightField highlightField = hit.getHighlightFields().get("name");

                // 高亮内容不为空再进行处理
                if (highlightField != null) {
                    Text[] texts = highlightField.getFragments();
                    // 处理高亮的内容，拼接完整
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text fragment : texts) {
                        stringBuffer.append(fragment);
                    }
                    skuInfo.setName(stringBuffer.toString());
                }

                skuInfos.add(skuInfo);
            }
        }


        AggregatedPage aggregatedPage = new AggregatedPageImpl<SkuInfo>(skuInfos, pageable, searchResponse.getHits().totalHits, searchResponse.getAggregations(),searchResponse.getHits().getMaxScore());

        return aggregatedPage;
    }
}
