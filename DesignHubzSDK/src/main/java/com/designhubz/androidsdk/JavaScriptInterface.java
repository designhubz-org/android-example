package com.designhubz.androidsdk;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.designhubz.androidsdk.helper.Communication;
import com.designhubz.androidsdk.helper.JSONHelper;
import com.designhubz.androidsdk.interfaces.OnAndroidResult;


public class JavaScriptInterface {

    @android.webkit.JavascriptInterface
    public static void onAndroidReceive(String result) {
        Log.i("onAndroidReceive", "" + result);
        OnAndroidResult onAndroidResult = (OnAndroidResult) new Communication<>().processMsg(new JSONHelper<>().getRequestid(result));
        onAndroidResult.onAndroidReceiveResponse(result);
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
        }, 2500);
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
        }, 2500);
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
        }, 2500);
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
        }, 2500);
    }

    @JavascriptInterface
    public static void preparingFinalResult() {
        DesignhubzWebview.mListener.preparingFinalResult();
    }
}
