package com.example.recommendation.entity.request;

public class GetProductByPage {
    private int pageNum;
    private int pageSize;
    private int userId;
    private String searchInput;

    public GetProductByPage(){}

    public GetProductByPage(int pageNum, int pageSize, int userId, String searchInput) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.userId = userId;
        this.searchInput = searchInput;
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

    public String getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    @Override
    public String toString() {
        return "GetProductByPage{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", userId=" + userId +
                ", searchInput='" + searchInput + '\'' +
                '}';
    }
}
