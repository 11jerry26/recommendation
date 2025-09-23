package com.example.recommendation.entity.request;

public class AddCartDTO {
    private int id;
    private int userId;
    private int productId;
    private int behaviorTypeId;
    private String behaviorTime;
    private int isDelete;
    private int count;

    public AddCartDTO() {}

    public AddCartDTO(int id, int userId, int productId, int behaviorTypeId, String behaviorTime, int isDelete, int count) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.behaviorTypeId = behaviorTypeId;
        this.behaviorTime = behaviorTime;
        this.isDelete = isDelete;
        this.count = count;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AddCartDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", behaviorTypeId=" + behaviorTypeId +
                ", behaviorTime='" + behaviorTime + '\'' +
                ", isDelete=" + isDelete +
                ", count=" + count +
                '}';
    }
}
