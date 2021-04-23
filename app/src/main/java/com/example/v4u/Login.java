package com.example.v4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Login extends AppCompatActivity {

    Button callSignUP,login_btn;
    ImageView image;
    TextView logo_name,sloganText;
    TextInputLayout username,password;
    private EditText usernameET, passwordET;
    private OkHttpClient client;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        callSignUP=findViewById(R.id.signUp);  // sign up button
        login_btn=findViewById(R.id.loginButton); // login button
        image = findViewById(R.id.loginLogo);       // logo image
        sloganText=findViewById(R.id.slogan_name);  // signin/signup text
        username=findViewById(R.id.username);   // username tv
        password=findViewById(R.id.password);   // password tv
        logo_name=findViewById(R.id.loginText);     // logo name

        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);

        client = new OkHttpClient();

        callSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Login.this,SignUp.class);

                Pair[] pairs=new Pair[7];
                pairs[0]= new Pair<View,String>(image,"logo_image");
                pairs[1]= new Pair<View,String>(logo_name,"logo_text");
                pairs[2]= new Pair<View,String>(sloganText,"logo_sign");
                pairs[3]= new Pair<View,String>(username,"logo_username");
                pairs[4]= new Pair<View,String>(password,"logo_password");
                pairs[5]= new Pair<View,String>(login_btn,"logo_login");
                pairs[6]= new Pair<View,String>(callSignUP,"logo_signup");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                    startActivity(intent,options.toBundle());
                }

            }
        });

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

    public void login(View view){
        final String un = usernameET.getText().toString();
        final String pw = passwordET.getText().toString();
        final String fcmToken = preferences.getString("fcmToken", "null");

        //NotificationFunc notificationFunc = new NotificationFunc(this);
        //notificationFunc.sendNotification("ZapZoo", "Order Confirmed");

        if(un.isEmpty() || pw.isEmpty())
        {
            Toast.makeText(this, "Field Empty!!", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody formBody = new FormEncodingBuilder()
                .add("username", un)
                .add("password", pw)
                .add("fcmToken", fcmToken)
                .build();

        Request request = new Request.Builder()
                .url("https://zapzoo.devsourabh.repl.co/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login.this, "Network Error", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Login.this, ""+res, Toast.LENGTH_SHORT).show();
                        if(res_code == 200)
                        {
                            editor.putBoolean("isLogined", true);
                            editor.putString("username", un);
                            editor.commit();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
//        startActivity(new Intent(this,MainActivity.class));
//        finish();
    }
    public void LoginSkipBtn(View view){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}