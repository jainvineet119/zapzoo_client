package com.example.v4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.v4u.bottom.Home_Fragment;

public class PaymentComplete extends AppCompatActivity {

    private Toolbar toolbar;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_complete);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        continueBtn=findViewById(R.id.shoppingBtn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PaymentComplete.this, Home_Fragment.class);
                startActivity(intent);
            }
        });

    }
}