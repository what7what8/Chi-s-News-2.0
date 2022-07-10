package com.chinews.xdapp;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Application instance;

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}