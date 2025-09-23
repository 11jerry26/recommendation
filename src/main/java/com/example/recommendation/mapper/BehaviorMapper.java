package com.example.recommendation.mapper;

import com.example.recommendation.entity.CartProduct;
import com.example.recommendation.entity.Product;
import com.example.recommendation.entity.UserBehavior;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface BehaviorMapper {

    //添加浏览数据
    public int browseProduct(int userId, int productId, int behaviorTypeId, String behaviorTime, int isDelete);

    //新增收藏
    public int insertCollect(int userId, int productId, int behaviorTypeId, String behaviorTime, int isDelete);

    //收藏/取消收藏
    public int updateCollect(int userId, int productId, int behaviorTypeId, String behaviorTime, int isDelete);

    //查询收藏数量
    public int searchCollectCount(int userId, int productId, int behaviorTypeId);

    //查询收藏记录
    public UserBehavior searchCollect(int userId, int productId, int behaviorTypeId);

    //添加购物表
    public int addCart(int userId, int productId, int count);

    //添加购物行为表
    public int addCartBehavior(int userId, int productId, int behaviorTypeId, String behaviorTime, int isDelete);

    //查询购物车商品列表
    public CartProduct[] getAllCartProduct(int userId);

    //添加入购物车前查询是否已经添加过
    public int searchCartProduct(int userId,int productId);

    //添加购物车行为前查询是否已经添加过
    public int searchCartBehavior(int userId,int productId,int behaviorTypeId);

    //添加购物车商品数量
    public int addCartProductCount(int userId,int productId,int count);

    //更新添加购物车行为
    public int updateCartBehavior(int userId,int productId,String behaviorTime, int isDelete);

    //更新购物车商品数量
    public int changeCartProductCount(int userId,int productId, int count);
}
