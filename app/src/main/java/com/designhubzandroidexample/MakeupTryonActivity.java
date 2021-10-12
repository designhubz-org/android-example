package com.designhubzandroidexample;

import static com.designhubz.androidsdk.helper.RequestCodes.REQUEST_CODE_PERMISSION;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.designhubz.androidsdk.DesignhubzWebview;
import com.designhubz.androidsdk.Permissions;
import com.designhubz.androidsdk.api.Product;
import com.designhubz.androidsdk.api.enums.Stat;
import com.designhubz.androidsdk.api.enums.TrackingStatus;
import com.designhubz.androidsdk.helper.Progress;
import com.designhubz.androidsdk.helper.Recommendations;
import com.designhubz.androidsdk.helper.Variation;
import com.designhubz.androidsdk.interfaces.OnDispose;
import com.designhubz.androidsdk.interfaces.OnRecommendation;
import com.designhubz.androidsdk.interfaces.OnScreenshotCallback;
import com.designhubz.androidsdk.interfaces.OnSendID;
import com.designhubz.androidsdk.interfaces.OnSendStat;
import com.designhubz.androidsdk.interfaces.OnStartMakeupRequestCallback;
import com.designhubz.androidsdk.interfaces.OnVariationCallback;
import com.designhubz.androidsdk.interfaces.WebviewListener;
import com.designhubzandroidexample.helper.LogHelper;

import java.util.List;

public class MakeupTryonActivity extends AppCompatActivity implements WebviewListener {

