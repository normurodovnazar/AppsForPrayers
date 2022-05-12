package com.normurodov_nazar.appsforprayers.Additional;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.normurodov_nazar.appsforprayers.Models.PrayerTime;

import java.util.Calendar;

public class Hey {
    public static String getItemName(PrayerTime item) {
        return item.getType()+" "+getTime(item.getTime());
    }

    private static String getTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String m = String.valueOf(calendar.get(Calendar.MINUTE));
        return calendar.get(Calendar.HOUR_OF_DAY)+":"+(m.length()==1 ? "0"+m : m);
    }

    public static boolean isFineNotAllowed(Context context){
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCoarseNotAllowed(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    public static void print(String tag,String message){
        Log.e(tag,message==null ? "null" : message);
    }

    public static void showToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
