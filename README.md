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
    implementation 'com.github.designhubz-org:android-sdk:1.4'
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


- To start the eyewear try-on widget:

```java
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

designhubzVar.startEyewearTryon("MP000000006870126",new OnStartEyewearRequestCallback() {
      @Override
      public void onResult(List<Variation> variations) {
          // write your code to process or show variations
          progressDialog.dismiss();
      }

      @Override
      public void onProgressCallback(Progress progress) {
          // write your code to process or show progress
      }

      @Override
      public void onTrackingCallback(TrackingStatus trackingStatus) {
          // write your code to process or show tracking status
          progressDialog.dismiss();
          Toast.makeText(VideoViewActivity.this, ""+trackingStatus.getValue(), Toast.LENGTH_SHORT).show();
      }
});
```

- To  load another variation:

```java
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
designhubzVar.loadVariation("MP000000007163139",new OnEyewearVariationCallback() {
      @Override
      public void onResult(List<Variation> variations) {
          // write your code to process or show variations
          progressDialog.dismiss();
      }

      @Override
      public void onProgressCallback(Progress progress) {
          // write your code to process or show progress
      }
});
```
- To switch context (toggle between 3D/Tryon):

```java
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
designhubzVar.switchContext(new OnEyewearSwitchCallback() {
      @Override
      public void onResult(String result) {
          // write your code to process or show result
          progressDialog.dismiss();
      }

      @Override
      public void onProgressCallback(Progress progress) {
          // write your code to process or show progress
      }
});
```
- To take a screenshot (returns bitmap image):

```java
progressDialog.show();
 /**
 * takeScreenshot
 *
 * Take screenshot of tryon or 3D tryon and returns Bitmap image as result
 *
 * @param OnEyewearScreenshotCallback override one callback methods
 *        1. onResult callbacks Bitmap image of tryon
 */
designhubzVar.takeScreenshot(new OnEyewearScreenshotCallback() {
      @Override
      public void onResult(Bitmap bitmap) {
                progressDialog.dismiss();
                // write your code to process or show image
      }
});

```

- To Fetch fit information:

```java
progressDialog.show();
/**
* fetchFitInfo
*
* Fetch fit info of eyewear
*
* @param OnEyewearFetchFitInfo override One callback methods
      *1. onResult callbacks receive two result i.e Eyewear Fit and Eyewear Size
*/
designhubzVar.fetchFitInfo(new OnEyewearFetchFitInfo() {
        @Override
        public void onResult(Eyewear.Fit fit, Eyewear.Size size) {
            // write your code to process or show fit info
            Toast.makeText(VideoViewActivity.this, "FIT:-"+fit.getValue()+" SIZE:-"+size.getValue(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
});

```
- To Fetch recommendations (pass number of recommendations needed):

```java
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
designhubzVar.fetchRecommendations(<"pass here number of recommandation">,new OnEyewearRecommendation() {
    @Override
    public void onResult(List<Recommendations> recommendations) {
        // write your code to process or show recommendations
        progressDialog.dismiss();
    }
});

```
- To Send statistics To SDK (Stats can be: Whishlisted, AddedToCart, SnapshotSaved,SharedToSocialMedia):

```java
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
designhubzVar.sendStat(Stat.Whishlisted,new OnEyewearSendStat() {
    @Override
    public void onResult(String result) {
        // write your code to process or show result
        progressDialog.dismiss();
    }
});

```
- To Send userID To SDK:

```java
progressDialog.show();
/**
* sendUserID
*
* Send user ID To SDK
*
* @param OnSendID override One callback methods
*        1. onResult callbacks string result
*/
designhubzVar.sendUserID(new OnSendID() {
    @Override
    public void onResult(String result) {
        // write your code to process or show result
        progressDialog.dismiss();
    }
});

```
- To Send request to dispose widget To SDK:

```java
progressDialog.show();
/**
* DisposeWidget
*
* To dispose widget
*
* @param OnEyewearDispose override One callback methods
*        1. onResult callbacks string result
*/
designhubzVar.disposeWidget(new OnEyewearDispose() {
    @Override
    public void onResult(String result) {
        // write your code to process or show result
        progressDialog.dismiss();
    }
});

```
