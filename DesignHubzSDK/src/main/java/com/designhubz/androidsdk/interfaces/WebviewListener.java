package com.designhubz.androidsdk.interfaces;

import android.graphics.Bitmap;

public interface WebviewListener {
    void onPageStarted(String url, Bitmap favicon);
    void onPageFinished(String url);
    void onPageError(int errorCode, String description, String failingUrl);
    void initializeCamera();
    void detectingFace();
    void initializingFacePoints();
    void initializingProductPoints();
    void preparingFinalResult();
}
