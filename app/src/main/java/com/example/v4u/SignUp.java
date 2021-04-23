package com.example.v4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class SignUp extends AppCompatActivity {

    Button callLogin,SignUpbtn;
    ImageView image;
    TextView logo_name,sloganText;
    TextInputLayout username,password;
    private EditText fullnameET, usernameET, emailET, phoneNumberET, passwordET;
    private OkHttpClient client;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        callLogin=findViewById(R.id.signInButton);  // sign up button
        SignUpbtn=findViewById(R.id.signUpButton); // login button
        image = findViewById(R.id.loginLogo);       // logo image
        sloganText=findViewById(R.id.slogan_name);  // signin/signup text
        username=findViewById(R.id.username);   // username tv
        password=findViewById(R.id.password);   // password tv
        logo_name=findViewById(R.id.loginText);     // logo name

        fullnameET = (EditText) findViewById(R.id.fullnameET);
        usernameET = (EditText) findViewById(R.id.usernameET);
        emailET = (EditText) findViewById(R.id.emailET);
        phoneNumberET = (EditText) findViewById(R.id.phoneNumberET);
        passwordET = (EditText) findViewById(R.id.passwordET);

        client = new OkHttpClient();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("#####", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();
                        editor.putString("fcmToken", token).commit();

                        Log.d("#####", token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void signUpButton(View view){
        String fn = fullnameET.getText().toString();
        String un = usernameET.getText().toString();
        String em = emailET.getText().toString();
        String pn = phoneNumberET.getText().toString();
        String pw = passwordET.getText().toString();
        String fcmToken = preferences.getString("fcmToken", "null");

        RequestBody formBody = new FormEncodingBuilder()
                .add("fullname", fn)
                .add("username", un)
                .add("email", em)
                .add("phoneNumber", pn)
                .add("password", pw)
                .add("fcmToken", fcmToken)
                .build();

        Request request = new Request.Builder()
                .url("https://zapzoo.devsourabh.repl.co/register")
                .post(formBody)
                .build();
        
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SignUp.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String res = response.body().string();
                final int res_code = response.code();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SignUp.this, ""+res, Toast.LENGTH_SHORT).show();
                        if(res_code == 200)
                        {
                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
        //Toast.makeText(SignUp.this, "Sign Up Successfully !", Toast.LENGTH_SHORT).show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(SignUp.this, "Login to Continue !", Toast.LENGTH_SHORT).show();
//            }
//        },2000);
//        startActivity(new Intent(this,Login.class));
        //finish();


    }

    public void signInButton(View view){
        startActivity(new Intent(this,Login.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
    public void SignSkipBtn(View view){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}


