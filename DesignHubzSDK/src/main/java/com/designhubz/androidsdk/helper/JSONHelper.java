package com.designhubz.androidsdk.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * The type Json helper.
 */
public class JSONHelper<T> {

    /**
     * Convert object to json string.
     *
     * @param object the object
     * @return the string
     */

     //TODO refactor
    public String convertObjecttoJson(T object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    /**
     * Convert json to object product.
     *
     * @param jsonString the json string
     * @param clazz
     * @return the product
     */
    public T convertJsontoObject(String jsonString, Class<T> clazz){
        Gson g = new Gson();
        return g.fromJson(jsonString, clazz);
    }

    public int getRequestid(String jsonString){
        RequestResponse requestResponse = (RequestResponse) new JSONHelper().convertJsontoObject(jsonString, RequestResponse.class);
        return requestResponse.result.getId();
    }
}
