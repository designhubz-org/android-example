package com.designhubzandroidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.designhubz.androidsdk.DesignhubzGeckoview;

public class GeckoVideoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gecko_video_view);
        DesignhubzGeckoview view = findViewById(R.id.geckoCamera);

        view.initView(this);
    }
}