package com.example.recommendation.entity;

public class Product {
    private int productId;
    private String productCategory;
    private String productName;
    private String productSecondCategory;
    private String fileName;
    private String description;
    private Double price;
    private int sellCount;

    public Product() {}

    public Product(int productId, String productCategory, String productName, String productSecondCategory, String fileName, String description, Double price) {
        this.productId = productId;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productSecondCategory = productSecondCategory;
        this.fileName = fileName;
        this.description = description;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
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
                ", productCategory='" + productCategory + '\'' +
                ", productName='" + productName + '\'' +
                ", productSecondCategory='" + productSecondCategory + '\'' +
                ", fileName='" + fileName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", sellCount=" + sellCount +
                '}';
    }
}
