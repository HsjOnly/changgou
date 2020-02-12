package com.itheima.canal.pojo;

import java.io.Serializable;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/4 11:24
 */
public class ContentCategory implements Serializable {

    private Long id;
    private String name;

    public ContentCategory() {
    }

    public ContentCategory(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ContentCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
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
}
