package com.designhubzandroidexample;

import static com.designhubz.androidsdk.helper.RequestCodes.REQUEST_CODE_PERMISSION;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.designhubz.androidsdk.DesignhubzWebview;
import com.designhubz.androidsdk.Permissions;
import com.designhubz.androidsdk.api.Product;
import com.designhubz.androidsdk.api.enums.Eyewear;
import com.designhubz.androidsdk.api.enums.Stat;
import com.designhubz.androidsdk.api.enums.TrackingStatus;
import com.designhubz.androidsdk.helper.Progress;
import com.designhubz.androidsdk.helper.Recommendations;
import com.designhubz.androidsdk.helper.Variation;
import com.designhubz.androidsdk.interfaces.OnDispose;
import com.designhubz.androidsdk.interfaces.OnEyewearFetchFitInfo;
import com.designhubz.androidsdk.interfaces.OnEyewearSwitchCallback;
import com.designhubz.androidsdk.interfaces.OnLoadProductCallback;
import com.designhubz.androidsdk.interfaces.OnRecommendation;
import com.designhubz.androidsdk.interfaces.OnScreenshotCallback;
import com.designhubz.androidsdk.interfaces.OnSendStat;
import com.designhubz.androidsdk.interfaces.OnStartEyewearRequestCallback;
import com.designhubz.androidsdk.interfaces.WebviewListener;
import com.designhubzandroidexample.helper.DebugMessage;
import com.designhubzandroidexample.helper.LogHelper;
import com.designhubzandroidexample.helper.Memory;
import com.designhubzandroidexample.helper.PreferencesManager;

import java.util.Date;
import java.util.List;

/**
 * The type Eyewear Tryon activity.
 */
public class EyewearTryonActivity extends AppCompatActivity implements WebviewListener {

