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
import com.designhubz.androidsdk.api.enums.Stat;
import com.designhubz.androidsdk.api.enums.TrackingStatus;
import com.designhubz.androidsdk.helper.Progress;
import com.designhubz.androidsdk.helper.Recommendations;
import com.designhubz.androidsdk.helper.Variation;
import com.designhubz.androidsdk.interfaces.OnDispose;
import com.designhubz.androidsdk.interfaces.OnDoubleScreenshotCallback;
import com.designhubz.androidsdk.interfaces.OnLoadProductCallback;
import com.designhubz.androidsdk.interfaces.OnRecommendation;
import com.designhubz.androidsdk.interfaces.OnScreenshotCallback;
import com.designhubz.androidsdk.interfaces.OnSendID;
import com.designhubz.androidsdk.interfaces.OnSendStat;
import com.designhubz.androidsdk.interfaces.OnStartMakeupRequestCallback;
import com.designhubz.androidsdk.interfaces.OnVariationCallback;
import com.designhubz.androidsdk.interfaces.WebviewListener;
import com.designhubzandroidexample.helper.DebugMessage;
import com.designhubzandroidexample.helper.LogHelper;
import com.designhubzandroidexample.helper.Memory;
import com.designhubzandroidexample.helper.PreferencesManager;

import java.util.Date;
import java.util.List;

public class MakeupTryonActivity extends AppCompatActivity implements WebviewListener {

