package com.itheima.changgou.user.dao;

import com.itheima.changgou.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/12 21:27
 */
public interface UserDao extends Mapper<User> {
    @Update("update tb_user set points = points + #{points} where username = #{username}")
    void updateUserByUsernameAndPoints(@Param("username") String username,@Param("points") Integer points);
}
