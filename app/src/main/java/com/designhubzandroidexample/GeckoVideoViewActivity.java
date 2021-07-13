package com.designhubzandroidexample;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.designhubz.androidsdk.DesignhubzGeckoview;

import com.designhubz.androidsdk.Permissions;
import com.designhubz.androidsdk.api.enums.Eyewear;
import com.designhubz.androidsdk.api.enums.Stat;
import com.designhubz.androidsdk.api.enums.TrackingStatus;
import com.designhubz.androidsdk.helper.Progress;
import com.designhubz.androidsdk.helper.Recommendations;
import com.designhubz.androidsdk.helper.Variation;
import com.designhubz.androidsdk.interfaces.OnEyewearFetchFitInfo;
import com.designhubz.androidsdk.interfaces.OnEyewearRecommendation;
import com.designhubz.androidsdk.interfaces.OnEyewearScreenshotCallback;
import com.designhubz.androidsdk.interfaces.OnEyewearSendStat;
import com.designhubz.androidsdk.interfaces.OnEyewearSwitchCallback;
import com.designhubz.androidsdk.interfaces.OnEyewearVariationCallback;
import com.designhubz.androidsdk.interfaces.OnStartEyewearRequestCallback;

import com.designhubz.androidsdk.interfaces.WebviewListener;
import com.designhubzandroidexample.helper.Constant;
import com.designhubzandroidexample.helper.LogHelper;

import java.util.List;

import static com.designhubz.androidsdk.helper.RequestCodes.REQUEST_CODE_PERMISSION;
/**
 * The type GeckoVideo view activity.
 */
public class GeckoVideoViewActivity extends AppCompatActivity implements WebviewListener {

