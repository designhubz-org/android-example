package com.designhubz.androidsdk;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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
import com.designhubz.androidsdk.helper.LockUI;
import com.designhubz.androidsdk.helper.JSONHelper;
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
    public static String mCameraFacing = "user";
    private Context mContext;
    private ProgressDialog pDialog;

    /**
     * Instantiates a new webview.
     *
     * @param context the context
     */
    public DesignhubzWebview(Context context) {
        super(context);
        mContext = context;
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
        mContext = context;
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
        } else {
            mActivity = null;
        }

        setListener(listener);
    }

    /**
     * Sets listener.
     *
     * @param listener the listener
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
        super.setWebViewClient(new WebviewClient() {
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
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }

    /**
     * For initialize Components
     *
     * @param activity the context pass from sample app side
     */
    public static void initializeComponents(Context activity) {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d("TAG", "onPermissionRequest");
                ((Activity) activity).runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        Log.d("TAG", request.getOrigin().toString());
                        if (request.getOrigin().toString().equals("file:///")
                                || request.getOrigin().toString().startsWith("https://")) {
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
        if (!Permissions.checkPermission(((Activity) activity))) {
            Permissions.requestPermission(((Activity) activity));
        } else {
            Log.i("BuildConfig.WEB_URL", "" + BuildConfig.WEB_URL);
            webView.loadUrl("" + BuildConfig.WEB_URL);
        }
    }

    String Res = "";

    public synchronized String startCamera() {
        Res = "";
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                pDialog.show();
            }
        });
        LockUI lockUI = new LockUI();

        OnAndroidResult mOnAndroidResult = action -> {
            Log.e("RES", "" + action);
            pDialog.dismiss();
            Res = action;
            lockUI.Release();
        };

        callDesignhubzWebAPI(new Communication<>(APIHelper.getApiObjectValue(APIObject.VIDEO),"startVideo", null), mOnAndroidResult);
        lockUI.Lock();
        Log.e("RES", "AFTER" + Res);
        return Res;
    }

    String prodResponse = "";

    public synchronized Product getProduct() {
        prodResponse = "";
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                pDialog.show();
            }
        });

        LockUI lockUI = new LockUI();

        Integer[] params = new Integer[1];
        params[0] = 1;

        OnAndroidResult mOnAndroidResult = action -> {
            Log.e("RES", "" + action);
            prodResponse = action;
            lockUI.Release();
            pDialog.dismiss();
        };

        callDesignhubzWebAPI(new Communication(APIHelper.getApiObjectValue(APIObject.PRODUCT),"getProduct", params), mOnAndroidResult);

        lockUI.Lock();
        Log.e("RES", "AFTER" + prodResponse);

        return (Product) new JSONHelper().convertJsontoObject(prodResponse, Product.class);
    }

    /**
     * Added by Nicolas/JP
     *
     * @param comObj
     * @param onAndroidResult
     */
    //TODO Refactor
    public void callDesignhubzWebAPI(Communication<T> comObj, OnAndroidResult onAndroidResult) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript(comObj.createMsg((T) onAndroidResult), new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String result) {
                        Log.i("LOGTAG", "result:-" + result);
                    }
                });
            }
        });
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
    public static void changeColor(String mColor) {
        webView.evaluateJavascript("javascript:changeColor(" + mColor + ");", result -> Log.i("LOGTAG", "result:-" + result));
    }

    /**
     * Switch camera.
     */
    public static void switchCamera() {
        if (mCameraFacing.equalsIgnoreCase("environment"))
            mCameraFacing = "user";
        else
            mCameraFacing = "environment";
        webView.evaluateJavascript("javascript:switchCamera(" + mCameraFacing + ");", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String result) {
                Log.i("LOGTAG", "result:-" + result);
            }
        });
    }

}