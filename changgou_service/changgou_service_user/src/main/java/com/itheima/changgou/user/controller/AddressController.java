package com.itheima.changgou.user.controller;

import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.pojo.Address;
import com.itheima.changgou.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/20 21:22
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;


    @GetMapping("/listAddressByUsername")
    public Result<List<Address>> listAddressByUsername(String username) {
        // 查询用户地址
        List<Address> addressList = addressService.listAddressByUsername(username);
        return new Result<>(true, StatusCode.OK, "查询用户地址成功", addressList);
    }
}
