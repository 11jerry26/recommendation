package com.example.recommendation.controller;

import com.example.recommendation.entity.Result;
import com.example.recommendation.entity.UserBehavior;
import com.example.recommendation.service.BehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/behavior")
@CrossOrigin
public class BehaviorController {
    @Autowired
    private BehaviorService behaviorService;

    @PostMapping("/browseProduct")
    public Result browseProduct(@RequestBody UserBehavior userBehavior) {
        try {
            boolean successBrowse = behaviorService.browseProduct(userBehavior);
            return new Result(200,successBrowse,"新增浏览记录成功");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/collectProduct")
    public Result collectProduct(@RequestBody UserBehavior userBehavior) {
        try {
            boolean successHandleCollect = behaviorService.collectProduct(userBehavior);
            return new Result(200,successHandleCollect,"处理收藏成功");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/searchCollectStatus")
    public Result searchCollectStatus(@RequestBody UserBehavior userBehavior) {
        try {
            boolean isCollected = behaviorService.searchCollectStatus(userBehavior);
            return new Result(200,isCollected,"查询收藏记录成功");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }
}
