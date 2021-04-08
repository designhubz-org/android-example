package com.designhubz.androidsdk.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
     * @return the product
     */
    public Product convertJsontoObject(String jsonString){
        Gson g = new Gson();
        Type type = new TypeToken<T>(){}.getType();
        return g.fromJson(jsonString, type);
    }
}
