package com.designhubzandroidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.designhubz.androidsdk.api.Product;
import com.designhubzandroidexample.adapter.ProductListAdapter;
import com.designhubzandroidexample.helper.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * The MainActivity targets first page of app.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

    }

    public void StartEyewearTryon(View view) {
        startActivity(new Intent(MainActivity.this, EyewearTryonActivity.class));
    }

    public void StartMakeupTryon(View view) {
        startActivity(new Intent(MainActivity.this, MakeupTryonActivity.class));
    }

}