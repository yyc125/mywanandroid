package com.sanki.mywanandroid.app;

import android.app.Application;


public class MyApp extends Application {
    public static MyApp mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static synchronized MyApp getContext() {
        return mContext;
    }
}
