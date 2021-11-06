package com.designhubzandroidexample.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * Created by Bhargav 14/10/2021
 */
public class PreferencesManager {

    private static final String KEY_ENABLE_DEBUG = "enableDebug";

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "designhubzPrefs";


    public PreferencesManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean getEnableDebug() {
        return pref.getBoolean(KEY_ENABLE_DEBUG, false);
    }

    public void setEnableDeug(boolean b) {
        editor.putBoolean(KEY_ENABLE_DEBUG, b);
        editor.commit();
    }
}