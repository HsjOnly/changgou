package com.itheima.changgou.user.controller;

import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.BCrypt;
import com.itheima.changgou.util.IdWorker;
import com.itheima.changgou.util.JwtUtils;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.pojo.User;
import com.itheima.changgou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author narcissu
 * @Date 2020/1/12 21:18
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IdWorker idWorker;

    /*
     * @Description 此方法用于登入
     * @Param [username, password]
     * @Return com.itheima.changgou.entity.Result
     * @Author narcissu
     * @Date 2020/1/12 21:22
     **/
    @GetMapping(value = "/login")
    public Result login(String username, String password, HttpServletResponse response) {
        if (username == null || password == null) {
            // 传递参数不完全
            return new Result(false, StatusCode.LOGINERROR, "您所输入的用户名或密码错误");
        }

        User user = userService.getUserByUsername(username);

        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            // 用户名或密码错误
            return new Result(false, StatusCode.LOGINERROR, "您所输入的用户名或密码错误");
        }

        // 登入成功，生成Jwt
        String jwt = JwtUtils.createJWT(idWorker.nextId() + "", "没有主题", null);
        Cookie cookie = new Cookie("jwt", jwt);
        // 设置cookie的可用路径
        cookie.setPath("/");
        response.addCookie(cookie);

        return new Result(true, StatusCode.OK, "登入成功");
    }

    @GetMapping("/getUserByUsername")
    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    /**
     * 此方法用于增加用户积分
     * @Return void
     * @Author narcissu
     * @Date 2020/1/21 12:48
     **/
    @PostMapping("/updateUserByUsernameAndPoints")
    void updateUserByUsernameAndPoints(@RequestParam("username") String username,@RequestParam("points") Integer points){
        userService.updateUserByUsernameAndPoints(username,points);
    }
}
