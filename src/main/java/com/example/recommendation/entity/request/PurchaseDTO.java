package com.example.recommendation.entity.request;

public class PurchaseDTO {
    private int userId;
    private int productId;
    private int cartCount;
    private int sellCount;
    private String behaviorTime;

    public PurchaseDTO(){}

    public PurchaseDTO(int userId, int productId, int cartCount, int sellCount, String behaviorTime) {
        this.userId = userId;
        this.productId = productId;
        this.cartCount = cartCount;
        this.sellCount = sellCount;
        this.behaviorTime = behaviorTime;
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

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public int getSellCount() {
        return sellCount;
    }

    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

    public String getBehaviorTime() {
        return behaviorTime;
    }

    public void setBehaviorTime(String behaviorTime) {
        this.behaviorTime = behaviorTime;
    }

    @Override
    public String toString() {
        return "PurchaseDTO{" +
                "userId=" + userId +
                ", productId=" + productId +
                ", cartCount=" + cartCount +
                ", sellCount=" + sellCount +
                ", behaviorTime='" + behaviorTime + '\'' +
                '}';
    }
}
