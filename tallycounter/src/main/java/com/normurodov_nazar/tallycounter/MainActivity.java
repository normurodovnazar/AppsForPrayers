package com.normurodov_nazar.tallycounter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.normurodov_nazar.tallycounter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences;

    ActivityMainBinding b;
    byte i = 0;
    Vibrator vibrator;
    boolean vibrate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        preferences = getPreferences(MODE_PRIVATE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        applyTheme();
        applyValue();
        b.btn.setOnClickListener(view -> {
            if (i < 32) {
                if (vibrate) shortVibrate();
                i++;
                b.t.setText(String.valueOf(i));
            } else {
                reset();
            }
        });
        b.reset.setOnClickListener(view -> reset());
        showView();
        b.vibration.setOnClickListener(view -> {
            preferences.edit().putBoolean("v", !vibrate).apply();
            showView();
        });
        b.day.setOnClickListener(view -> {
            saveValue();
            preferences.edit().putBoolean("n",false).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });
        b.night.setOnClickListener(view -> {
            saveValue();
            preferences.edit().putBoolean("n",true).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveValue();
    }

    private void saveValue(){
        preferences.edit().putString("s",String.valueOf(i)).apply();
    }

    private void applyValue(){
        i = Byte.parseByte(preferences.getString("s","0"));
        b.t.setText(String.valueOf(i));
    }

    private void applyTheme(){
        if (preferences.getBoolean("n",true)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void reset() {
        if (vibrate) longVibrate();
        i = 0;
        b.t.setText(String.valueOf(i));
    }

    private void showView() {
        if (preferences.getBoolean("v", true)) {
            vibrate = true;
            b.vibration.setBackgroundResource(R.drawable.item_selected);
        } else {
            vibrate = false;
            b.vibration.setBackgroundResource(R.drawable.item_not_selected);
        }
    }

    private void longVibrate() {
        vibrator.vibrate(100);
    }

    private void shortVibrate() {
        vibrator.vibrate(15);
    }
}