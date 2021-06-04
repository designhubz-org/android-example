package com.designhubzandroidexample;

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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.designhubz.androidsdk.DesignhubzWebview;
import com.designhubz.androidsdk.Permissions;
import com.designhubz.androidsdk.api.enums.Eyewear;
import com.designhubz.androidsdk.helper.Progress;
import com.designhubz.androidsdk.helper.RequestResponseTryon;
import com.designhubz.androidsdk.helper.Variation;
import com.designhubz.androidsdk.interfaces.OnEyewearSwitchCallback;
import com.designhubz.androidsdk.interfaces.OnStartEyewearRequestCallback;
import com.designhubz.androidsdk.interfaces.OnEyewearScreenshotCallback;
import com.designhubz.androidsdk.interfaces.OnEyewearVariationCallback;
import com.designhubz.androidsdk.interfaces.WebviewListener;
import com.designhubzandroidexample.helper.Constant;

import java.util.List;

import static com.designhubz.androidsdk.helper.RequestCodes.REQUEST_CODE_PERMISSION;

/**
 * The type Video view activity.
 */
public class VideoViewActivity extends AppCompatActivity implements WebviewListener {

    private DesignhubzWebview designhubzVar;
    private FrameLayout flRoot;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        main();
    }

    private void main() {
        flRoot = findViewById(R.id.flRoot);
        designhubzVar = findViewById(R.id.wvCamera);

        designhubzVar.initView();

        designhubzVar.setListener(this);

        designhubzVar.initializeComponents(this);

        designhubzVar.setEyewearSize(Eyewear.Size.Small);
        designhubzVar.setEyewearFit(Eyewear.Fit.JustRight);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoViewActivity.this);
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
        progressDialog.show();
        //Pass Eyewear ID and Start Eyewear Callback to the SDK
        designhubzVar.startEyewearTryon(Constant.mProduct.getId(),new OnStartEyewearRequestCallback() {

            @Override
            public void onResult(List<Variation> variations) {
                progressDialog.dismiss();
            }

            @Override
            public void onProgressCallback(Progress progress) {
            }

            @Override
            public void onTrackingCallback(String message) {
                progressDialog.dismiss();
                Toast.makeText(VideoViewActivity.this, ""+message, Toast.LENGTH_SHORT).show();
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
        //Pass Load Variation Callback
        designhubzVar.loadVariation("MP000000007163139",new OnEyewearVariationCallback() {
            @Override
            public void onResult(List<Variation> variations) {
                progressDialog.dismiss();
            }

            @Override
            public void onProgressCallback(Progress progress) {
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
        //Pass Switch Context Callback
        designhubzVar.switchContext(new OnEyewearSwitchCallback() {
            @Override
            public void onResult(String result) {
                progressDialog.dismiss();
            }

            @Override
            public void onProgressCallback(Progress progress) {}
        });
    }

    /**
     * Screenshot.
     *
     * @param view the view
     */
    public void screenshot(View view) {
        progressDialog.show();
        //Pass Screenshot Callback
        designhubzVar.takeScreenshot(new OnEyewearScreenshotCallback() {
            @Override
            public void onResult(Bitmap bitmap) {
                progressDialog.dismiss();
                showImageInDialog(bitmap);
            }
        });
    }

    private void showImageInDialog(Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoViewActivity.this);
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

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                Math.round(imageWidthInPX * (float)bitmap.getHeight() / (float)bitmap.getWidth()));
        ivScreenshotPreview.setLayoutParams(layoutParams);
        ivScreenshotPreview.setImageBitmap(bitmap);
        dialog.show();
    }
}