package com.designhubz.androidsdk.helper;

/**
 * The type Lock ui.
 */
public class LockUI {
    private Object lock = new Object();

    /**
     * Lock.
     */
    public void Lock(){
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Release.
     */
    public void Release(){
        synchronized (lock){
            lock.notify();
        }
    }
}
