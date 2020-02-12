package com.itheima.changgou.user.service.impl;

import com.itheima.changgou.pojo.Address;
import com.itheima.changgou.user.dao.AddressDao;
import com.itheima.changgou.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/20 21:26
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Address> listAddressByUsername(String username) {
        Address address = new Address();
        address.setUsername(username);
        return addressDao.select(address);
    }
}
