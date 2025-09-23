package com.example.recommendation.service;

import com.example.recommendation.entity.UserBehavior;

public interface BehaviorService {
    public boolean browseProduct(UserBehavior userBehavior);
    public boolean collectProduct(UserBehavior userBehavior);
    public boolean searchCollectStatus(UserBehavior userBehavior);
}
