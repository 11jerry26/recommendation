package com.example.recommendation.controller;

import com.example.recommendation.entity.Product;
import com.example.recommendation.entity.Result;
import com.example.recommendation.entity.UserBehavior;
import com.example.recommendation.entity.request.GetProductByCategory;
import com.example.recommendation.entity.request.GetProductByPage;
import com.example.recommendation.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/getProductByPage")
    public Result getProductByPage(@RequestBody GetProductByPage getProductByPage) {
        try {
            int pageNum = getProductByPage.getPageNum();
            int pageSize = getProductByPage.getPageSize();
            int userId = getProductByPage.getUserId();
            List<Product> productList = productService.getProductByPage(pageNum,pageSize,userId);
           return new Result(200,productList,"查询商品列表成功");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/getProductByCategory")
    public Result getProductByCategory(@RequestBody GetProductByCategory getProductByCategory) {
        try {
            int pageNum = getProductByCategory.getPageNum();
            int pageSize = getProductByCategory.getPageSize();
            int userId = getProductByCategory.getUserId();
            String category = getProductByCategory.getCategory();
            List<Product> productList = productService.getProductByCategory(pageNum,pageSize,userId,category);
            return new Result(200,productList,"查询商品列表成功");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }

    @PostMapping("/getProductById")
    public Result getProductById(@RequestBody Product product) {
        try {
           int productId = product.getProductId();
           Product returnProduct = productService.getProductById(productId);
            return new Result(200,returnProduct,"查询商品成功");
        } catch (Exception e) {
            // 服务器异常：属于“请求处理失败”，返回code=500
            return new Result(500, null, "服务器内部错误：" + e.getMessage());
        }
    }
}
