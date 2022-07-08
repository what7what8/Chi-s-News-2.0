package com.chinews.xdapp;

import android.app.Application;
import android.content.Context;

import java.math.BigDecimal;

public class App extends Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}