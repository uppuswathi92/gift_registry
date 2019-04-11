package com.uppu.swathi_giftregistryproject;

public class Product {
    private String productId;
    private String productColor;
    private String productLink;
    private String productName;

    public Product(String productId, String productColor, String productLink, String productName) {
        this.productId = productId;
        this.productColor = productColor;
        this.productLink = productLink;
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
