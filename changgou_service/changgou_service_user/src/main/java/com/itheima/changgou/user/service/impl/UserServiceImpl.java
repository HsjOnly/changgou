package com.itheima.changgou.user.service.impl;

import com.itheima.changgou.pojo.User;
import com.itheima.changgou.user.dao.UserDao;
import com.itheima.changgou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/12 21:26
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return userDao.selectOne(user);
    }

    @Override
    public void updateUserByUsernameAndPoints(String username, Integer points) {
        userDao.updateUserByUsernameAndPoints(username, points);
    }
}
