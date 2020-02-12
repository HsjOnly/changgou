package com.itheima.changgou.content.service.impl;

import com.itheima.canal.pojo.Content;
import com.itheima.changgou.content.dao.ContentDao;
import com.itheima.changgou.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/4 17:28
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao contentDao;

    @Override
    public List<Content> listContentsByCategoryId(Long categoryId) {
        Content content = new Content();
        content.setCategoryId(categoryId);
        return contentDao.select(content);
    }
}
