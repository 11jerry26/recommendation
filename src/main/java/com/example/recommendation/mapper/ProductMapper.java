package com.example.recommendation.mapper;

import com.example.recommendation.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    //分页查询商品列表
    public List<Product> getProductByPage(int offset, int pageSize, int userId);
}
