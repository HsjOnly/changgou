package com.itheima.changgou.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/5 10:10
 */

@Document(indexName = "skuinfo", type = "skuinfo")
public class SkuInfo implements Serializable {

    @Id
    private Long id;

    @Field(type = FieldType.Text,analyzer = "ik_smart", index = true)
    private String name;

    private Long price;

    private Integer num;

//    @Field(index = false)
    private String image;

    private Date createTime;

    private Date updateTime;

    private Long spuId;

    private Integer categoryId;

    @Field(type = FieldType.Keyword)
    private String brandName;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    private String spec;

    private Integer saleNum;

    private Integer commentNum;

    private String status;

    private Map<String, String> specMap;

    public SkuInfo() {
    }

    public SkuInfo(Long id, String name, Long price, Integer num, String image, Date createTime, Date updateTime, Long spuId, Integer categoryId, String brandName, String categoryName, String spec, Integer saleNum, Integer commentNum, String status, Map<String, String> specMap) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.num = num;
        this.image = image;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.spuId = spuId;
        this.categoryId = categoryId;
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.spec = spec;
        this.saleNum = saleNum;
        this.commentNum = commentNum;
        this.status = status;
        this.specMap = specMap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getSpecMap() {
        return specMap;
    }

    public void setSpecMap(Map<String, String> specMap) {
        this.specMap = specMap;
    }
}