    private DesignhubzWebview designhubzVar;
    private LinearLayout flRoot;
    private ProgressDialog progressDialog;
    private final String exampleMakeupId = "MP000000008737118";
    private List<Variation> exampleVariations;
    private static int variationNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeup_tryon);

        main();
    }

    private void main() {
        flRoot = findViewById(R.id.flRoot);
        designhubzVar = findViewById(R.id.wvCamera);

        designhubzVar.initView();

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
                StartMakeup(null);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MakeupTryonActivity.this);
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
     * Start makeup.
     *
     * @param view the view
     */
    public void StartMakeup(View view) {
        progressDialog.show();
        /**
         * startMakeupTryon
         *
         * Load makeup widget of given makeup id
         *
         * @param makeupID the makeup id
         * @param onStartEyewearRequestCallback override three callback methods
         *        1. onResult callbacks eyewear variation list
         *        2. onProgressCallback callbacks progress update
         *        3. onTrackingCallback callbacks the status of eyewear tracking like Analyzing,Tracking,FaceNotFound,etc.
         */
        new LogHelper().logText("MakeupTryonActivity", "startMakeupTryon", "StartMethodCall");
        designhubzVar.startMakeuptryon(exampleMakeupId, new OnStartMakeupRequestCallback() {

            @Override
            public void onResult(List<Variation> variations) {
                // write your code to process or show variations
                //Storing the retrieved list of variations
                exampleVariations = variations;
                new LogHelper().logText("MakeupTryonActivity", "startMakeupTryon", "onResult--> Variations:-" + exampleVariations.size());
                progressDialog.dismiss();


                //Send UserID to SDK
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendUserID(null);
                    }
                });

            }

            @Override
            public void onProgressCallback(Progress progress) {
                // write your code to process or show progress
                new LogHelper().logText("MakeupTryonActivity", "startMakeupTryon", "onProgressCallback-->:-" + progress.data);
            }

            @Override
            public void onTrackingCallback(TrackingStatus trackingStatus) {
                // write your code to process or show tracking status
                new LogHelper().logText("MakeupTryonActivity", "startMakeupTryon", "onTrackingCallback-->:-" + trackingStatus.getValue());
                progressDialog.dismiss();
                Toast.makeText(MakeupTryonActivity.this, "" + trackingStatus.getValue(), Toast.LENGTH_SHORT).show();
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
        new LogHelper().logText("MakeupTryonActivity", "LoadVariation", "StartMethodCall");
        
        //Getting variation id from the variations retrieved from StartMakeup one by one
        String variationId = "";
        if (variationNumber < exampleVariations.size()) {
            variationNumber = variationNumber < exampleVariations.size() - 1 ? variationNumber + 1 : 0;
        } else {
            variationNumber = 0;
        }
        variationId = exampleVariations.get(variationNumber).getProductKey();
        
        designhubzVar.loadVariation(variationId, new OnVariationCallback() {
            @Override
            public void onResult(List<Variation> variations) {
                // write your code to process or show variations
                new LogHelper().logText("MakeupTryonActivity", "LoadVariation", "onResult--> Variations:-" + variations.size());
                progressDialog.dismiss();
            }

            @Override
            public void onProgressCallback(Progress progress) {
                // write your code to process or show progress
                new LogHelper().logText("MakeupTryonActivity", "LoadVariation", "onProgressCallback-->:-" + progress.data);
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
        new LogHelper().logText("MakeupTryonActivity", "screenshot", "StartMethodCall");
        designhubzVar.takeScreenshot(new OnScreenshotCallback() {
            @Override
            public void onResult(Bitmap bitmap) {
                progressDialog.dismiss();
                // write your code to process or show image
                new LogHelper().logText("MakeupTryonActivity", "screenshot", "onResult--> Bitmap");
                showImageInDialog(bitmap);
            }
        });
    }

    private void showImageInDialog(Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MakeupTryonActivity.this);
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
        ivScreenshotPreview.setImageBitmap(bitmap);
        dialog.show();
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
        new LogHelper().logText("MakeupTryonActivity", "fetchRecommendation", "StartMethodCall");
        designhubzVar.fetchRecommendations(3, new OnRecommendation() {
            @Override
            public void onResult(List<Recommendations> recommendations) {
                // write your code to process or show recommendations
                new LogHelper().logText("MakeupTryonActivity", "fetchRecommendation", "onResult--> Recommendations:-" + recommendations.size());
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
         * @param Stat Pass enum of the stats it can be Whishlisted, AddedToCart, SnapshotSaved,SharedToSocialMedia
         * @param OnEyewearSendStat override One callback methods
         *        1. onResult callbacks string result
         */
        new LogHelper().logText("MakeupTryonActivity", "sendStat", "StartMethodCall");
        designhubzVar.sendStat(Stat.Whishlisted, new OnSendStat() {
            @Override
            public void onResult(String result) {
                // write your code to process or show result
                new LogHelper().logText("MakeupTryonActivity", "sendStat", "onResult--> " + result);
                progressDialog.dismiss();
            }
        });
    }

    /**
     * sendUserID.
     *
     * @param view the view
     */
    public void sendUserID(View view) {
        progressDialog.show();
        /**
         * sendUserID
         *
         * Send user ID To SDK
         *@param UserID
         * @param OnSendID override One callback methods
         *        1. onResult callbacks string result
         */
        new LogHelper().logText("MakeupTryonActivity", "sendUserID", "StartMethodCall");
        designhubzVar.sendUserID("0001", new OnSendID() {
            @Override
            public void onResult(String result) {
                // write your code to process or show result
//                ((LinearLayout)findViewById(R.id.lyOtherOptions)).setVisibility(View.VISIBLE);
                new LogHelper().logText("MakeupTryonActivity", "sendUserID", "onResult--> " + result);
                progressDialog.dismiss();
            }
        });
    }

    /**
     * DisposeWidget.
     *
     * @param view the view
     */
    public void disposeWidget(View view) {
        progressDialog.show();
        /**
         * DisposeWidget
         *
         * To dispose widget
         *
         * @param OnEyewearDispose override One callback methods
         *        1. onResult callbacks string result
         */
        new LogHelper().logText("MakeupTryonActivity", "disposeWidget", "StartMethodCall");
        designhubzVar.disposeWidget(new OnDispose() {
            @Override
            public void onResult(String result) {
                // write your code to process or show result
                new LogHelper().logText("MakeupTryonActivity", "disposeWidget", "onResult--> " + result);
                progressDialog.dismiss();
            }
        });
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
        designhubzVar.destroy();
        super.onDestroy();
    }

    @Override
    public void onPageStarted(String s, Bitmap bitmap) {

    }

    @Override
    public void onPageFinished(String s) {

    }

    @Override
    public void onPageError(int i, String s, String s1) {

    }
}
