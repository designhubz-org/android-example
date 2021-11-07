package com.designhubzandroidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.designhubz.androidsdk.api.Product;
import com.designhubzandroidexample.adapter.ProductListAdapter;
import com.designhubzandroidexample.helper.Constant;
import com.designhubzandroidexample.helper.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * The MainActivity targets first page of app.
 */
public class MainActivity extends AppCompatActivity {

    private SwitchCompat swDebug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        swDebug = findViewById(R.id.swDebug);
        swDebug.setChecked(new PreferencesManager(MainActivity.this).getEnableDebug());
        //Enable/Disable debug logs
        swDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean enable) {
                new PreferencesManager(MainActivity.this).setEnableDeug(enable);
            }
        });

    }

    public void StartEyewearTryon(View view) {
        startActivity(new Intent(MainActivity.this, EyewearTryonActivity.class));
    }

    public void StartMakeupTryon(View view) {
        startActivity(new Intent(MainActivity.this, MakeupTryonActivity.class));
    }

}