package com.itheima.changgou.goods.service;

import com.itheima.changgou.goods.pojo.Spec;

import java.util.List;

public interface SpecService {
    List<Spec> listSpecByCategoryID(Integer categoryID);
}
