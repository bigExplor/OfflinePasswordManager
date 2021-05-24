package com.example.accountmanager.activities;

import android.content.Intent;
import android.text.TextUtils;

import com.example.accountmanager.R;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.utils.SpUtil;
import com.example.accountmanager.utils.TimeUtil;

public class SplashActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        TimeUtil.getInstance().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (!TextUtils.isEmpty(SpUtil.getInstance().getString("password"))) {
                    intent = new Intent(SplashActivity.this, PasswordActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    @Override
    protected int getStatusColor() {
        return R.color.white;
    }
}
