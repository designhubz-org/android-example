package com.designhubz.androidsdk.helper;


/**
 * The type Request queue manager.
 *
 * @param <T> the type parameter
 */
public class RequestQueueManager<T> {

    /**
     * Generate request id int.
     *
     * @return the int
     */
    public int generateRequestID() {
        return (int) Math.floor(Math.random() * 65536);
    }

    /**
     * Add rquest.
     *
     * @param id       the id
     * @param callback the callback
     */
    public void addRquest(int id, T callback) {
        Constatnt.mRequestsMap.put(id, callback);
    }

    /**
     * Gets request.
     *
     * @param id the id
     * @return the callback
     */
    public Object getRequest(int id) {
        if (Constatnt.mRequestsMap.containsKey(id)) {
            System.out.println("Initial Mappings are: " + Constatnt.mRequestsMap);
            Object mCallback = Constatnt.mRequestsMap.get(id);
            Constatnt.mRequestsMap.remove(id);
            System.out.println("New map is: "+ Constatnt.mRequestsMap);
            return mCallback;
        }else
            return null;
    }

}
