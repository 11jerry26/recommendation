package com.example.recommendation.entity;

public class Product {
    private int productId;
    private int categoryId;
    private String productName;
    private String productSecondCategory;
    private String fileName;
    private String description;
    private Double price;
    private int sellCount;

    public Product() {}

    public Product(int productId, int categoryId, String productName, String productSecondCategory, String fileName, String description, Double price, int sellCount) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.productSecondCategory = productSecondCategory;
        this.fileName = fileName;
        this.description = description;
        this.price = price;
        this.sellCount = sellCount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSecondCategory() {
        return productSecondCategory;
    }

    public void setProductSecondCategory(String productSecondCategory) {
        this.productSecondCategory = productSecondCategory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getSellCount() {
        return sellCount;
    }

    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", categoryId=" + categoryId +
                ", productName='" + productName + '\'' +
                ", productSecondCategory='" + productSecondCategory + '\'' +
                ", fileName='" + fileName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", sellCount=" + sellCount +
                '}';
    }
}
