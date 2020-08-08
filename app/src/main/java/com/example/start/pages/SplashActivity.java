package com.example.start.pages;
import com.example.start.pages.login.LoginActivity;
import com.example.start.utils.*;
import com.example.start.constants.*;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.start.R;

/**
 * 2020-7-15
 * author: lzh
 * description: app启动页, 如果是第一次，直接跳转到引导页，如果不是那么直接跳转到首页
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NoWindowBackground);
        super.onCreate(savedInstanceState);
        this.init();
    }
    // 判断是不是第一次
    public void init() {
        // 从sharePreferences中取出first_open_app,默认值是true
        boolean isFirstOpenApp = (boolean) spUtil.get(this, Constants.FIRST_OPEN_APP, true);
        // 如果是第一次，那么就直接跳转到引导页
        if (isFirstOpenApp) {
            spUtil.put(this, Constants.FIRST_OPEN_APP, false);
            Intent intent = new Intent(this, WelcomeGuide.class);
            startActivity(intent);
            finish();
            return;
        }
        // 进入启动屏
        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
