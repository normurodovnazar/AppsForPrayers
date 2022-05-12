package com.normurodov_nazar.appsforprayers;


import static com.normurodov_nazar.appsforprayers.Additional.Const.elevation;
import static com.normurodov_nazar.appsforprayers.Additional.Const.latitude;
import static com.normurodov_nazar.appsforprayers.Additional.Const.longitude;
import static com.normurodov_nazar.appsforprayers.Additional.Const.url;
import static com.normurodov_nazar.appsforprayers.Additional.Hey.isCoarseNotAllowed;
import static com.normurodov_nazar.appsforprayers.Additional.Hey.isFineNotAllowed;
import static com.normurodov_nazar.appsforprayers.Additional.Hey.print;
import static com.normurodov_nazar.appsforprayers.Additional.Hey.showToast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.normurodov_nazar.appsforprayers.Additional.Adapter;
import com.normurodov_nazar.appsforprayers.Additional.Const;
import com.normurodov_nazar.appsforprayers.Models.PrayerTime;
import com.normurodov_nazar.appsforprayers.databinding.ActivityMainBinding;

import com.normurodov_nazar.appsforprayers.Additional.Const.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        try {
            intiVars();
        } catch (JSONException | SecurityException e) {
            e.printStackTrace();
            print("error", e.getLocalizedMessage());
        }
    }

    private void intiVars() throws JSONException,SecurityException {
        getData();
        //cycle();
    }

    private void getData() throws SecurityException {
        print("getData", "called");
        JSONObject data = new JSONObject();
        //LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f,this);
        //Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //double x = 69.37600193,y = 41.13132431,z = 432.2777099609375;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,"https://dog.ceo/api/breeds/image/random", null, response -> {
            print("Response",response.toString());
            try {
                Glide.with(this).load(response.get("message")).placeholder(R.drawable.ic_off).into(b.image);
            } catch (JSONException e) {
                e.printStackTrace();
                print("Error", e.getLocalizedMessage());
            }
        }, error -> {
            print("Error", error.getLocalizedMessage());
        });

        requestQueue.add(request);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            //requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);
            getData();
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
            getData();
            return true;
        }else return super.onKeyDown(keyCode, event);
    }
}