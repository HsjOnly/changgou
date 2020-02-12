package com.itheima.changgou.user.service;

import com.itheima.changgou.pojo.Address;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/20 21:26
 */
public interface AddressService {
    List<Address> listAddressByUsername(String username);
}
