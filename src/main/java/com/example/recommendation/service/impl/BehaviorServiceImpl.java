package com.example.recommendation.service.impl;

import com.example.recommendation.entity.UserBehavior;
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
}