    private DesignhubzGeckoview designhubzVar;
    private FrameLayout flRoot;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gecko_video_view);

        main();
    }

    private void main() {
        designhubzVar = findViewById(R.id.geckoCamera);

        designhubzVar.initView(this);

        designhubzVar.setListener(this);

        designhubzVar.initializeComponents(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (Permissions.checkPermission(this)) {
                designhubzVar.initializeComponents(this);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(GeckoVideoViewActivity.this);
                builder.setTitle("Permission Denied")
                        .setMessage("Please allow permission to proceed.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onBackPressed();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    /**
     * On close.
     *
     * @param view the view
     */
    public void onClose(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
//        designhubzVar.destroy();
        super.onDestroy();
    }

    /**
     * On page started.
     *
     * @param s      the s
     * @param bitmap the bitmap
     */
    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    /**
     * On page finished.
     *
     * @param s the s
     */
    @Override
    public void onPageFinished(String url) {

    }

    /**
     * On page error.
     *
     * @param i  the
     * @param s  the s
     * @param s1 the s 1
     */
    @Override
    public void onPageError(int i, String s, String s1) {

    }

    /**
     * Start eyewear.
     *
     * @param view the view
     */
    public void StartEyewear(View view) {
        progressDialog.show();
        /**
         * startEyewearTryon
         *
         * Load eyewear widget of given eyewear id
         *
         * @param eyewearID the eyewear id
         * @param onStartEyewearRequestCallback override three callback methods
         *        1. onResult callbacks eyewear variation list
         *        2. onProgressCallback callbacks progress update
         *        3. onTrackingCallback callbacks the status of eyewear tracking like Analyzing,Tracking,FaceNotFound,etc.
         */
        new LogHelper().logText("VideoViewActivity","startEyewearTryon","StartMethodCall");
        designhubzVar.startEyewearTryon(Constant.mProduct.getId(),new OnStartEyewearRequestCallback() {

            @Override
            public void onResult(List<Variation> variations) {
                // write your code to process or show variations
                new LogHelper().logText("VideoViewActivity","startEyewearTryon","onResult--> Variations:-"+variations.size());
                progressDialog.dismiss();
            }

            @Override
            public void onProgressCallback(Progress progress) {
                // write your code to process or show progress
                new LogHelper().logText("VideoViewActivity","startEyewearTryon","onProgressCallback-->:-"+progress.data);
            }

            @Override
            public void onTrackingCallback(TrackingStatus trackingStatus) {
                // write your code to process or show tracking status
                new LogHelper().logText("VideoViewActivity","startEyewearTryon","onTrackingCallback-->:-"+trackingStatus.getValue());
                progressDialog.dismiss();
                Toast.makeText(GeckoVideoViewActivity.this, ""+trackingStatus.getValue(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Load variation.
     *
     * @param view the view
     */
    public void LoadVariation(View view) {
        progressDialog.show();
        /**
         * loadVariation
         *
         * Load eyewear widget variation of passed eyewear variation id
         *
         * @param eyewearID the eyewear id
         * @param OnEyewearVariationCallback override two callback methods
         *        1. onResult callbacks eyewear variation list
         *        2. onProgressCallback callbacks progress update
         */
        new LogHelper().logText("VideoViewActivity","LoadVariation","StartMethodCall");
        designhubzVar.loadVariation("MP000000007163139",new OnEyewearVariationCallback() {
            @Override
            public void onResult(List<Variation> variations) {
                // write your code to process or show variations
                new LogHelper().logText("VideoViewActivity","LoadVariation","onResult--> Variations:-"+variations.size());
                progressDialog.dismiss();
            }

            @Override
            public void onProgressCallback(Progress progress) {
                // write your code to process or show progress
                new LogHelper().logText("VideoViewActivity","LoadVariation","onProgressCallback-->:-"+progress.data);
            }
        });
    }

    /**
     * Switch context.
     *
     * @param view the view
     */
    public void switchContext(View view) {
        progressDialog.show();
        /**
         * switchContext
         *
         * Switch context from 3D to Tryon and Tryon to 3D
         *
         * @param OnEyewearSwitchCallback override two callback methods
         *        1. onResult callbacks string result
         *        2. onProgressCallback callbacks progress update
         */
        new LogHelper().logText("VideoViewActivity","switchContext","StartMethodCall");
        designhubzVar.switchContext(new OnEyewearSwitchCallback() {
            @Override
            public void onResult(String result) {
                // write your code to process or show result
                new LogHelper().logText("VideoViewActivity","switchContext","onResult--> "+result);
                progressDialog.dismiss();
            }

            @Override
            public void onProgressCallback(Progress progress) {
                // write your code to process or show progress
                new LogHelper().logText("VideoViewActivity","switchContext","onProgressCallback-->:-"+progress.data);
            }
        });
    }

    /**
     * Screenshot.
     *
     * @param view the view
     */
    public void screenshot(View view) {
        progressDialog.show();
        /**
         * takeScreenshot
         *
         * Take screenshot of tryon or 3D tryon and returns Bitmap image as result
         *
         * @param OnEyewearScreenshotCallback override one callback methods
         *        1. onResult callbacks Bitmap image of tryon
         */
        new LogHelper().logText("VideoViewActivity","screenshot","StartMethodCall");
        designhubzVar.takeScreenshot(new OnEyewearScreenshotCallback() {
            @Override
            public void onResult(Bitmap bitmap) {
                progressDialog.dismiss();
                // write your code to process or show image
                new LogHelper().logText("VideoViewActivity","screenshot","onResult--> Bitmap");
                showImageInDialog(bitmap);
            }
        });
    }

    private void showImageInDialog(Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GeckoVideoViewActivity.this);
        builder.setTitle("DesignHubzSDK");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.image_preview_dialog, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ImageView ivScreenshotPreview = (ImageView) dialogLayout.findViewById(R.id.ivScreenshotPreview);
        float imageWidthInPX = (float)bitmap.getWidth();

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
//                Math.round(imageWidthInPX * (float)bitmap.getHeight() / (float)bitmap.getWidth()));
//        ivScreenshotPreview.setLayoutParams(layoutParams);
        ivScreenshotPreview.setImageBitmap(bitmap);
        dialog.show();
    }

    /**
     * fetchFit.
     *
     * @param view the view
     */
    public void fetchFit(View view) {
        progressDialog.show();
        /**
         * fetchFitInfo
         *
         * Fetch fit info of eyewear
         *
         * @param OnEyewearFetchFitInfo override One callback methods
         *        1. onResult callbacks receive two result i.e Eyewear Fit and Eyewear Size
         */
        new LogHelper().logText("VideoViewActivity","fetchFit","StartMethodCall");
        designhubzVar.fetchFitInfo(new OnEyewearFetchFitInfo() {
            @Override
            public void onResult(Eyewear.Fit fit, Eyewear.Size size) {
                // write your code to process or show fit info
                new LogHelper().logText("VideoViewActivity","fetchFit","onResult--> Fit:-"+fit.getValue()+"  Size:-"+size.getValue());
                Toast.makeText(GeckoVideoViewActivity.this, "FIT:-"+fit.getValue()+" SIZE:-"+size.getValue(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    /**
     * fetchRecommendation.
     *
     * @param view the view
     */
    public void fetchRecommendation(View view) {
        progressDialog.show();
        /**
         * fetchRecommendations
         *
         * Fetch eyewear Recommendations
         *
         * @param noOfRecomendation the no of recommendations want to get
         * @param OnEyewearRecommendation override One callback methods
         *        1. onResult callbacks recommendation list
         */
        new LogHelper().logText("VideoViewActivity","fetchRecommendation","StartMethodCall");
        designhubzVar.fetchRecommendations(3,new OnEyewearRecommendation() {
            @Override
            public void onResult(List<Recommendations> recommendations) {
                // write your code to process or show recommendations
                new LogHelper().logText("VideoViewActivity","fetchRecommendation","onResult--> Recommendations:-"+recommendations.size());
                progressDialog.dismiss();
            }
        });
    }

    /**
     * sendStat.
     *
     * @param view the view
     */
    public void sendStat(View view) {
        progressDialog.show();
        /**
         * sendStat
         *
         * Send statistics To SDK
         *
         * @param Stat Pass enum of the stats it can be Whishlisted, AddedToCart, SnapshotSaved
         * @param OnEyewearSendStat override One callback methods
         *        1. onResult callbacks string result
         */
        new LogHelper().logText("VideoViewActivity","sendStat","StartMethodCall");
        designhubzVar.sendStat(Stat.Whishlisted,new OnEyewearSendStat() {
            @Override
            public void onResult(String result) {
                // write your code to process or show result
                new LogHelper().logText("VideoViewActivity","sendStat","onResult--> "+result);
                progressDialog.dismiss();
            }
        });
    }
}