package com.example.recommendation.service.impl;

import com.example.recommendation.entity.CartProduct;
import com.example.recommendation.entity.UserBehavior;
import com.example.recommendation.entity.request.AddCartDTO;
import com.example.recommendation.mapper.BehaviorMapper;
import com.example.recommendation.service.BehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BehaviorServiceImpl implements BehaviorService {
    @Autowired
    BehaviorMapper behaviorMapper;

    @Override
    public boolean browseProduct(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        int productId = userBehavior.getProductId();
        int behaviorTypeId = userBehavior.getBehaviorTypeId();
        String behaviorTime = userBehavior.getBehaviorTime();
        int isDelete = userBehavior.getIsDelete();
        int result = behaviorMapper.browseProduct(userId,productId,behaviorTypeId,behaviorTime,isDelete);
        return result == 1;
    }

    @Override
    public boolean collectProduct(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        int productId = userBehavior.getProductId();
        int behaviorTypeId = userBehavior.getBehaviorTypeId();
        String behaviorTime = userBehavior.getBehaviorTime();
        int isDelete = userBehavior.getIsDelete();
        int isExist = behaviorMapper.searchCollectCount(userId,productId,behaviorTypeId);
        int result = 0;
        if (isExist == 0) {
            result = behaviorMapper.insertCollect(userId,productId,behaviorTypeId,behaviorTime,0);
        } else {
            result = behaviorMapper.updateCollect(userId,productId,behaviorTypeId,behaviorTime,isDelete);
        }
        return result == 1;
    }

    @Override
    public boolean searchCollectStatus(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        int productId = userBehavior.getProductId();
        int behaviorTypeId = userBehavior.getBehaviorTypeId();
        int count = behaviorMapper.searchCollectCount(userId,productId,behaviorTypeId);
        if (count == 0) {
            return false;
        } else {
            UserBehavior record = behaviorMapper.searchCollect(userId,productId,behaviorTypeId);
            if (record.getIsDelete() == 1) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public int addCart(AddCartDTO addCartDTO) {
        int userId = addCartDTO.getUserId();
        int productId = addCartDTO.getProductId();
        int count = addCartDTO.getCount();
        int isExist = behaviorMapper.searchCartProduct(userId,productId);
        int result = 0;
        if (isExist > 0) {
            //如果购物车表已有数据，就只加count
            result = behaviorMapper.addCartProductCount(userId,productId,count);
        } else {
            //否则新加一条数据
            result = behaviorMapper.addCart(userId,productId,count);
        }
        return result;
    }

    @Override
    public int addCartBehavior(AddCartDTO addCartDTO) {
        int userId = addCartDTO.getUserId();
        int productId = addCartDTO.getProductId();
        int behaviorTypeId = addCartDTO.getBehaviorTypeId();
        String behaviorTime = addCartDTO.getBehaviorTime();
        int isDelete = addCartDTO.getIsDelete();
        int isExist = behaviorMapper.searchCartBehavior(userId,productId,behaviorTypeId);
        int result = 0;
        if (isExist > 0) {
            //如果行为表中已添加过这个商品，那么更新行为时间到最新
            result = behaviorMapper.updateCartBehavior(userId,productId,behaviorTime,isDelete);
        } else {
            //否则新增行为
            result = behaviorMapper.addCartBehavior(userId,productId,behaviorTypeId,behaviorTime,isDelete);
        }
        return result;
    }

    @Override
    public CartProduct[] getAllCartProduct(UserBehavior userBehavior) {
        int userId = userBehavior.getUserId();
        return behaviorMapper.getAllCartProduct(userId);
    }

    @Override
    public int changeCartProductCount(AddCartDTO addCartDTO) {
        int userId = addCartDTO.getUserId();
        int productId = addCartDTO.getProductId();
        int cartCount = addCartDTO.getCount();
        int result = behaviorMapper.changeCartProductCount(userId,productId,cartCount);
        return result;
    }
}
