package com.normurodov_nazar.webviewapp.interfaces;

public interface PageListener {
    void pageFinishedFailure();
    void pageStarted();
    void pageFinishedSuccess(String url);
}
