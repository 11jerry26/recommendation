package com.example.recommendation.service;

import com.example.recommendation.entity.Product;

import java.io.IOException;
import java.util.List;

public interface RecommendService {
    // 初始化模型（服务启动时调用）
    void initModel() throws IOException;

    // 获取混合推荐列表（推荐商品 + 普通商品）
    List<Product> getHybridRecommend(Integer userId, Integer curPage, Integer pageSize);
}
