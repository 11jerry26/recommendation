package com.example.recommendation.controller;

import com.example.recommendation.entity.Result;
import com.example.recommendation.entity.User;
import com.example.recommendation.service.UserService;
import com.example.recommendation.utils.TokenPassJson;
import com.example.recommendation.utils.TokenProccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

//    //接收register请求
//    @PostMapping("/register")
//    public String addUser(@RequestBody User user) {
//        int i = userService.addUser(user);
//        if (i > 0) {
//            return "注册成功";
//        } else {
//            return "该用户名已存在";
//        }
//    }

    //接收登录请求
    @PostMapping("/login")
    public Result login(@RequestBody User user) {  // 使用@RequestBody接收前端传递的User对象
        // 获取用户名和密码
        String account = user.getAccount();
        String password = user.getPassword();

        try {
            // 调用服务层验证（业务逻辑）
            int resultCode = userService.selectUser(account, password);

            // 业务逻辑内的结果：均属于“请求成功处理”，返回code=200
            if (resultCode == 1) {
                // 业务成功
                String token = TokenProccessor.createToken(account);
                return new Result(200, token, "登录成功");
            } else if (resultCode == 2) {
                // 业务错误：密码错误
                return new Result(500, null, "密码错误");
            } else {
                // 业务错误：用户名不存在
                return new Result(500, null, "用户名不存在");
            }
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    //接收查询用户详情请求
    @PostMapping("/getUserInfo")
    public Result getUserInfo(@RequestBody User user) {
        try {
            String account = user.getAccount();
            if (userService.selectUserByAccount(account) != null) {
                User userInfo = userService.selectUserByAccount(account);
                return new Result(200, userInfo, "查询用户信息成功");
            } else {
                return new Result(500,null,"查询用户信息失败");
            }
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }
}

