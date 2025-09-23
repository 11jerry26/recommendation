package com.example.recommendation.entity;

public class UserBehavior {
    private int id;
    private int userId;
    private int productId;
    private int behaviorTypeId;
    private String behaviorTime;
    private int isDelete;

    public UserBehavior() {}

    public UserBehavior(int id, int userId, int productId, int behaviorTypeId, String behaviorTime, int isDelete) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.behaviorTypeId = behaviorTypeId;
        this.behaviorTime = behaviorTime;
        this.isDelete = isDelete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getBehaviorTypeId() {
        return behaviorTypeId;
    }

    public void setBehaviorTypeId(int behaviorTypeId) {
        this.behaviorTypeId = behaviorTypeId;
    }

    public String getBehaviorTime() {
        return behaviorTime;
    }

    public void setBehaviorTime(String behaviorTime) {
        this.behaviorTime = behaviorTime;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "UserBehavior{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", behaviorTypeId=" + behaviorTypeId +
                ", behaviorTime='" + behaviorTime + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }
}
