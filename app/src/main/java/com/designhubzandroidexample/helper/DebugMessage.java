package com.designhubzandroidexample.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class DebugMessage {
    public static void print(Context context, CharSequence text) {
        try {
            if (!((Activity) context).isFinishing() && new PreferencesManager(context).getEnableDebug()) {
                Log.e("DEBUG MESSAGE", text.toString());
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
