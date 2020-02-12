package com.itheima.changgou.content.service;

import com.itheima.canal.pojo.Content;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/4 17:15
 */
public interface ContentService {
    List<Content> listContentsByCategoryId(Long categoryId);
}
