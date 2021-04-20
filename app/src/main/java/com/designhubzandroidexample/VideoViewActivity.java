package com.designhubzandroidexample;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.designhubz.androidsdk.DesignhubzWebview;
import com.designhubz.androidsdk.Permissions;
import com.designhubz.androidsdk.helper.JSONHelper;
import com.designhubz.androidsdk.helper.Product;
import com.designhubz.androidsdk.interfaces.WebviewListener;
import com.designhubzandroidexample.adapter.VideoviewProductListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.designhubz.androidsdk.helper.RequestCodes.REQUEST_CODE_PERMISSION;
import static com.designhubzandroidexample.helper.Constant.mProduct;

/**
 * The type Video view activity.
 */
public class VideoViewActivity extends AppCompatActivity implements WebviewListener {

    private DesignhubzWebview designhubzVar;
    private TextView tvDesc, tvBlack, tvRed, tvBlue;
    private RecyclerView rcvProduct;
    private List<Product> videoViewProductList = new ArrayList<>();
    private VideoviewProductListAdapter productListAdapter;
    private FrameLayout flRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        main();
    }

    private void main() {
        flRoot = findViewById(R.id.flRoot);
        designhubzVar = findViewById(R.id.wvCamera);
        rcvProduct = findViewById(R.id.rcvProduct);
        tvDesc = findViewById(R.id.tvDesc);
        tvBlack = findViewById(R.id.tvBlack);
        tvRed = findViewById(R.id.tvRed);
        tvBlue = findViewById(R.id.tvBlue);

        designhubzVar.initView(mProduct);

        designhubzVar.setListener(this, this);

        designhubzVar.initializeComponents(this);

        rcvProduct.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rcvProduct);

        getProducts();
    }

    private void getProducts() {
        videoViewProductList.add(new Product(1.365, "Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 2300, 3000));
        videoViewProductList.add(new Product(1.365, "Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 5300, 7400));
        videoViewProductList.add(new Product(1.365, "Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 2500, 3050));
        videoViewProductList.add(new Product(1.365, "Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 4300, 5000));
        videoViewProductList.add(new Product(1.365, "Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 2400, 3200));

        productListAdapter = new VideoviewProductListAdapter(this, videoViewProductList) {
            @Override
            public void onClickItem(int adapterPosition) {

            }
        };
        rcvProduct.setAdapter(productListAdapter);

    }

    /**
     * Request permission listner for watch is permission allow by user or not.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (Permissions.checkPermission(this)) {
                DesignhubzWebview.initializeComponents(this);
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
     * On black click.
     *
     * @param view the view
     */
    public void onBlackClick(View view) {
        String colorCode = "#" + Integer.toHexString(ContextCompat.getColor(this, R.color.item_black));
        DesignhubzWebview.changeColor(colorCode);
        tvBlack.setVisibility(View.VISIBLE);
        tvRed.setVisibility(View.INVISIBLE);
        tvBlue.setVisibility(View.INVISIBLE);
    }

    /**
     * On red click.
     *
     * @param view the view
     */
    public void onRedClick(View view) {
        String colorCode = "#" + Integer.toHexString(ContextCompat.getColor(this, R.color.item_red));
        DesignhubzWebview.changeColor(colorCode);
        tvBlack.setVisibility(View.INVISIBLE);
        tvRed.setVisibility(View.VISIBLE);
        tvBlue.setVisibility(View.INVISIBLE);
    }

    /**
     * On blue click.
     *
     * @param view the view
     */
    public void onBlueClick(View view) {
        String colorCode = "#" + Integer.toHexString(ContextCompat.getColor(this, R.color.item_blue));
        DesignhubzWebview.changeColor(colorCode);
        tvBlack.setVisibility(View.INVISIBLE);
        tvRed.setVisibility(View.INVISIBLE);
        tvBlue.setVisibility(View.VISIBLE);
    }

    /**
     * On switch camera.
     *
     * @param view the view
     */
    public void onSwitchCamera(View view) {
        DesignhubzWebview.switchCamera();
    }

    /**
     * On close.
     *
     * @param view the view
     */
    public void onClose(View view) {
        onBackPressed();
    }

    /**
     * On filter.
     *
     * @param view the view
     */
    public void onFilter(View view) {
    }

    /**
     * On cart.
     *
     * @param view the view
     */
    public void onCart(View view) {
    }

    /**
     * Release camera view on activity close.
     */
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

//    @Override
//    public void onReceiveResult(String result) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(VideoViewActivity.this);
//        builder.setTitle("DesignHubzSDK")
//                .setMessage(""+result)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

    @Override
    public void initializeCamera() {

    }

    @Override
    public void detectingFace() {

    }

    @Override
    public void initializingFacePoints() {

    }

    @Override
    public void initializingProductPoints() {

    }

    @Override
    public void preparingFinalResult() {

    }

    /**
     * Start camera.
     *
     * @param view the view
     */
    String result = "";
    public void StartCamera(View view) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            /*
             * Your task will be executed here
             * its like doInBackground()
             * */
            result = designhubzVar.startCamera();
            handler.post(() -> {
                /*
                 * its like onPostExecute()
                 * */
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoViewActivity.this);
                builder.setTitle("DesignHubzSDK")
                        .setMessage("StartCamera>>>>" + result)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            });
        });
    }

    /**
     * Get product.
     *
     * @param view the view
     */
    public void GetProduct(View view) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            /*
             * Your task will be executed here
             * its like doInBackground()
             * */
            Product product = designhubzVar.getProduct();
            handler.post(() -> {
                /*
                 * its like onPostExecute()
                 * */
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoViewActivity.this);
                builder.setTitle("DesignHubzSDK")
                        .setMessage("GetProduct>>>>>" + new JSONHelper().convertObjecttoJson(product))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            });
        });
    }
}