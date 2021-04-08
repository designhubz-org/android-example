package com.designhubz.androidsdk;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;


public class JavaScriptInterface {

    @android.webkit.JavascriptInterface
    public static void onAndroidReceive(String result) {
        Log.i("onAndroidReceive",""+result);
        DesignhubzWebview.mOnAndroidResult.onAndroidReceiveResponse(result);
    }

    @JavascriptInterface
    public static void initializingCamera() {
        DesignhubzWebview.mListener.initializeCamera();
        //this is for testing
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JavaScriptInterface.detectingFace();
            }
        },2500);
    }
    @JavascriptInterface
    public static void detectingFace() {
        DesignhubzWebview.mListener.detectingFace();
        //this is for testing
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JavaScriptInterface.initializingFacePoints();
            }
        },2500);
    }
    @JavascriptInterface
    public static void initializingFacePoints() {
        DesignhubzWebview.mListener.initializingFacePoints();
        //this is for testing
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JavaScriptInterface.initializingProductPoints();
            }
        },2500);
    }
    @JavascriptInterface
    public static void initializingProductPoints() {
        DesignhubzWebview.mListener.initializingProductPoints();
        //this is for testing
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JavaScriptInterface.preparingFinalResult();
            }
        },2500);
    }
    @JavascriptInterface
    public static void preparingFinalResult() {
        DesignhubzWebview.mListener.preparingFinalResult();
    }
}