    private DesignhubzWebview designhubzVar;
    private LinearLayout flRoot;
    private ProgressDialog progressDialog;
    private final String exampleMakeupId = "MP000000008737118";
    private static long startTime = 0;
    private Context context;
    private TextView tvAvailableRAM, tvTestLabel, tvTracking;
    private BroadcastReceiver mReceiverStartTest;
    private IntentFilter mIntentFilterStartTest;
    private LinearLayout llLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeup_tryon);

        main();
    }

    private void main() {
        context = MakeupTryonActivity.this;
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
                Log.e("StartReceiver", "START_METHOD: "+test);

                switch (test) {
                    case "loadProduct":
                        tvTestLabel.setText("Running loadProduct");
                        LoadProduct(null);
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
                    case "takeDoubleScreenshot":
                        tvTestLabel.setText("Running takeDoubleScreenshot");
                        doubleScreenshot(null);
                        break;
                    case "liveCompare":
                        tvTestLabel.setText("Running liveCompare");
                        liveCompare(null);
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
        startTime = new Date().getTime();
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
        designhubzVar.startMakeuptryon("user12345", new OnStartMakeupRequestCallback() {

            @Override
            public void onResult() {
                progressDialog.dismiss();
                DebugMessage.print(context, "Load Time Makeup TryOn: " + ((new Date().getTime() - startTime) / 1000f) + " s");

                //Run all tests if receiver is registered
                Intent broadcast = new Intent();
                broadcast.setAction("START_TEST");
                broadcast.putExtra("test", "loadProduct");
                sendBroadcast(broadcast);
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
                //Toast.makeText(MakeupTryonActivity.this, "" + trackingStatus.getValue(), Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
                Toast.makeText(MakeupTryonActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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
         * @param productId the makeup id
         * @param OnLoadProductCallback override three callback methods
         *        1. onResult callbacks completion
         *        2. onProgressCallback callbacks progress update
         *        3. onErrorCallback callbacks error
         */
        new LogHelper().logText("EyewearTryonActivity", "loadProduct", "StartMethodCall");
        designhubzVar.loadProduct(exampleMakeupId, new OnLoadProductCallback() {

            @Override
            public void onResult(List<Variation> variations) {
                // write your code to process or show variations
                //Storing the retrieved list of variations
                progressDialog.dismiss();
                DebugMessage.print(context, "Load Time: " + ((new Date().getTime() - startTime) / 1000f) + " s");

                Toast.makeText(context,  variations.size() +" variations loaded each with "
                        + variations.get(0).getProperties().size()+ " properties", Toast.LENGTH_LONG).show();

                //Run all tests if receiver is registered
                Intent broadcast = new Intent();
                broadcast.setAction("START_TEST");
                broadcast.putExtra("test", "sendStat");
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
                broadcast.putExtra("test", "sendStat");
                sendBroadcast(broadcast);
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

            @Override
            public void onErrorCallback(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Double Screenshot.
     *
     * @param view the view
     */
    public void doubleScreenshot(View view) {
        progressDialog.show();
        /**
         * takeDoubleScreenshot
         *
         * Take screenshots of Makeup Try-on with and without Makeup
         *
         * @param OnDoubleScreenshotCallback override one callback methods
         *        1. onResult callbacks Bitmap image of with and without the tryon
         */
        new LogHelper().logText("MakeupTryonActivity", "doubleScreenshot", "StartMethodCall");
        designhubzVar.takeDoubleScreenshot(new OnDoubleScreenshotCallback() {
            @Override
            public void onResult(Bitmap originalSnapshot, Bitmap snapshot) {
                progressDialog.dismiss();
                // write your code to process or show image
                new LogHelper().logText("MakeupTryonActivity", "doubleScreenshot", "onResult--> Bitmap");
                showDoubleImageInDialog(originalSnapshot, snapshot);
            }

            @Override
            public void onErrorCallback(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showImageInDialog(Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MakeupTryonActivity.this);
        builder.setTitle("DesignHubzSDK");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Run all tests if receiver is registered
                Intent broadcast = new Intent();
                broadcast.setAction("START_TEST");
                broadcast.putExtra("test", "takeDoubleScreenshot");
                sendBroadcast(broadcast);
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

    private void showDoubleImageInDialog(Bitmap originalSnapshot, Bitmap snapshot) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MakeupTryonActivity.this);
        builder.setTitle("DesignHubzSDK");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Run all tests if receiver is registered
                Intent broadcast = new Intent();
                broadcast.setAction("START_TEST");
                broadcast.putExtra("test", "fetchRecommendations");
                sendBroadcast(broadcast);
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.double_image_preview_dialog, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ImageView ivOriginalPreview = (ImageView) dialogLayout.findViewById(R.id.ivOriginalPreview);
        ivOriginalPreview.setImageBitmap(originalSnapshot);
        ImageView ivMakeupSnapshot = (ImageView) dialogLayout.findViewById(R.id.ivMakeupPreview);
        ivMakeupSnapshot.setImageBitmap(snapshot);
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
         * @param OnRecommendation override One callback methods
         *        1. onResult callbacks recommendation list
         */
        new LogHelper().logText("MakeupTryonActivity", "fetchRecommendation", "StartMethodCall");
        designhubzVar.fetchRecommendations(3, new OnRecommendation() {
            @Override
            public void onResult(List<Recommendations> recommendations) {
                // write your code to process or show recommendations
                new LogHelper().logText("MakeupTryonActivity", "fetchRecommendation",
                        "onResult--> Number of Recommendations:-" + recommendations.size());
                if (recommendations.size() > 0) {
                    displayRecommendations(recommendations);
                }
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
                        //Run all tests if receiver is registered
                        Intent broadcast = new Intent();
                        broadcast.setAction("START_TEST");
                        broadcast.putExtra("test", "liveCompare");
                        sendBroadcast(broadcast);
                        progressDialog.dismiss();
                        dialog.dismiss();
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
        new LogHelper().logText("MakeupTryonActivity", "sendStat", "StartMethodCall");
        designhubzVar.sendStat(Stat.Whishlisted, new OnSendStat() {
            @Override
            public void onResult(String result) {
                // write your code to process or show result
                new LogHelper().logText("MakeupTryonActivity", "sendStat", "onResult--> " + result);
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
     * live compare.
     *
     * @param view the view
     */
    public void liveCompare(View view) {
        /**
         * liveCompare
         *
         * Start live compare for make-up try on
         */
        new LogHelper().logText("MakeupTryonActivity", "liveCompare", "StartMethodCall");
        designhubzVar.liveCompare(0.5);
        try {
            unregisterReceiver(mReceiverStartTest);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
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

            @Override
            public void onErrorCallback(String errorMessage) {
                progressDialog.dismiss();
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
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
        try {
            unregisterReceiver(mReceiverStartTest);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
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

    /**
     * TestAll.
     *
     * @param view the view
     */
    public void TestAll(View view) {
        llLabel.setVisibility(View.VISIBLE);
        registerReceiver(mReceiverStartTest, mIntentFilterStartTest);
        tvTestLabel.setText("Running startMakeupTryon");
        StartMakeup(view);
    }
}
