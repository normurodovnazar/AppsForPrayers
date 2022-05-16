package com.normurodov_nazar.webviewapp;

import static com.normurodov_nazar.webviewapp.Hey.combination;
import static com.normurodov_nazar.webviewapp.Key.district;
import static com.normurodov_nazar.webviewapp.Key.url;
import static com.normurodov_nazar.webviewapp.Key.urlCity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.normurodov_nazar.webviewapp.databinding.ActivityMainBinding;
import com.normurodov_nazar.webviewapp.interfaces.PageListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding b;
    ActivityResultLauncher<Intent> launcher;
    SharedPreferences preferences;
    String path;
    boolean firstOpen = true,;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        b.web.getSettings().setDomStorageEnabled(true);
        b.web.getSettings().setAllowFileAccess(true);
        b.web.getSettings().setAllowFileAccessFromFileURLs(true);
        preferences = Hey.getSharedPreferences(this);
        path = getDir("d",MODE_PRIVATE)+"/"+"vaqti.mht";
        MClient client = new MClient( (message, code) -> {
            if (code == -2) b.errorText.setText(R.string.error_connection);
            else b.errorText.setText(message);
            showErrorPage();
        }, new PageListener() {
            @Override
            public void pageFinishedFailure() {
                showErrorPage();
            }

            @Override
            public void pageFinishedSuccess() {
                showWebPage();
                b.web.saveWebArchive(path);
                Log.e("saved",path);
            }

            @Override
            public void pageStarted() {
                showLoadingPage();
            }
        });
        b.reload.setOnClickListener(view -> {
            b.web.reload();
            showLoadingPage();
        });
        b.reloadIc.setOnClickListener(c->{
            if (b.web.getTitle().equals("a")){

            }else {

            }

            b.web.reload();
            showLoadingPage();
        });
        b.web.setWebViewClient(client);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()==RESULT_OK){
                recreate();
            }
        });
        String city = preferences.getString(district,null);
        if (city==null) launchSettings(); else {
            urlCity = url + city;
            File file = new File(path);
            if (file.exists()){
                b.web.loadUrl("file://"+path);
            }else {
                b.web.loadUrl(urlCity);
            }
            Log.e("exists", String.valueOf(file.exists()));
        }
        b.settings.bringToFront();
        b.reloadIc.bringToFront();
        b.settings.setOnClickListener(v-> launchSettings());
    }

    void launchSettings(){
        launcher.launch(new Intent(this,Settings.class));
    }

    @Override
    public void onBackPressed() {
        if (b.web.canGoBack()) b.web.goBack(); else super.onBackPressed();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }else return super.onKeyLongPress(keyCode, event);
    }

    private void showErrorPage(){
        Log.e("showErrorPage","a");
        combination(b.loadingBar,b.web,b.errorPage);
    }

    private void showWebPage(){
        Log.e("showWebPage","a");
        combination(b.loadingBar,b.errorPage,b.web);
    }

    private void showLoadingPage(){
        Log.e("showLoadingPage","a");
        combination(b.web,b.errorPage,b.loadingBar);
    }
}