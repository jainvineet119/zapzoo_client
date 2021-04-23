package com.example.v4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.v4u.bottom.Cart_Fragment;

public class PaymentFailed extends AppCompatActivity {

    private Toolbar toolbar;
    Button failed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        failed=findViewById(R.id.tryBtn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PaymentFailed.this, Cart_Fragment.class);
                startActivity(intent);
            }
        });

    }
}