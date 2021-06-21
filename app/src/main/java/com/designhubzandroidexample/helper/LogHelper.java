package com.designhubzandroidexample.helper;

import android.content.Context;
import android.util.Log;

public class LogHelper {
    public void logText(String className,String methodName,String logTextData){
        Log.e(className,methodName+"--> "+logTextData);
    }
}
