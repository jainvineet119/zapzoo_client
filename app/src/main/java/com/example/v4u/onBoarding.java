package com.example.v4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class onBoarding extends AppCompatActivity {

    //Variables
    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView dots[];
    Button getStartedButton;
    Animation animation;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        viewPager =findViewById(R.id.slider);
        dotsLayout=findViewById(R.id.dots);
        getStartedButton=findViewById(R.id.get_started_btn);

        //Call Adapter
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);

    }

    public void skip(View view){     // SKIP BUTTON
        startActivity(new Intent(this,AddressActivity.class));
        finish();
    }

    public void next(View view){    // NEXT BUTTON
        viewPager.setCurrentItem(currentPosition+1);
    }

    public void getStarted(View view) // GET STARTED BUTTON
    {
        startActivity(new Intent(this,AddressActivity.class));
        finish();
    }

    public void addDots(int position) {             //  ADD DOTS FUNCTION
        dots=new TextView[4];
        dotsLayout.removeAllViews();

        for(int i=0;i<dots.length;i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if(dots.length>0)
        {
            dots[position].setTextColor(getResources().getColor(R.color.skyblue));
        }

    }

    ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPosition=position;
            if(position == 3){
                animation= AnimationUtils.loadAnimation(onBoarding.this,R.anim.button);
                getStartedButton.setAnimation(animation);
                getStartedButton.setVisibility(View.VISIBLE);
            }
            else{
                getStartedButton.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}