package com.itheima.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.itheima.changgou.entity.Page;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Sku;
import com.itheima.changgou.search.dao.EsSearchDao;
import com.itheima.changgou.search.feign.GoodsSkuClient;
import com.itheima.changgou.search.pojo.SkuInfo;
import com.itheima.changgou.search.service.EsSearchService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/5 10:27
 */
@Service
public class EsSearchServiceImpl implements EsSearchService {

    @Autowired(required = false)
    private GoodsSkuClient goodsSkuClient;

    @Autowired
    private EsSearchDao esSearchDao;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /*
     * @Description 此方法用于将DB中的数据导入ES中
     * @Param []
     * @Return void
     * @Author narcissu
     * @Date 2020/1/9 9:49
     **/
    @Override
    public void importSkuInfo() {
        // 先调用Feign从goods_service中获取数据
        Result<List<Sku>> result = goodsSkuClient.listSearchSkus();
        List<Sku> skus = result.getData();

        // 封装为SkuInfo
        String skusJson = JSON.toJSONString(skus);
        List<SkuInfo> skuInfos = JSON.parseArray(skusJson, SkuInfo.class);

        // 将每个skuInfo中的spec转换为specMap
        for (SkuInfo skuInfo : skuInfos) {
            skuInfo.setSpecMap(JSON.parseObject(skuInfo.getSpec(), Map.class));
        }

        // 将SkuInfo存入Es中
        esSearchDao.saveAll(skuInfos);
    }

