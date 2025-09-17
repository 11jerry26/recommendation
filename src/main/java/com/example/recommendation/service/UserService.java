package com.example.recommendation.service;


import com.example.recommendation.entity.User;

public interface UserService {
    public int addUser(User user);
    public int selectUser(String account,String password);
    public User selectUserByAccount(String account);
}
