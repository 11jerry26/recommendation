package com.example.recommendation.controller;


import com.example.recommendation.entity.Product;
import com.example.recommendation.entity.Result;
import com.example.recommendation.entity.request.RecommendRequest;
import com.example.recommendation.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/recommend")
@CrossOrigin
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    // 获取猜你喜欢商品列表
    @PostMapping("/recommendList")
    public Result getRecommendProductsByPage(@RequestBody RecommendRequest request) {
        Integer userId = request.getUserId();
        Integer curPage = request.getPageNum();
        Integer pageSize = request.getPageSize();

        // 获取当前页商品
        List<Product> currentPageProducts = recommendService.getHybridRecommend(userId, curPage, pageSize);

        if (currentPageProducts.isEmpty()) {
            return new Result(200, Collections.emptyList(), "没有更多数据");
        }

        return new Result(200, currentPageProducts, "获取猜你喜欢列表成功");
    }
}
