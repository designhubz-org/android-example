package com.designhubz.androidsdk.helper;

public class Communication<T> {
    // 0 -> product
    // 1 -> video
    int apiObject;
    // Id of request
    int id;
    // Name of function
    String property;
    // Parameters of function
    T[] parameters;

    public Communication(int apiObject, int id, String property, T[] parameters) {
        this.apiObject = apiObject;
        this.id = id;
        this.property = property;
        this.parameters = parameters;
    }

    //todo refactor
    public String toString() {
        String s =  "javascript:callDesignhubzAPI('";

        //Converting the communication object to Json + String
        s += new JSONHelper().convertObjecttoJson(this);
        s += "');";
        return s;
    }
}


