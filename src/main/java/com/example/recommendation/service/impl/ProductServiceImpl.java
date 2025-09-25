package com.example.recommendation.service.impl;

import com.example.recommendation.entity.Product;
import com.example.recommendation.entity.UserBehavior;
import com.example.recommendation.mapper.ProductMapper;
import com.example.recommendation.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Override
    public List<Product> getProductByPage(int pageNum, int pageSize, int userId) {
        int offset = (pageNum - 1) * pageSize;
        return productMapper.getProductByPage(offset, pageSize, userId);
    }

    @Override
    public Product getProductById(int productId) {
        return productMapper.getProductInfoById(productId);
    }

}
