## designhubz Android Example
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=7)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)](http://opensource.org/licenses/MIT)

Designhubz Android Example is written in Java for interact with the 3D/AR widgets via Designhubz Android library.

## Prerequisites

- Android Studio
- Android SDK

## Requirements

- Android 4.4+

## Features

- Get access to products and variations details
- Seamlessly switch between variations in real-time
- Get access to recommendations
- Check information about product fit (if applicable. Example: Eyewear is too large)

## Installation

- Create a new project in Android Studio as you would normally for e.g. SampleApp.
- Add maven repository to project build.gradle.
```gradle
 allprojects {
    repositories {
        /**
         * Step 1 : Add jitpack maven repository
         */
        maven { url "https://jitpack.io" }
    }
 }
```
- In your app’s build.gradle add this dependency:

```gradle
dependencies{
    /**
     * Step 2 : Add this android-sdk dependency
     */
    implementation 'com.github.designhubz-org:android-sdk:1.1'
}
```

## Usage

- ### Layout (XML)

```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    // Add DesignHubzWebview here

    <com.designhubz.androidsdk.DesignhubzWebview
        android:id="@+id/wvCamera"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

- ### Activity (Java)

```java
package com.designhubzandroidsdk;

import com.designhubz.androidsdk.DesignhubzWebview;
import com.designhubz.androidsdk.Permissions;
import com.designhubz.androidsdk.interfaces.WebChromeClientListner;

/**
* implements WebChromeClientListner for WebchromeClient
*/
public class MainActivity extends AppCompatActivity implements WebviewListener{

     /**
     * The SDK camera view.
     */
    DesignhubzWebview designhubzVar;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        designhubzVar = findViewById(R.id.wvCamera);

        designhubzVar.initView();

        //Register webview client listener here
        designhubzVar.setListener(this);

        designhubzVar.initializeComponents(this);
    }

    /**
     * Request permission listner for watch is permission allow by user or not.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) 
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 50) {
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
        
}
```

- For Initialize Components:

```java
DesignhubzWebview.initializeComponents(this);
```


- For Start the eyewear try-on widget:

```java
progressDialog.show();
designhubzVar.startEyewearTryon(new OnEyewearRequestCallback() {
      @Override
      public void onResult(Object action) {
          progressDialog.dismiss();
      }

      @Override
      public void onProgressCallback(String action) {
      }

      @Override
      public void onTrackingCallback(String action) {
          progressDialog.dismiss();
          Toast.makeText(VideoViewActivity.this, ""+action, Toast.LENGTH_SHORT).show();
      }
});
```

- For load another variation:

```java
 progressDialog.show();
 designhubzVar.loadVariation(new OnEyewearRequestCallback() {
      @Override
      public void onResult(Object action) {
          progressDialog.dismiss();
      }

      @Override
      public void onProgressCallback(String action) {
      }

      @Override
      public void onTrackingCallback(String action) {

      }
});
```
- For switch context (3D/Tryon):

```java
 progressDialog.show();
 designhubzVar.switchContext(new OnEyewearRequestCallback() {
      @Override
      public void onResult(Object action) {
          progressDialog.dismiss();
      }

      @Override
      public void onProgressCallback(String action) {
      }

      @Override
      public void onTrackingCallback(String action) {
         progressDialog.setMessage("" + action);
      }
});
```
- To take screenshot (returns bitmap image):

```java
 progressDialog.show();
 designhubzVar.takeScreenshot(new OnEyewearRequestCallback() {
      @Override
      public void onResult(Object action) {
                progressDialog.dismiss();
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
                Bitmap bitmap = (Bitmap) action;
                float imageWidthInPX = (float)bitmap.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)bitmap.getHeight() / (float)bitmap.getWidth()));
                ivScreenshotPreview.setLayoutParams(layoutParams);
                ivScreenshotPreview.setImageBitmap(bitmap);
                dialog.show();
      }

      @Override
      public void onProgressCallback(String action) {
      }

      @Override
      public void onTrackingCallback(String action) {
         progressDialog.setMessage("" + action);
      }
});

```
- Screenshot dialog design (image_preview_dialog.xml):

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content">

    <ImageView
        android:id="@+id/ivScreenshotPreview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

</LinearLayout>
```

## Contributing

All contributions are welcome! If you wish to contribute, please create an issue first so that your feature, problem or question can be discussed.

## License

This project is licensed under the terms of the [MIT License](https://opensource.org/licenses/MIT).
