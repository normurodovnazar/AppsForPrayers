package com.normurodov_nazar.webviewapp;

import static com.normurodov_nazar.webviewapp.Hey.showPopupMenu;
import static com.normurodov_nazar.webviewapp.Hey.showYesNoDialog;
import static com.normurodov_nazar.webviewapp.Key.district;
import static com.normurodov_nazar.webviewapp.Key.region;
import static com.normurodov_nazar.webviewapp.Key.url;
import static com.normurodov_nazar.webviewapp.Key.urlCity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.normurodov_nazar.webviewapp.databinding.ActivitySettingsBinding;
import com.normurodov_nazar.webviewapp.interfaces.PageListener;

import java.util.ArrayList;
import java.util.Arrays;

public class Settings extends AppCompatActivity {

    final ArrayList<String> regions = new ArrayList<>(Arrays.asList("toshkent", "buxoro", "fargona", "sirdaryo", "jizzax", "navoiy", "namangan",
            "qoraqalpogistonrespublikasi", "samarqand", "surxondaryo", "qashqadaryo", "andijon", "xorazm")),
            areasOfT = new ArrayList<>(Arrays.asList("toshkent", "angren", "piskent", "bekobod", "parkent", "gazalkent", "olmaliq", "boka", "yangiyol", "nurafshon")),
            areasOfB = new ArrayList<>(Arrays.asList("buxoro", "gazli", "gijduvon", "qorakol", "jondor")), areasOfF = new ArrayList<>(Arrays.asList("fargona", "margilon", "qoqon", "quva", "rishton", "bogdod", "oltiariq")),
            areasOfSir = new ArrayList<>(Arrays.asList("guliston", "sardoba", "sirdaryo", "boyovut", "paxtaobod")),
            areasOfJ = new ArrayList<>(Arrays.asList("jizzax", "zomin", "forish", "gallaorol", "dostlik")),
            areasOfNavoiy = new ArrayList<>(Arrays.asList("navoiy", "zarafshon", "konimex", "nurota", "uchquduq")),
            areasOfNamangan = new ArrayList<>(Arrays.asList("namangan", "chortoq", "chust", "pop", "uchqorgon")),
            areasOfQ = new ArrayList<>(Arrays.asList("nukus", "moynoq", "taxtakopir", "tortkol", "qongirot")),
            areasOfSam = new ArrayList<>(Arrays.asList("samarqand", "ishtixon", "mirbozor", "kattaqorgon", "urgut")),
            areasOfSur = new ArrayList<>(Arrays.asList("termiz", "boysun", "denov", "sherobod", "shorchi")),
            areasOfQash = new ArrayList<>(Arrays.asList("qarshi", "dehqonobod", "muborak", "shahrisabz", "guzor")),
            areasOfAnd = new ArrayList<>(Arrays.asList("andijon", "xonobod", "shahrixon", "xojaobod", "asaka", "marhamat", "poytug")),
            areasOfX = new ArrayList<>(Arrays.asList("urganch", "hazorasp", "xonqa", "yangibozor", "shovot", "xiva"));
    SharedPreferences preferences;
    String currentRegion, currentCity;
    ActivitySettingsBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        preferences = Hey.getSharedPreferences(this);
        currentRegion = preferences.getString(region, null);
        b.currentRegion.setText(currentRegion);
        currentCity = preferences.getString(district, null);
        if (currentCity!=null) b.currentDistrict.setText(getString(R.string.selected_city)+ currentCity);
        b.selectDistrict.setOnClickListener(v -> {
            if (currentRegion != null) {
                switch (currentRegion) {
                    case "toshkent":
                        showDistrictMenu(areasOfT);
                        break;
                    case "buxoro":
                        showDistrictMenu(areasOfB);
                        break;
                    case "fargona":
                        showDistrictMenu(areasOfF);
                        break;
                    case "sirdaryo":
                        showDistrictMenu(areasOfSir);
                        break;
                    case "jizzax":
                        showDistrictMenu(areasOfJ);
                        break;
                    case "navoiy":
                        showDistrictMenu(areasOfNavoiy);
                        break;
                    case "namangan":
                        showDistrictMenu(areasOfNamangan);
                        break;
                    case "qoraqalpogistonrespublikasi":
                        showDistrictMenu(areasOfQ);
                        break;
                    case "samarqand":
                        showDistrictMenu(areasOfSam);
                        break;
                    case "surxondaryo":
                        showDistrictMenu(areasOfSur);
                        break;
                    case "qashqadaryo":
                        showDistrictMenu(areasOfQash);
                        break;
                    case "andijon":
                        showDistrictMenu(areasOfAnd);
                        break;
                    case "xorazm":
                        showDistrictMenu(areasOfX);
                        break;
                }
            } else
                Toast.makeText(this,R.string.region_first, Toast.LENGTH_SHORT).show();
        });
        b.selectRegion.setOnClickListener(view -> showPopupMenu(this, b.selectRegion, regions, (message, code) -> {
            currentRegion = String.valueOf(message);
            b.currentRegion.setText(getString(R.string.selected_region) + message);
        }));
        b.apply.setOnClickListener(c-> applyData());
    }

    void showDistrictMenu(ArrayList<String> area) {
        showPopupMenu(this, b.selectDistrict, area, (message, code) -> applyDistrict(message));
    }

    private void applyDistrict(CharSequence message) {
        currentCity = String.valueOf(message);
        b.currentDistrict.setText(getString(R.string.selected_city) + currentCity);
    }

    @Override
    public void onBackPressed() {
        if (isCityChanged()) {
            if (currentCity!=null)
                showYesNoDialog(this, new PageListener() {
                @Override
                public void pageFinishedFailure() {
                    if (currentCity!=null) finish(); else Toast.makeText(getApplicationContext(), R.string.region_and_city, Toast.LENGTH_SHORT).show();
                }
                @Override public void pageStarted() {

                }
                @Override
                public void pageFinishedSuccess(String u) {
                    applyData();
                }
                });  else Toast.makeText(getApplicationContext(), R.string.region_and_city, Toast.LENGTH_SHORT).show();
        } else super.onBackPressed();
    }

    void applyData(){
        if (currentCity !=null) {
            if (isCityChanged()) {
                preferences.edit().putString(district, currentCity).apply();
                urlCity = url+ currentCity;
                setResult(RESULT_OK);
            }
            finish();
        } else Toast.makeText(this, R.string.region_and_city, Toast.LENGTH_SHORT).show();
    }

    boolean isCityChanged(){
        return !preferences.getString(district, "m").equals(currentCity);
    }
}