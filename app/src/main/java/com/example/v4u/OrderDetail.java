package com.example.v4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.example.v4u.adapters.OrderProductListAdapter;
import com.example.v4u.model.Product;

import java.util.List;

public class OrderDetail extends AppCompatActivity {

    private List<Product> productList;
    private Toolbar toolbar;
    private String title;
    private RecyclerView recyclerView;
    private OrderProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = getIntent().getStringExtra("orderid");
        productList = (List<Product>) getIntent().getExtras().getSerializable("products");

        getSupportActionBar().setTitle("Order #"+title);

        adapter = new OrderProductListAdapter(productList, this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}