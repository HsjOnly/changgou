package com.itheima.changgou.user.service;

import com.itheima.changgou.pojo.User;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/12 21:26
 */
public interface UserService {
    User getUserByUsername(String username);

    void updateUserByUsernameAndPoints(String username, Integer points);
}
