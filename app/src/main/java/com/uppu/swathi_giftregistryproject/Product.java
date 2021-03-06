package com.uppu.swathi_giftregistryproject;

//Model for product
public class Product {
    private String productId;
    private String productColor;
    private String productLink;
    private String productName;
    private int isPurchased;
    private String isPurchasedBy;
    private byte[] productImage;

    //constructor
    public Product(String productId, String productColor, String productLink, String productName, int isPurchased, String isPurchasedBy, byte[] productImage) {
        this.productId = productId;
        this.productColor = productColor;
        this.productLink = productLink;
        this.productName = productName;
        this.isPurchased = isPurchased;
        this.isPurchasedBy = isPurchasedBy;
        this.productImage = productImage;
    }

    //getters and setters
    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public int isPurchased() {
        return isPurchased;
    }

    public void setPurchased(int purchased) {
        isPurchased = purchased;
    }

    public String getIsPurchasedBy() {
        return isPurchasedBy;
    }

    public void setIsPurchasedBy(String isPurchasedBy) {
        this.isPurchasedBy = isPurchasedBy;
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