    private DesignhubzWebview designhubzVar;
    private LinearLayout flRoot;
    private ProgressDialog progressDialog;
    private Product mProduct = new Product("MP000000006870126", "Fastrack",
            "Fastrack P254678D Green Anti-Reflactive Sunglasses", 2300, 3000);
    private static long startTime = 0;
    private static boolean switchContextClicked = false;
    private Context context;
    private TextView tvAvailableRAM, tvTestLabel, tvTracking;
    private BroadcastReceiver mReceiverStartTest;
    private IntentFilter mIntentFilterStartTest;
    private LinearLayout llLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eyewear_tryon);

        main();
    }

    private void main() {
        context = EyewearTryonActivity.this;
        flRoot = findViewById(R.id.flRoot);
        designhubzVar = findViewById(R.id.wvCamera);

        designhubzVar.initView();

        designhubzVar.setListener(this);

        designhubzVar.initializeComponents(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        tvAvailableRAM = findViewById(R.id.tvAvailableRAM);
        tvTestLabel = findViewById(R.id.tvTestLabel);
        tvTracking = findViewById(R.id.tvTracking);

        llLabel = findViewById(R.id.llLabel);

        //Debug available Memory
        if (new PreferencesManager(context).getEnableDebug()) {
            tvAvailableRAM.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            final int delay = 1000; // 1000 milliseconds == 1 second

            handler.postDelayed(new Runnable() {
                public void run() {
                    tvAvailableRAM.setText("Available RAM: " + Memory.getAvailableRAM(context) + " MB");
                    handler.postDelayed(this, delay);
                }
            }, delay);
        }

        //BroadcastReceiver to start test
        mReceiverStartTest = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String test = intent.getStringExtra("test");
                Log.e("StartReceiver", "START_METHOD: " + test);

                switch (test) {
                    case "loadProduct":
                        tvTestLabel.setText("Running loadProduct");
                        LoadProduct(null);
                        break;
                    case "switchContext":
                        tvTestLabel.setText("Running switchContext");
                        switchContext(null);
                        break;
                    case "fetchFit":
                        tvTestLabel.setText("Running fetchFit");
                        fetchFit(null);
                        break;
                    case "fetchRecommendations":
                        tvTestLabel.setText("Running fetchRecommendations");
                        fetchRecommendation(null);
                        break;
                    case "sendStat":
                        tvTestLabel.setText("Running sendStat");
                        sendStat(null);
                        break;
                    case "takeScreenshot":
                        tvTestLabel.setText("Running takeScreenshot");
                        screenshot(null);
                        break;
                }
            }
        };

        mIntentFilterStartTest = new IntentFilter("START_TEST");

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (Permissions.checkPermission(this)) {
                switchContext(null);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(EyewearTryonActivity.this);
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
        designhubzVar.destroy();
        try {
            unregisterReceiver(mReceiverStartTest);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * On page started.
     *
     * @param s      the s
     * @param bitmap the bitmap
     */
    @Override
    public void onPageStarted(String s, Bitmap bitmap) {

    }

    /**
     * On page finished.
     *
     * @param s the s
     */
    @Override
    public void onPageFinished(String s) {

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
        startTime = new Date().getTime();
        progressDialog.show();
        /**
         * startEyewearTryon
         *
         * Load eyewear widget of given eyewear id
         *
         * @param eyewearID the eyewear id
         * @param onStartEyewearRequestCallback override three callback methods
         *        1. onResult callbacks completion
         *        2. onProgressCallback callbacks progress update
         */
        new LogHelper().logText("EyewearTryonActivity", "startEyewearTryon", "StartMethodCall");
        designhubzVar.startEyewearTryon("user12345", new OnStartEyewearRequestCallback() {

            @Override
            public void onResult() {
                progressDialog.dismiss();
                DebugMessage.print(context, "Load Time Eyewear 3D: " + ((new Date().getTime() - startTime) / 1000f) + " s");

                //Run all tests if receiver is registered
                Intent broadcast = new Intent();
                broadcast.setAction("START_TEST");
                broadcast.putExtra("test", "loadProduct");
                sendBroadcast(broadcast);

            }

            @Override
            public void onProgressCallback(Progress progress) {
                // write your code to process or show progress
                new LogHelper().logText("EyewearTryonActivity", "startEyewearTryon", "onProgressCallback-->:-" + progress.data);
            }

            @Override
            public void onTrackingCallback(TrackingStatus trackingStatus) {
                new LogHelper().logText("EyewearTryonActivity", "startEyewearTryon", "onTrackingCallback-->:-" + trackingStatus.getValue());
                progressDialog.dismiss();
                //Toast.makeText(context, trackingStatus.getValue(), Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTracking.setVisibility(View.VISIBLE);
                        tvTracking.setText("Tracking Status: " + trackingStatus.getValue());
                    }
                });

            }

            @Override
            public void onErrorCallback(String errorMessage) {
                new LogHelper().logText("EyewearTryonActivity", "startEyewearTryon", "onErrorCallback-->:-" + errorMessage);
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Load Product.
     *
     * @param view the view
     */
    public void LoadProduct(View view) {
        startTime = new Date().getTime();
        progressDialog.show();
        /**
         * loadProduct
         *
         * Load product with productId
         *
         * @param productId the eyewear id
         * @param OnLoadProductCallback override three callback methods
         *        1. onResult callbacks completion
         *        2. onProgressCallback callbacks progress update
         *        3. onErrorCallback callbacks error
         */
        new LogHelper().logText("EyewearTryonActivity", "loadProduct", "StartMethodCall");
        designhubzVar.loadProduct(mProduct.getId(), new OnLoadProductCallback() {

            @Override
            public void onResult(List<Variation> variations) {
                // write your code to process or show variations
                //Storing the retrieved list of variations
                progressDialog.dismiss();
                DebugMessage.print(context, "Load Time: " + ((new Date().getTime() - startTime) / 1000f) + " s");

                Toast.makeText(context, variations.size() + " variations loaded each with "
                        + variations.get(0).getProperties().size() + " properties", Toast.LENGTH_LONG).show();

                //Run all tests if receiver is registered
                Intent broadcast = new Intent();
                broadcast.setAction("START_TEST");
                broadcast.putExtra("test", "switchContext");
                sendBroadcast(broadcast);

            }

            @Override
            public void onProgressCallback(Progress progress) {
                // write your code to process or show progress
                new LogHelper().logText("EyewearTryonActivity", "loadProduct", "onProgressCallback-->:-" + progress.data);
            }

            @Override
            public void onErrorCallback(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                //Run all tests if receiver is registered
                Intent broadcast = new Intent();
                broadcast.setAction("START_TEST");
                broadcast.putExtra("test", "switchContext");
                sendBroadcast(broadcast);
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
        startTime = new Date().getTime();
        switchContextClicked = true;
        /**
         * switchContext
         *
         * Switch context from 3D to Tryon and Tryon to 3D
         *
         * @param OnEyewearSwitchCallback override two callback methods
         *        1. onResult callbacks string result
         *        2. onProgressCallback callbacks progress update
         */
        new LogHelper().logText("EyewearTryonActivity", "switchContext", "StartMethodCall");
        designhubzVar.switchContext(new OnEyewearSwitchCallback() {

            @Override
            public void onResult(String data) {
                new LogHelper().logText("EyewearTryonActivity", "switchContext", "switchContext-->:-" + data);
                progressDialog.dismiss();

                //Run all tests if receiver is registered
                Intent broadcast = new Intent();
                broadcast.setAction("START_TEST");
                broadcast.putExtra("test", "fetchFit");
                sendBroadcast(broadcast);
            }

            @Override
            public void onProgressCallback(Progress progress) {
                // write your code to process or show progress
                new LogHelper().logText("EyewearTryonActivity", "switchContext", "onProgressCallback-->:-" + progress.data);
            }

            @Override
            public void onErrorCallback(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
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
        new LogHelper().logText("EyewearTryonActivity", "screenshot", "StartMethodCall");
        designhubzVar.takeScreenshot(new OnScreenshotCallback() {
            @Override
            public void onResult(Bitmap bitmap) {
                progressDialog.dismiss();
                // write your code to process or show image
                new LogHelper().logText("EyewearTryonActivity", "screenshot", "onResult--> Bitmap");
                showImageInDialog(bitmap);
            }

            @Override
            public void onErrorCallback(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showImageInDialog(Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EyewearTryonActivity.this);
        builder.setTitle("DesignHubzSDK");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llLabel.setVisibility(View.GONE);
                        try {
                            unregisterReceiver(mReceiverStartTest);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.image_preview_dialog, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ImageView ivScreenshotPreview = (ImageView) dialogLayout.findViewById(R.id.ivScreenshotPreview);
        float imageWidthInPX = (float) bitmap.getWidth();

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
        new LogHelper().logText("EyewearTryonActivity", "fetchFit", "StartMethodCall");
        designhubzVar.fetchFitInfo(new OnEyewearFetchFitInfo() {
            @Override
            public void onResult(Eyewear.Fit fit, Eyewear.Size size) {
                // write your code to process or show fit info
                new LogHelper().logText("EyewearTryonActivity", "fetchFit", "onResult--> Fit:-" + fit.getValue() + "  Size:-" + size.getValue());
                Toast.makeText(EyewearTryonActivity.this, "FIT:-" + fit.getValue() + " SIZE:-" + size.getValue(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                //Run all tests if receiver is registered
                Intent broadcast = new Intent();
                broadcast.setAction("START_TEST");
                broadcast.putExtra("test", "fetchRecommendations");
                sendBroadcast(broadcast);
            }

            @Override
            public void onErrorCallback(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
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
        new LogHelper().logText("EyewearTryonActivity", "fetchRecommendation", "StartMethodCall");
        designhubzVar.fetchRecommendations(3, new OnRecommendation() {
            @Override
            public void onResult(List<Recommendations> recommendations) {
                // write your code to process or show recommendations
                new LogHelper().logText("EyewearTryonActivity", "fetchRecommendation",
                        "onResult--> Number of Recommendations:-" + recommendations.size());
                if (recommendations.size() > 0) {
                    displayRecommendations(recommendations);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onErrorCallback(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * displayRecommendations
     *
     * @param recommendations list of recommendations
     */
    private void displayRecommendations(List<Recommendations> recommendations) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Product Key => Score");
        strBuilder.append("\n");
        strBuilder.append("\n");
        for (int i = 0; i < recommendations.size(); i++) {
            strBuilder.append(recommendations.get(i).getProductKey());
            strBuilder.append(" => ");
            strBuilder.append(recommendations.get(i).getScore());
            strBuilder.append("\n");
            strBuilder.append("\n");
        }
        new AlertDialog.Builder(context)
                .setTitle("Recommendations")
                .setMessage(strBuilder.toString())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Run all tests if receiver is registered
                        Intent broadcast = new Intent();
                        broadcast.setAction("START_TEST");
                        broadcast.putExtra("test", "sendStat");
                        sendBroadcast(broadcast);
                    }
                })
                .show();
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
        new LogHelper().logText("EyewearTryonActivity", "sendStat", "StartMethodCall");
        designhubzVar.sendStat(Stat.Whishlisted, new OnSendStat() {
            @Override
            public void onResult(String result) {
                // write your code to process or show result
                new LogHelper().logText("EyewearTryonActivity", "sendStat", "onResult--> " + result);
                progressDialog.dismiss();

                //Run all tests if receiver is registered
                Intent broadcast = new Intent();
                broadcast.setAction("START_TEST");
                broadcast.putExtra("test", "takeScreenshot");
                sendBroadcast(broadcast);
            }

            @Override
            public void onErrorCallback(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
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
        new LogHelper().logText("EyewearTryonActivity", "disposeWidget", "StartMethodCall");
        designhubzVar.disposeWidget(new OnDispose() {
            @Override
            public void onResult(String result) {
                // write your code to process or show result
                new LogHelper().logText("EyewearTryonActivity", "disposeWidget", "onResult--> " + result);
                progressDialog.dismiss();
            }

            @Override
            public void onErrorCallback(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * TestAll.
     *
     * @param view the view
     */
    public void TestAll(View view) {
        llLabel.setVisibility(View.VISIBLE);
        registerReceiver(mReceiverStartTest, mIntentFilterStartTest);
        tvTestLabel.setText("Running startEyewearTryon");
        StartEyewear(view);
    }
}