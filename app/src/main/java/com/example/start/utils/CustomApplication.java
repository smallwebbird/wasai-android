package com.example.start.utils;

import android.app.Application;

// 获取自定义的application
// 单例模式
public class CustomApplication extends Application {
    private static CustomApplication app;

    public static CustomApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
