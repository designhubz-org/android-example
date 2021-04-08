package com.designhubz.androidsdk.helper;

public class Product {
    Double id;
    String name;
    String desc;
    int price;
    int orgPrice;

    public Product(Double id, String name, String desc, int price, int orgPrice) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.orgPrice = orgPrice;
    }

    public Double getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }

    public int getOrgPrice() {
        return orgPrice;
    }
}
