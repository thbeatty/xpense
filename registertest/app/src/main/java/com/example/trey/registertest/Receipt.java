package com.example.trey.registertest;

public class Receipt {
    private String storeName;
    private String price;
    private String category;
    private String date;
    private String receiptID;

    public Receipt(String storeName, String price, String category, String date, String receiptID) {
        this.storeName = storeName;
        this.price = price;
        this.category = category;
        this.date = date;
        this.receiptID = receiptID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }


}