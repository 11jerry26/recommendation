package com.example.recommendation.service.impl;

import com.example.recommendation.entity.User;
import com.example.recommendation.mapper.UserMapper;
import com.example.recommendation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public int addUser(User user) {
        if (userMapper.findAllAccount().contains(user.getAccount())) {
            return -1; //用户名已存在
        }else {
            return userMapper.insertUser(user);
        }
    }

    @Override
    public int selectUser(String account, String password) {
        if (userMapper.selectUserByAccount(account) == null) {
            return 0; //用户名不存在
        } else {
            User user = userMapper.selectUserByAccount(account);
            if (password.equals(user.getPassword())) {
                return 1; //密码正确
            }else {
                return 2; //密码错误
            }
        }
    }

    @Override
    public User selectUserByAccount(String account) {
        return userMapper.selectUserByAccount(account);
    }
}
