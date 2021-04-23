package com.example.v4u;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.v4u.adapters.ShopListAdapter;
import com.example.v4u.listener.ShopOnItemClickListener;
import com.example.v4u.model.Shop;
import com.example.v4u.model.SingleProduct;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShopsList extends AppCompatActivity {

    private Button changeAddress;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private List<Shop> shopList = new ArrayList<>();
    private ShopListAdapter shopListAdapter;
    private ProgressDialog progressDialog;
    private OkHttpClient client;
    private ShopOnItemClickListener listener;

    private static DecimalFormat df = new DecimalFormat("0.00");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_list);

        //change status bar color

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Nearby Stores...");

        client = new OkHttpClient();

        changeAddress = (Button) findViewById(R.id.change_address);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopsList.this, AddressActivity.class);
                startActivity(intent);
            }
        });

        listener = new ShopOnItemClickListener() {
            @Override
            public void onSelect(int position) {
                Log.d("#####", ""+shopList.get(position).getShop_id());
                if(shopList.get(position).getShop_id().equalsIgnoreCase("0"))
                {
                    Toast.makeText(ShopsList.this, "Shop Not Available", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ShopsList.this, MainActivity.class);
                editor.putString("shop_id", ""+shopList.get(position).getShop_id());
                editor.putString("shop_name", ""+shopList.get(position).getName());
                editor.commit();
                startActivity(intent);
                finishAffinity();
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        shopListAdapter = new ShopListAdapter(shopList, this, listener);
        recyclerView.setAdapter(shopListAdapter);

        fetchNearby();

    }

    public void fetchNearby() {
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String lat = sharedPreferences.getString("lat", "");
        final String lng = sharedPreferences.getString("lng", "");

        final Request request = new Request.Builder()
                .url("https://zapzoo.devsourabh.repl.co/getNearbyStores?lat="+lat+"&lng="+lng)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("#####", ""+e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String res = response.body().string();
                Log.d("#####", "res "+res);

                try {
                    JSONArray jsonArray = new JSONArray(res);
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Shop shop = new Shop();
                        shop.setShop_id(""+jsonObject.getString("shop_id"));
                        shop.setName(""+jsonObject.getString("shopName"));

                        //JSONObject loc = jsonObject.getJSONObject("geometry").getJSONObject("location");
                        String lat_shop = jsonObject.getString("lat");
                        String lng_shop = jsonObject.getString("lng");

                        double dist = distance(Double.parseDouble(lat), Double.parseDouble(lng), Double.parseDouble(lat_shop), Double.parseDouble(lng_shop), "K");
                        shop.setDistance(Double.parseDouble(df.format(dist)));

                        String storeImageUrl = jsonObject.getString("storeImageUrl");
                        shop.setImage_url(storeImageUrl);

                        /*if(jsonObject.has("photos"))
                        {
                            JSONArray photos = jsonObject.getJSONArray("photos");
                            String photo_reference = photos.getJSONObject(0).getString("photo_reference");
                            shop.setImage_url("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyBTWpbHWfWgdqOyeuHqLwox-nEgYn8hHjs&photoreference="+photo_reference);
                        }*/
                        shopList.add(shop);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shopListAdapter.notifyDataSetChanged();
                    }
                });

                progressDialog.dismiss();
            }
        });

        return;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}