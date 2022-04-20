At **Designhubz** we empower brands around the world to connect with shoppers in a complete immersive way.

We’re transforming the online shopping experience with next generation eCommerce interfaces using our leading AR technology that’s being adopted by some of the largest brands and retailers globally.

---

## designhubz Android Example
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=7)

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
- Take before/after screenshots
- Live compare with/without make up look

## Changelog

### Latest: com.github.designhubz-org:android-sdk:3.0.2

### 3.0.2

- Improves logging and debugging
- Requesting Garbage Collection before widgets are loaded.

### 3.0.1 hotfix
*(backward compatible with no required upgrades)*

- `onErrorCallback(String errorMessage)` of `startMakeupTryon` will throw:
    - `errorMessage = "0"` for lack of webgl support or outdated opengl drivers.
    - `errorMessage = "1"` when not enough available RAM (~600MB).
    - Otherwise unchanged: The error callback `errorMessage = "any other error"` should still be handled.

### 3.0.1

- Changed method signature of onResult() inside loadProduct callback. It will now have one parameter, list of variations.

### 3.0

- The entry methods `startEyewearTryon(...)` and `startMakeupTryon(...)` now take an `userId` instead of `productId`
- Added call to `loadProduct` method to load eyewear and makeup products
- Added `onErrorCallback` for all methods.
- Removed the deprecated `loadVariation` method.
- Removed the deprecated `sendUserId`.
- Transparent changes reflecting improvements to infrastructure.

Check [Usage](#usage) below for an updated reference of any change.

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

    implementation 'com.github.designhubz-org:android-sdk:3.0.1'

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
     * Request permission listener for watch is permission allow by user or not.
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
                switchContext(null);
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

- To Initialize Components:

```java
DesignhubzWebview.initializeComponents(this);
```

- Starting Eyewear Try-On:

```java
designhubzVar.startEyewearTryon("<userId>", new OnStartEyewearRequestCallback() {

@Override
public void onResult() {
        // write your code to display result
        }

@Override
public void onProgressCallback(Progress progress) {
        // write your code to process or show progress. Value will be between 0 to 1
        }

@Override
public void onTrackingCallback(TrackingStatus trackingStatus) {
        // write your code to use tracking status
        }

@Override
public void onErrorCallback(String errorMessage) {
        // write your code to display error message
        }
});
```

- Starting Makeup Try-On:

```java
designhubzVar.startMakeupTryon("<userId>", new OnStartMakeupRequestCallback() {

@Override
public void onResult() {
        // write code to display result
        }

@Override
public void onProgressCallback(Progress progress) {
        // write code to process or show progress. Value will be between 0 to 1
        }

@Override
public void onTrackingCallback(TrackingStatus trackingStatus) {
        // write code to use tracking status
        }

@Override
public void onErrorCallback(String errorMessage) {
        // write code to display error message
        }
});
```

- Loading a Product:

```java
designhubzVar.loadProduct("<ProductId>", new OnLoadProductCallback() {

@Override
public void onResult(List<Variation> variations) {
        // write code to process list of variations
        // Example: variations.get(<index>).getProperties().get("Color Code")
        }

@Override
public void onProgressCallback(Progress progress) {
        // write code to diplay progress
        }

@Override
public void onErrorCallback(String errorMessage) {
        // write code to display error
        }
});
```

- To switch context from 3D to Tryon and Tryon to 3D (Only for Eyewear Try-On):

```java
designhubzVar.switchContext(new OnEyewearSwitchCallback() {

@Override
public void onProgressCallback(Progress progress) {
        // write code to show progress
        }

@Override
public void onErrorCallback(String errorMessage) {
        // write code to display error
        }
});
```

- To take screenshot of tryon or 3D and get Bitmap image as result:

```java
designhubzVar.takeScreenshot(new OnScreenshotCallback() {
@Override
public void onResult(Bitmap bitmap) {
        // write code to display/save bitmap
        }

@Override
public void onErrorCallback(String errorMessage) {
        // write code to display error
        }
});
```

- To fetch fit info (Only for Eyewear Try-On):

```java
designhubzVar.fetchFitInfo(new OnEyewearFetchFitInfo() {
@Override
public void onResult(Eyewear.Fit fit, Eyewear.Size size) {
        // write code to process or show fit info
        }

@Override
public void onErrorCallback(String errorMessage) {
        // write code to display error
        }
});
```

- To fetch Eyewear/Makeup Recommendations:

```java
designhubzVar.fetchRecommendations(3, new OnRecommendation() {
@Override
public void onResult(List<Recommendations> recommendations) {
        // write code to process or show recommendations
        }

@Override
public void onErrorCallback(String errorMessage) {
        // write code to display error
        }
});
```

- To send statistics to SDK:

```java
designhubzVar.sendStat(Stat.Whishlisted, new OnSendStat() {
@Override
public void onResult(String result) {
        // write code to process or show result
        }

@Override
public void onErrorCallback(String errorMessage) {
        // write code to display error
        }
});
```

- To take before/after screenshots (Only for Makeup Try-On):

```java
designhubzVar.takeDoubleScreenshot(new OnDoubleScreenshotCallback() {
@Override
public void onResult(Bitmap originalSnapshot, Bitmap snapshot) {
        // write code to display before/after images
        }

@Override
public void onErrorCallback(String errorMessage) {
        // write code to display error
        }
});
```

- To start live comparison (Only for Makeup Try-On):

```java
int horizontalRatio = 0.5; //Value between 0 to 1
designhubzVar.liveCompare(horizontalRatio);
```

- To dispose widget:

```java
designhubzVar.disposeWidget(new OnDispose() {
@Override
public void onResult(String result) {
        // write code to process or show result
        }

@Override
public void onErrorCallback(String errorMessage) {
        // write code to display error
        }
});
```
