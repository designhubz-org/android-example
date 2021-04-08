package com.designhubz.androidsdk.helper;

public enum APIObject {
    PRODUCT(0),//0
    VIDEO(1)//1
    ;

    private final int value;
    APIObject(int i) {
        this.value = i;
    }
    public int getValue() {
        return value;
    }
}
