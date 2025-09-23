package com.example.recommendation.mapper;

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
}
