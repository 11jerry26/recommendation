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

    //获取猜你喜欢商品列表
    @PostMapping("/recommendList")
    public Result getRecommendProductsByPage(@RequestBody RecommendRequest request) {
        // 计算分页
        Integer userId = request.getUserId();
        Integer curPage = request.getPageNum();
        Integer pageSize = request.getPageSize();

        // 先获取所有推荐商品对象（按优先级排序）
        // 这里的topN传一个足够大的值，比如Integer.MAX_VALUE
        List<Product> allRecommendProducts = recommendService.getHybridRecommend(userId,curPage,pageSize);

        // 分页截取
        int startIndex = (curPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, allRecommendProducts.size());

        if (startIndex >= allRecommendProducts.size()) {
            return new Result(200,Collections.emptyList(),"没有更多数据"); // 没有更多数据
        }

        return new Result(200,allRecommendProducts.subList(startIndex, endIndex),"获取猜你喜欢列表成功");
    }
}
