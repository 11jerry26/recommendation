package com.example.recommendation.controller;

import com.example.recommendation.entity.*;
import com.example.recommendation.entity.request.AddCartDTO;
import com.example.recommendation.entity.request.PurchaseDTO;
import com.example.recommendation.service.BehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/behavior")
@CrossOrigin
public class BehaviorController {
    @Autowired
    private BehaviorService behaviorService;

    //浏览
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

    //收藏
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

    //加购
    @PostMapping("/addCart")
    public Result addCart(@RequestBody AddCartDTO addCartDTO) {
        try {
            int result1 = behaviorService.addCart(addCartDTO);
            int result2 = behaviorService.addCartBehavior(addCartDTO);
            boolean result = result1 != 0 && result2 != 0;
            return new Result(200,result,"添加商品到购物车成功！");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/getAllCartProduct")
    public Result getAllCartProduct(@RequestBody UserBehavior userBehavior) {
        try {
            CartProduct[] products = behaviorService.getAllCartProduct(userBehavior);
            return new Result(200,products,"查询购物车列表成功！");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/changeCartProductCount")
    public Result changeCartProductCount(@RequestBody AddCartDTO addCartDTO) {
        try {
            int result = behaviorService.changeCartProductCount(addCartDTO);
            return new Result(200,result,"修改购物车商品数量成功！");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/deleteCartProduct")
    public Result deleteCartProduct(@RequestBody UserBehavior userBehavior) {
        try {
            int result = behaviorService.deleteCartProduct(userBehavior);
            return new Result(200,result,"修改购物车商品数量成功！");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/purchaseCartProduct")
    public Result purchaseCartProduct(@RequestBody PurchaseDTO[] purchaseDTOS) {
        try {
            int result = behaviorService.purchaseCartProduct(purchaseDTOS);
            return new Result(200,result,"购物车商品购买成功！");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/getMyCollection")
    public Result getMyCollection(@RequestBody UserBehavior userBehavior) {
        try {
            Product[] products = behaviorService.selectUserCollection(userBehavior);
            return new Result(200,products,"查询收藏成功！");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/getMyOrder")
    public Result getMyOrder(@RequestBody UserBehavior userBehavior) {
        try {
            Order[] orders = behaviorService.selectUserOrder(userBehavior);
            return new Result(200,orders,"查询订单成功！");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/deleteUserOrder")
    public Result deleteUserOrder(@RequestBody Order order) {
        try {
            Order receive = order;
            int result = behaviorService.deleteUserOrder(order);
            return new Result(200,result,"删除订单成功！");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }
}
