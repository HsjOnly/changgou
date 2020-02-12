package com.itheima.changgou.goods.service;

import com.itheima.changgou.goods.pojo.Para;

import java.util.List;

public interface ParaService {
    List<Para> listParaByCategoryID(Integer categoryID);
}