    /*
     * @Description 此方法用于查询
     * @Param [paramMap]
     * @Return java.util.Map
     * @Author narcissu
     * @Date 2020/1/9 9:50
     **/
    @Override
    public Map search(Map<String, String> paramMap) {
        // 获取关键字
        String keyword = paramMap.get("keyword");

        // 判断字符串是否为null、length=0、为空白符构成，若为则提供一个默认值，防止全表查询
        if (StringUtils.isBlank(keyword)) {
            keyword = "华为";
        }

        // 构建查询对象的构造者(对应Query DSL的大括号)
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        // 设置查询
        nativeSearchQueryBuilder.withQuery(QueryBuilders.termQuery("name", keyword));

        // 构建布尔过滤
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 添加分类过滤条件
        setFilterCondition(paramMap, boolQueryBuilder, "categoryNameFilterCondition", "categoryName");

        // 添加品牌过滤条件
        setFilterCondition(paramMap, boolQueryBuilder, "brandNameFilterCondition", "brandName");

        // 添加规格过滤条件
        for (Map.Entry<String, String> paramEntry : paramMap.entrySet()) {
            if (paramEntry.getKey().startsWith("spec_")) {
                // 如果参数的Key以Spec开头，则说明该参数是规格
                boolQueryBuilder.filter(QueryBuilders.termQuery("specMap."+paramEntry.getKey().substring(5)+".keyword", paramEntry.getValue()));
            }
        }

        // 添加价格过滤条件
        String priceFilterCondition = paramMap.get("priceFilterCondition");
        if (StringUtils.isNotBlank(priceFilterCondition)) {
            String[] priceRange = priceFilterCondition.split("-");
            if (priceRange[1].equalsIgnoreCase("*")) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(priceRange[0]));
            } else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(priceRange[0]).to(priceRange[1]));
            }
        }

        // 设置过滤
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);

        // 设置聚合
        // 添加分类聚合
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("categoryGrouping").field("categoryName").size(50));

        // 添加品牌聚合
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("brandNameGrouping").field("brandName").size(50));

        // 添加规格聚合
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("specGrouping").field("spec.keyword").size(1000));

        // 设置分页
        Page pageInfo = new Page<>();
        setPageableCondition(paramMap, nativeSearchQueryBuilder);

        // 设置排序
        setOrderCondition(paramMap, nativeSearchQueryBuilder);

        // 设置高亮
        // 设置高亮的字段
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("name"));
        nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder().preTags("<em style='color:red'>").postTags("</em>"));

        // 使用前面设置的条件构造查询对象
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();

        // 执行查询获取结果
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(nativeSearchQuery, SkuInfo.class, new HightSearchResultMapper());

        // 获取分类分组名称
        ArrayList<String> categoryGroupingNames = listAggregationGroupingName(page, "categoryGrouping");

        // 获取品牌分组名称
        ArrayList<String> brandGroupingNames = listAggregationGroupingName(page, "brandNameGrouping");

        // 获取所有规格组合名称（JSON）
        ArrayList<String> specGroupingNames = listAggregationGroupingName(page, "specGrouping");

        // 取出页面的内容
        List<SkuInfo> skuInfos = page.getContent();

        // 封装Map并返回(当前页码、每页条数、总条数、总页数、当前页面展示的数据、分类信息等）
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("rows", skuInfos);
        resultMap.put("totalCount", page.getTotalElements());
        resultMap.put("totalPages", page.getTotalPages());
        resultMap.put("categoryGroupingNames", categoryGroupingNames);
        resultMap.put("brandGroupingNames", brandGroupingNames);

        // 遍历，处理其中的每个组合，将所有规格名称和对应的参数取出，将规格名称相同的参数存入同一个集合
        for (String specGroupingName : specGroupingNames) {
            // 先将Json转为Map
            Map<String, String> specGroupingNameMap = JSON.parseObject(specGroupingName, Map.class);
            // 遍历每个组合的Map获取其中每个的规格名和参数，将相同规格名的参数聚合起来成为一个集合
            for (Map.Entry<String,String> entry : specGroupingNameMap.entrySet()) {

                String specName = "spec_" + entry.getKey();
                String specValue = entry.getValue();

                Object specParam = resultMap.get(specName);

                // 如果之前未添加该规格名
                if (specParam == null) {
                    specParam = new HashSet<>();
                }

                // 如果之前添加了该规格名，取出已有的集合，在集合中添加新的规格值
                ((HashSet<String>)specParam).add(specValue);
                resultMap.put(specName, specParam);
            }
        }

        // 封装页面信息
        pageInfo.initPage(page.getTotalPages(), Integer.parseInt(paramMap.get("pageNum")), Integer.parseInt(paramMap.get("pageSize")));
        resultMap.put("pageInfo", pageInfo);

        return resultMap;
    }

    /*
     * @Description 此方法用于添加排序条件
     * @Param [paramMap, nativeSearchQueryBuilder]
     * @Return void
     * @Author narcissu
     * @Date 2020/1/9 11:06
     **/
    private void setOrderCondition(Map<String, String> paramMap, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        String sortField = paramMap.get("sortField");
        String sortRule = paramMap.get("sortRule");
        if (StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(sortRule)) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortRule.equals("ASC")? SortOrder.ASC : SortOrder.DESC));
        }
    }


    /*
     * @Description 此方法用于添加分页条件
     * @Param [paramMap, nativeSearchQueryBuilder]
     * @Return void
     * @Author narcissu
     * @Date 2020/1/9 10:38
     **/
    private void setPageableCondition(Map<String, String> paramMap, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        // 处理页码(默认1)
        int pageNum = 1;
        if (StringUtils.isNotBlank(paramMap.get("pageNum"))) {
            // 如果传入的页码不为空
            pageNum = Integer.parseInt(paramMap.get("pageNum"));
        }

        //  处理每页大小(默认10)
        int pageSize = 10;
        String paramPageSize = paramMap.get("pageSize");
        if (StringUtils.isNotBlank(paramPageSize) && Integer.parseInt(paramPageSize) <= 100) {
            // 如果传入的每页大小不为空,且不大于100
            pageSize = Integer.parseInt(paramPageSize);
        }

        // 查询对象构造者添加分页条件
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum, pageSize));

        // 把处理好的分页信息存回Map，用于展示
        paramMap.put("pageNum", pageNum+"");
        paramMap.put("pageSize", pageSize+"");
    }



    /*
     * @Description 此方法用于添加词条过滤条件
     * @Param [paramMap, boolQueryBuilder, paramMapFilterConditionKey, filterFieldName]
     * @Return void
     * @Author narcissu
     * @Date 2020/1/9 9:47
     **/
    private void setFilterCondition(Map<String, String> paramMap, BoolQueryBuilder boolQueryBuilder, String paramMapFilterConditionKey, String filterFieldName) {
        String categoryNameFilterCondition = paramMap.get(paramMapFilterConditionKey);

        if (StringUtils.isNotBlank(categoryNameFilterCondition)) {
            // 如果前台传递条件则添加
            boolQueryBuilder.filter(QueryBuilders.termQuery(filterFieldName, categoryNameFilterCondition));
        }
    }




    /*
     * @Description 此方法用于获取聚合各分组名称，步骤：AggregatedPage（查询结果）-> Aggregation（聚合）
     *              -> Buckets（分组） -> Keys（分组名称）
     * @Param [page, aggregationName]
     * @Return java.util.ArrayList<java.lang.String>
     * @Author narcissu
     * @Date 2020/1/9 9:26
     **/
    private ArrayList<String> listAggregationGroupingName(AggregatedPage page, String aggregationName) {
        // 根据分组名获取分组
        StringTerms aggregation = (StringTerms) page.getAggregation(aggregationName);

        // 获取分组中的Buckets
        List<StringTerms.Bucket> buckets = aggregation.getBuckets();

        ArrayList<String> Keys = new ArrayList<>();

        // 若Buckets中有数据，遍历Buckets
        if (buckets != null && !buckets.isEmpty()) {

            // 获取所有的Keys
            for (StringTerms.Bucket bucket : buckets) {
                String key = bucket.getKeyAsString();
                Keys.add(key);
            }
        }
        return Keys;
    }

}
