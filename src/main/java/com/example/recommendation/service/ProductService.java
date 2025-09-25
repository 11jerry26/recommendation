package com.example.recommendation.service;

import com.example.recommendation.entity.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getProductByPage(int pageNum, int pageSize, int userId);
    public Product getProductById(int productId);
}
