package com.normurodov_nazar.webviewapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;

import com.normurodov_nazar.webviewapp.interfaces.ErrorListener;
import com.normurodov_nazar.webviewapp.interfaces.PageListener;


import java.util.ArrayList;

public class Hey {
    public static void hideAnimation(View view){
        view.clearAnimation();
        view.setAlpha(1f);
        view.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        }).start();
    }

    public static void showAnimation(View view,ErrorListener errorListener){
        view.clearAnimation();
        view.setAlpha(0f);
        view.animate().alpha(1f).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                errorListener.onError(null,0);
            }
        }).start();
    }

    public static void combination(View v1,View v2,View v3){
        hideAnimation(v1);
        hideAnimation(v2);
        showAnimation(v3, (message, code) -> {});
        v3.setVisibility(View.VISIBLE);
    }

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("a",Context.MODE_PRIVATE);
    }

    public static PopupMenu showPopupMenu(Context context, View item, ArrayList<String> items, ErrorListener itemClickListener) {
        PopupMenu menu = new PopupMenu(context, item);
        for (String s : items) {
            menu.getMenu().add(Menu.NONE, Menu.NONE, items.indexOf(s), s);
        }
        menu.setOnMenuItemClickListener(item1 -> {
            itemClickListener.onError(item1.getTitle(), item1.getOrder());
            return true;
        });
        menu.show();
        return menu;
    }

    public static void showYesNoDialog(Activity activity, PageListener pageListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.save_changes).setPositiveButton(R.string.yes, (dialogInterface, i) -> pageListener.pageFinishedSuccess(null)).setNegativeButton(R.string.no, (dialogInterface, i) -> pageListener.pageFinishedFailure()).show();
    }
}
