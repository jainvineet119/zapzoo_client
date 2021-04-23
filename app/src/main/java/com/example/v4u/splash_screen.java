package com.example.v4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash_screen extends AppCompatActivity {

    //variables
    public int SPLASH_TIMER = 1500;
    ImageView image;
    TextView logo;
    TextView slogan;
    Animation topAnim, sideAnim, lsideAnim;
    SharedPreferences onBoardingScreen, sharedPreferences;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar

        //Hooks
        image = findViewById(R.id.ssImage);
        logo = findViewById(R.id.ssText);
        slogan = findViewById(R.id.ssText2);

        topAnim = AnimationUtils.loadAnimation(splash_screen.this, R.anim.topanim);  // Animations initialize
        sideAnim = AnimationUtils.loadAnimation(splash_screen.this, R.anim.sideanim);
        lsideAnim = AnimationUtils.loadAnimation(splash_screen.this, R.anim.lsideanim);

        image.setAnimation(topAnim); // animation set
        logo.setAnimation(sideAnim);
        slogan.setAnimation(lsideAnim);

        builder = new AlertDialog.Builder(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            allDone();
        } else {
            askPermission();
        }
    }

    public void allDone() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE); // onBoarding Visits
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

                if (isFirstTime) {

                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), onBoarding.class);
                    startActivity(intent);
                    finish();
                } else {
                    if(sharedPreferences.getBoolean("isLogined", false) == true)
                    {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                        return;
                    }
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(image,"logo_image");
                    pairs[1] = new Pair<View, String>(logo,"logo_text");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(splash_screen.this, pairs);
                        startActivity(intent, options.toBundle());
                        finishAffinity();
                    }
                }
            }
        }, SPLASH_TIMER);
    }

    public void askPermission() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 10);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 10)
        {
            Log.d("#####", ""+grantResults.length);
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                allDone();
            } else {
                builder.setTitle("Error");
                builder.setMessage("Permission Failed!!");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        askPermission();
                    }
                });
                builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                });
                builder.show();
            }
        }
    }
}