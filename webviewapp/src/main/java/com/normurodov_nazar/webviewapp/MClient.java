package com.normurodov_nazar.webviewapp;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.normurodov_nazar.webviewapp.interfaces.ErrorListener;
import com.normurodov_nazar.webviewapp.interfaces.PageListener;

public class MClient extends WebViewClient {
    final ErrorListener errorListener;
    final PageListener pageListener;
    public MClient(ErrorListener errorListener, PageListener pageListener){
        this.errorListener = errorListener;
        this.pageListener = pageListener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        Log.e("onReceivedError","a");
        errorListener.onError(error.getDescription(),error.getErrorCode());
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        Log.e("onLoadResource",url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.e("onPageStarted",url);
        pageListener.pageStarted();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.e("onPageFinished",view.getTitle());
        if (view.getTitle().contains("vaqti")) pageListener.pageFinishedSuccess(); else pageListener.pageFinishedFailure();
    }
}
