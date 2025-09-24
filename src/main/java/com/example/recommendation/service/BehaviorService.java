package com.example.recommendation.service;

import com.example.recommendation.entity.CartProduct;
import com.example.recommendation.entity.UserBehavior;
import com.example.recommendation.entity.request.AddCartDTO;
import com.example.recommendation.entity.request.PurchaseDTO;

public interface BehaviorService {
    //浏览
    public boolean browseProduct(UserBehavior userBehavior);

    //收藏
    public boolean collectProduct(UserBehavior userBehavior);
    public boolean searchCollectStatus(UserBehavior userBehavior);

    //加购
    public int addCart(AddCartDTO addCartDTO);
    public int addCartBehavior(AddCartDTO addCartDTO);
    public CartProduct[] getAllCartProduct(UserBehavior userBehavior);
    public int changeCartProductCount(AddCartDTO addCartDTO);
    public int deleteCartProduct(UserBehavior userBehavior);
    public int purchaseCartProduct(PurchaseDTO[] purchaseDTOS);
}
