package com.example.v4u.category.personalcat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.v4u.R;
import com.example.v4u.ViewPagerAdapter;
import com.example.v4u.category.beveragecat.tea_coffee;
import com.google.android.material.tabs.TabLayout;

public class personal_cat extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_cat);

        toolbar=findViewById(R.id.myToolbar);
        tabLayout=findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.myViewPager);

        //change status bar color

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager(viewPager);

        // getSupportActionBar().setTitle("Grocery & Staples");
        getSupportActionBar().setTitle(null);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(tea_coffee.newInstance("Personal Care", "Bath and Handwash"),"Bath & Handwash");
        viewPagerAdapter.addFragment(tea_coffee.newInstance("Personal Care", "Oral Care"),"Oral Care");
        viewPagerAdapter.addFragment(tea_coffee.newInstance("Personal Care", "Hair Care"),"Hair Care");
        viewPagerAdapter.addFragment(tea_coffee.newInstance("Personal Care", "Feminine Hygiene"),"Feminine Hygiene");
        viewPagerAdapter.addFragment(tea_coffee.newInstance("Personal Care", "Skin Care and Body Care"),"Skin Care & Body Care");



        viewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}