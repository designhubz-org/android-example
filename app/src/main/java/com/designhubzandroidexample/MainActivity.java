package com.designhubzandroidexample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.designhubz.androidsdk.helper.Product;
import com.designhubzandroidexample.adapter.ProductListAdapter;
import com.designhubzandroidexample.helper.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * The MainActivity targets first page of app.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView rcvProductList;
    private LinearLayoutManager linearLayoutManager;
    private List<Product> productList = new ArrayList<>();
    private ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        rcvProductList = findViewById(R.id.rcvProductList);

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rcvProductList.setLayoutManager(linearLayoutManager);

        getProducts();

    }

    private void getProducts() {
        productList.add(new Product(1.365,"Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 2300, 3000));
        productList.add(new Product(2.365,"Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 5300, 7400));
        productList.add(new Product(3.365,"Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 2500, 3050));
        productList.add(new Product(4.365,"Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 4300, 5000));
        productList.add(new Product(5.365,"Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 2400, 3200));
        productList.add(new Product(6.365,"Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 2300, 3000));
        productList.add(new Product(7.365,"Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 5300, 7400));
        productList.add(new Product(8.365,"Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 2500, 3050));
        productList.add(new Product(9.365,"Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 4300, 5000));
        productList.add(new Product(10.35,"Fastrack", "Fastrack P254678D Green Anti-Reflactive Sunglasses", 2400, 3200));

        productListAdapter = new ProductListAdapter(this, productList) {
            @Override
            public void onClickItem(int adapterPosition) {
                Constant.mProduct = productList.get(adapterPosition);
                startActivity(new Intent(MainActivity.this,VideoViewActivity.class));
            }
        };
        rcvProductList.setAdapter(productListAdapter);

    }
}