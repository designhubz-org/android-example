package com.designhubz.androidsdk.helper;

public class RequestResponse {
    public Result result;

    public class ApiObject{
        public int id;
        public String value;

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
    }

    public class Result{
        public int id;
        public Product.ApiObject apiObject;

        public int getId() {
            return id;
        }

        public Product.ApiObject getApiObject() {
            return apiObject;
        }
    }
}
