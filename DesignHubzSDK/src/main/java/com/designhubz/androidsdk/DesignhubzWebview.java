package com.designhubz.androidsdk;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.designhubz.androidsdk.helper.APIHelper;
import com.designhubz.androidsdk.helper.APIObject;
import com.designhubz.androidsdk.helper.Communication;
import com.designhubz.androidsdk.helper.Product;
import com.designhubz.androidsdk.interfaces.OnAndroidResult;
import com.designhubz.androidsdk.interfaces.WebviewListener;

import java.lang.ref.WeakReference;


/**
 * The DesignHubz webview.
 */
public class DesignhubzWebview<T> extends WebView {

    public static WebView webView;
    protected WeakReference<Activity> mActivity;
    protected static WebviewListener mListener;
    protected static WebViewClient mCustomWebViewClient;
    protected static OnAndroidResult mOnAndroidResult;
    public static String mCameraFacing="user";

    /**
     * Instantiates a new webview.
     *
     * @param context the context
     */
    public DesignhubzWebview(Context context) {
        super(context);
        webView = this;
    }

    /**
     * Instantiates a new webview.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DesignhubzWebview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        webView = this;
    }

    /**
     * Sets listener.
     *
     * @param activity the activity
     * @param listener the listener
     */
    public void setListener(final Activity activity, final WebviewListener listener) {
        if (activity != null) {
            mActivity = new WeakReference<Activity>(activity);
        }
        else {
            mActivity = null;
        }

        setListener(listener);
    }

    /**
     * Sets listener.
     *
     * @param listener              the listener
     */
    protected void setListener(final WebviewListener listener) {
        mListener = listener;
    }

    @Override
    public void setWebViewClient(final WebViewClient client) {
        mCustomWebViewClient = client;
    }

    /**
     * Initialise webview with settings.
     *
     * @param product
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void initView(Product product) {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setUseWideViewPort(true);
        super.addJavascriptInterface(new JavaScriptInterface(), "Android");
        super.setWebViewClient(new WebviewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!hasError()) {
                    if (mListener != null) {
                        mListener.onPageFinished(url);
                    }
                }
                if (mCustomWebViewClient != null) {
                    mCustomWebViewClient.onPageFinished(view, url);
                }
            }
        });
    }

    /**
     * For load Camera
     *
     * @param activity the context pass from sample app side
     */
    public static void loadCamera(Context activity) {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d("TAG", "onPermissionRequest");
                ((Activity)activity).runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        Log.d("TAG", request.getOrigin().toString());
                        if (request.getOrigin().toString().equals("file:///")
                        ||request.getOrigin().toString().startsWith("https://")) {
                            Log.d("TAG", "GRANTED");
                            request.grant(request.getResources());
                        } else {
                            Log.d("TAG", "DENIED");
                            request.deny();
                        }
                    }
                });
            }

        });
        if (!Permissions.checkPermission(((Activity)activity))) {
            Permissions.requestPermission(((Activity) activity));
        }else {
            Log.i("BuildConfig.WEB_URL",""+BuildConfig.WEB_URL);
            webView.loadUrl(""+BuildConfig.WEB_URL);
        }
    }

    public void startCamera(OnAndroidResult onAndroidResult){
        mOnAndroidResult = onAndroidResult;
        callDesignhubzWebAPI(new Communication(APIHelper.getApiObjectValue(APIObject.VIDEO),0,"startVideo",null));
    }

    public void getProduct(OnAndroidResult onAndroidResult){
        mOnAndroidResult = onAndroidResult;
        Integer[] params = new Integer[1];
        params[0] = 1;
        callDesignhubzWebAPI(new Communication(APIHelper.getApiObjectValue(APIObject.PRODUCT),9999,"getProduct",params));
    }

    /**
     * For load any web URL in webview.
     *
     * @param webURL the web url pass from sample app side
     */
    public static void loadURL(String webURL) {
        webView.loadUrl(webURL);
    }

    /**
     * Change color.
     *
     * @param mColor the HASH color code
     */
    public static void changeColor(String mColor){
        webView.evaluateJavascript("javascript:changeColor("+mColor+");", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String result) {
                Log.i("LOGTAG", "result:-" + result);
            }
        });
    }

    /**
     * Switch camera.
     */
    public static void switchCamera(){
        if(mCameraFacing.equalsIgnoreCase("environment"))
            mCameraFacing="user";
        else
            mCameraFacing="environment";
        webView.evaluateJavascript("javascript:switchCamera("+mCameraFacing+");", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String result) {
                Log.i("LOGTAG", "result:-" + result);
            }
        });
    }

    /**
     * Added by Nicolas/JP
     *
     * @param comObj
     */
    //TODO Refactor
    public void callDesignhubzWebAPI(Communication<T> comObj) {
        webView.evaluateJavascript(comObj.toString(), new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String result) {
                Log.i("LOGTAG", "result:-" + result);
            }
        });
    }
}