package com.itheima.changgou.feign;

import com.itheima.changgou.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/17 9:33
 */
@FeignClient("service-user")
@RequestMapping("/user")
public interface UserFeign {

    @GetMapping("/getUserByUsername")
    User getUserByUsername(@RequestParam("username") String username);

    @PostMapping("/updateUserByUsernameAndPoints")
    void updateUserByUsernameAndPoints(@RequestParam("username") String username, @RequestParam("points") Integer points);
}
