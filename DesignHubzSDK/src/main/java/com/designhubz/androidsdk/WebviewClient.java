package com.designhubz.androidsdk;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebviewClient extends WebViewClient {

    protected long mLastError;

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (!hasError()) {
            if (DesignhubzWebview.mListener != null) {
                DesignhubzWebview.mListener.onPageStarted(url, favicon);
            }
        }
        if (DesignhubzWebview.mCustomWebViewClient != null) {
            DesignhubzWebview.mCustomWebViewClient.onPageStarted(view, url, favicon);
        }
    }


    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        setLastError();
        if (DesignhubzWebview.mListener != null) {
            DesignhubzWebview.mListener.onPageError(errorCode, description, failingUrl);
        }
        if (DesignhubzWebview.mCustomWebViewClient != null) {
            DesignhubzWebview.mCustomWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        if (DesignhubzWebview.mCustomWebViewClient != null) {
            if (DesignhubzWebview.mCustomWebViewClient.shouldOverrideUrlLoading(view, url)) {
                return true;
            }
        }
        view.loadUrl(url);
        return true;
    }

    /**
     * Sets last error.
     */
    protected void setLastError() {
        mLastError = System.currentTimeMillis();
    }

    /**
     * Has error boolean.
     *
     * @return the boolean
     */
    protected boolean hasError() {
        return (mLastError + 500) >= System.currentTimeMillis();
    }
}
