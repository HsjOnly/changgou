package com.itheima.changgou.search.service;

import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/5 10:27
 */
public interface EsSearchService {
    void importSkuInfo();

    Map search(Map<String, String> paramMap);
}
