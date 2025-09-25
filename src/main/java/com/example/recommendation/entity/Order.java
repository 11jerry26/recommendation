package com.example.recommendation.entity;

public class Order {
    private int id;
    private int userId;
    private int productId;
    private int count;
    private double price;
    private String createTime;
    private int isDelete;

    public Order(){}

    public Order(int id, int userId, int productId, int count, double price, String createTime, int isDelete) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.count = count;
        this.price = price;
        this.createTime = createTime;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", count=" + count +
                ", price=" + price +
                ", createTime='" + createTime + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }
}
