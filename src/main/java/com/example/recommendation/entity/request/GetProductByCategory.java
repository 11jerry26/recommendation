package com.example.recommendation.entity.request;

public class GetProductByCategory {
    private int pageNum;
    private int pageSize;
    private int userId;
    private String category;

    public GetProductByCategory(){}

    public GetProductByCategory(int pageNum, int pageSize, int userId, String category) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.userId = userId;
        this.category = category;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "GetProductByCategory{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", userId=" + userId +
                ", category='" + category + '\'' +
                '}';
    }
}
