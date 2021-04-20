package com.designhubz.androidsdk.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Communication.
 *
 * @param <T> the type parameter
 */
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
    // request queue map
    public static Map<Integer, Object> mRequestsMap = new HashMap<Integer, Object>();

    /**
     * Instantiates a new Communication.
     *
     * @param apiObject  the api object
     * @param property   the property
     * @param parameters the parameters
     */
    public Communication(int apiObject, String property, T[] parameters) {
        this.apiObject = apiObject;
        this.id = generateRequestID();
        this.property = property;
        this.parameters = parameters;
    }

    public Communication() {
    }

    /**
     * Generate request id int.
     *
     * @return the int
     */
    public int generateRequestID() {
        return (int) Math.floor(Math.random() * 65536);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Create msg string.
     *
     * @param callback the callback
     * @return the string
     */
    public String createMsg(T callback) {
        String s = "javascript:callDesignhubzAPI('";

        //Converting the communication object to Json + String
        s += new JSONHelper().convertObjecttoJson(this);
        s += "');";
        mRequestsMap.put(id, callback);
        return s;
    }

    /**
     * ProcessMsg.
     *
     * @param id the id
     * @return the callback
     */
    public Object processMsg(int id) {
        if (mRequestsMap.containsKey(id)) {
            System.out.println("Initial Mappings are: " + mRequestsMap);
            Object mCallback = mRequestsMap.get(id);
            mRequestsMap.remove(id);
            System.out.println("New map is: " + mRequestsMap);
            return mCallback;
        } else
            return null;
    }
}


