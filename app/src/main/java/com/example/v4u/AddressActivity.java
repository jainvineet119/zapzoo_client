package com.example.v4u;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Toolbar toolbar;
    private LocationManager locationManager;
    private String provider;
    private Location location;
    private TextView currentAddress;
    private MarkerOptions markerOptions;
    private Marker marker;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private OkHttpClient client;
    private LatLng latLng;
    private Button selectAddressBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        selectAddressBtn = (Button) findViewById(R.id.selectAddressBtn);

        client = new OkHttpClient();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Choose Address");

        currentAddress = (TextView) findViewById(R.id.currentAddress);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //Log.d("#####", provider);
        //location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        markerOptions = new MarkerOptions()
                .position(new LatLng(20.5937, 78.9629))
                .title("You are here");
        markerOptions.draggable(true);

        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Make sure Location is ON.\nFetching Location...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //locationManager.removeUpdates(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng myloc = new LatLng(20.5937, 78.9629);
        marker = googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(myloc));
    }

    /*@Override
    public void onLocationChanged(Location location) {
        if(progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        this.location = location;

        LatLng myloc = new LatLng(location.getLatitude(), location.getLongitude());

        marker.setPosition(myloc);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myloc)
                .zoom(17)
                .bearing(90)
                .tilt(30)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            currentAddress.setText(""+address);
            editor.putString("myaddress", ""+address).commit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }*/

    public void confirmLocation(View view) {
        if(latLng == null)
        {
            Toast.makeText(this, "Select Location!!", Toast.LENGTH_SHORT).show();
            return;
        }
        editor.putString("lat", ""+latLng.latitude);
        editor.putString("lng", ""+latLng.longitude);
        editor.commit();

        RequestBody formBody = new FormEncodingBuilder()
                .add("username", sharedPreferences.getString("username", ""))
                .add("lat", String.valueOf(latLng.latitude))
                .add("lng", String.valueOf(latLng.longitude))
                .build();

        Request request = new Request.Builder()
                .url("https://zapzoo.devsourabh.repl.co/changeLocation")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddressActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddressActivity.this, ""+res, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddressActivity.this, ShopsList.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void chooseAddress(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddressActivity.this), 333);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 333)
        {
            Place place = PlacePicker.getPlace(this, data);
            String placeName = String.format("Place: %s", place.getName());
            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;
            latLng = new LatLng(latitude, longitude);

            marker.setPosition(latLng);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(17)
                    .bearing(90)
                    .tilt(30)
                    .build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String shopAddress = "" + address + city;
                currentAddress.setText(shopAddress);
                currentAddress.setVisibility(View.VISIBLE);
                selectAddressBtn.setText("Change Location");
                editor.putString("myaddress", ""+address+" "+city).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("#####", "PlaceName : "+placeName+" "+latitude+" "+longitude);
        }
    }
}