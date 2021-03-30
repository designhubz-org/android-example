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

        //Register webview client listener here
        designhubzVar.setListener(this,this);

        DesignhubzWebview.loadCamera(this);
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
                DesignhubzWebview.loadCamera(this);
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

    // Handle result return from camera actions
    @Override
    public void onReceiveResult(String action) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoViewActivity.this);
        builder.setTitle("DesignhubzSDK")
                .setMessage(""+result)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show(); 
    }
    
    // Notify the host application that camera is initializing
    @Override
    public void initializeCamera() {
        Snackbar.make(flRoot, "Camera initializing", Snackbar.LENGTH_LONG).show();
    }

    // Notify the host application that camera is detecting face
    @Override
    public void detectingFace() {
        Snackbar.make(flRoot, "Detecting your face", Snackbar.LENGTH_LONG).show();
    }

    // Notify the host application that initializing face points
    @Override
    public void intializingFacePoints() {
        Snackbar.make(flRoot, "Initializing face points", Snackbar.LENGTH_LONG).show();
    }

    // Notify the host application that initializing product points
    @Override
    public void initiazingProductPoints() {
        Snackbar.make(flRoot, "Initializing product points", Snackbar.LENGTH_LONG).show();
    }

    // Notify the host application that result is ready
    @Override
    public void preparingFinalResult() {
        Snackbar.make(flRoot, "Preparing final result", Snackbar.LENGTH_LONG).show();
    }

    // Notify the host application that a page has started loading.
    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    // Notify the host application that a page has finished loading.
    @Override
    public void onPageFinished(String url) {
    }

    // Report web resource loading error to the host application.
    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }
        
}
```

- For webview camera:

```java
DesignhubzWebview.loadCamera(this);
```


- For start camera:

```java
designhubzVar.startCamera();
```

- For get product:

```java
designhubzVar.getProduct();
```

## Contributing

All contributions are welcome! If you wish to contribute, please create an issue first so that your feature, problem or question can be discussed.

## License

This project is licensed under the terms of the [MIT License](https://opensource.org/licenses/MIT).
