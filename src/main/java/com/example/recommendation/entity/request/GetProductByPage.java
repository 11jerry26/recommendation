package com.example.recommendation.entity.request;

public class GetProductByPage {
    private int pageNum;
    private int pageSize;
    private int userId;

    public GetProductByPage(int pageNum, int pageSize, int userId) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GetProductByPage{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", userId=" + userId +
                '}';
    }
}
