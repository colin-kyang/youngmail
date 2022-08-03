package com.example.youngmall.search.entity;

public class product {
    private String pName;
    private int pPrice;
    private String pId;

    public product(){

    }

    public product(String pName, int pPrice, String pId) {
        this.pName = pName;
        this.pPrice = pPrice;
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getpPrice() {
        return pPrice;
    }

    public void setpPrice(int pPrice) {
        this.pPrice = pPrice;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    @Override
    public String toString() {
        return "product{" +
                "pName='" + pName + '\'' +
                ", pPrice=" + pPrice +
                ", pId='" + pId + '\'' +
                '}';
    }
}
