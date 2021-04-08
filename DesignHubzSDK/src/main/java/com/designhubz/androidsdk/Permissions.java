package com.designhubz.androidsdk;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.designhubz.androidsdk.helper.RequestCodes;

/**
 * The type Permissions.
 */
public class Permissions {

    /**
     * Check camera permission
     *
     * @param activity the activity
     * @return returns is permission allow or not
     */
    public static boolean checkPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            return false;
        }else
        return true;
    }

    /**
     * Request permission.
     *
     * @param activity the activity
     */
    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                RequestCodes.REQUEST_CODE_PERMISSION);
    }
}
