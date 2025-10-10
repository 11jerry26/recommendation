package com.example.recommendation.entity.request;

public class RecommendRequest {
    private int userId;
    private int pageNum;
    private int pageSize;

    public RecommendRequest(){}

    public RecommendRequest(int userId, int pageNum, int pageSize) {
        this.userId = userId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "RecommendRequest{" +
                "userId=" + userId +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
