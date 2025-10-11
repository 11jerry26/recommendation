package com.example.recommendation.entity.request;

public class GetProductByCategory {
    private int pageNum;
    private int pageSize;
    private int userId;
    private int categoryId;

    public GetProductByCategory(){}

    public GetProductByCategory(int pageNum, int pageSize, int userId, int categoryId) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.userId = userId;
        this.categoryId = categoryId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "GetProductByCategory{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                '}';
    }
}
