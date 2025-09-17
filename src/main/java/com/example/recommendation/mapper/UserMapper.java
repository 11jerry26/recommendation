package com.example.recommendation.mapper;

import com.example.recommendation.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
        //新增用户
        public int insertUser(User user);

        //根据用户名查询用户
        public User selectUserByAccount(String account);

        //查询所有用户名
        public List<String> findAllAccount();
}
