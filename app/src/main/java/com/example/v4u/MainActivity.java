package com.example.v4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.v4u.bottom.Category_Fragment;
import com.example.v4u.db.DatabaseHandler;
import com.example.v4u.drawer.FavOrder_Fragment;
import com.example.v4u.drawer.Notification_Fragment;
import com.example.v4u.drawer.OrderHistory_Fragment;
import com.example.v4u.drawer.ZZCredit_Fragment;
import com.example.v4u.model.Product;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.razorpay.PaymentResultListener;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PaymentResultListener{

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    private DatabaseHandler databaseHandler;
    private SharedPreferences sharedPreferences;
    private NotificationFunc notificationFunc;

    NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private OkHttpClient httpClient;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
        FirebaseMessaging.getInstance().subscribeToTopic("global");

        notificationFunc = new NotificationFunc(this);
        //notificationFunc.sendNotification("ZapZoo", "Working");

        databaseHandler = new DatabaseHandler(this);
        httpClient = new OkHttpClient();

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        // Hooks
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(sharedPreferences.getString("shop_name", "Empty"));

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.navigation_bottom);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Action bar toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.Home);

        //show and hiding menu item
        Menu menu=navigationView.getMenu();
        if(sharedPreferences.getBoolean("isLogined", false) == true)
        {
            menu.findItem(R.id.Login).setVisible(false);
            menu.findItem(R.id.Logout).setVisible(true);
        } else {
            menu.findItem(R.id.Login).setVisible(true);
            menu.findItem(R.id.Logout).setVisible(false);
        }


        appBarConfiguration = new AppBarConfiguration.Builder(R.id.Home,R.id.search_Fragment,R.id.offers_Fragment,R.id.category_Fragment,R.id.cart_Fragment)
                .setDrawerLayout(drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.Home:
                break;
            case R.id.favOrder_Fragment:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment,new FavOrder_Fragment()).commit();
                break;
            case R.id.notification_Fragment:
                    Intent intent=new Intent(this,Login.class);
                    startActivity(intent);
                    finish();
                    break;
            case R.id.orderHistory_Fragment:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_container,new OrderHistory_Fragment()).commit();
                break;
            case R.id.ZZCredit_Fragment:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_container,new ZZCredit_Fragment()).commit();
                break;
            case R.id.FAQ:
                Intent intent1=new Intent(this,Login.class);
                startActivity(intent1);
                finish();
                Toast.makeText(this, "FAQ !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Rate_Us:
                Toast.makeText(this, "Rate Us !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Feedback:
                Toast.makeText(this, "Feedback !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Share:
                Toast.makeText(this, "Share !", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.d("#####", s);
        List<Product> productList = databaseHandler.getAllProduct();

        JSONObject jsonObject = new JSONObject();
        JSONArray products = new JSONArray();
        try {
            jsonObject.put("username", sharedPreferences.getString("username", "null"));
            jsonObject.put("timestamp", new Timestamp(new Date().getTime()).getTime());
            jsonObject.put("shop_id", sharedPreferences.getString("shop_id", ""));
            jsonObject.put("status", "Received");
            for(Product product: productList)
            {
                products.put(product.getJSONObject());
            }
            jsonObject.put("products", products);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url("https://zapzoo.devsourabh.repl.co/createOrder")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.v("#####", ""+e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String res = response.body().string();
                Log.v("#####", ""+res);
            }
        });

        databaseHandler.emptyCart();
        notificationFunc.sendNotification("ZapZoo", "Order Successfull");
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, PaymentComplete.class);
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d("#####", s);
        Toast.makeText(this, "Payment Unsuccessful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, PaymentFailed.class);
        startActivity(intent);
    }

    public void changeStore(View view) {
        Intent intent = new Intent(this, ShopsList.class);
        startActivity(intent);
    }

    //    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
////        Category_Fragment category_fragment=new Category_Fragment();
////        getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();
//
//        switch (item.getItemId()) {
//            case R.id.category_Fragment:
//                onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}