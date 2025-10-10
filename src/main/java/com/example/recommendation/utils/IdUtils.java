package com.example.recommendation.utils;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理行为权重、校验ID合法性
 */
public class IdUtils {
    // 行为类型-权重映射
    private static final Map<Integer, Double> ACTION_WEIGHTS = new HashMap<>();
    static {
        ACTION_WEIGHTS.put(1, 1.0);  // 1=浏览
        ACTION_WEIGHTS.put(2, 1.8);  // 2=加购（权重高于浏览）
        ACTION_WEIGHTS.put(3, 2.5);  // 3=购买（权重最高）
        ACTION_WEIGHTS.put(4, 0.5);  // 4=取消收藏（权重最低）
    }

    /**
     * 获取行为类型对应的权重
     */
    public static double getActionWeight(int behaviorTypeId) {
        return ACTION_WEIGHTS.getOrDefault(behaviorTypeId, 1.0); // 未知行为默认权重1.0
    }

    /**
     * 校验商品ID合法性（避免0或负数ID）
     */
    public static boolean isValidProductId(int productId) {
        return productId > 0;
    }

    /**
     * 校验用户ID合法性
     */
    public static boolean isValidUserId(int userId) {
        return userId > 0;
    }
}
