package com.example.accountmanager.activities;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accountmanager.R;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.ui.CircleView;
import com.example.accountmanager.utils.BiometricUtil;
import com.example.accountmanager.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CharlesLu
 * @description 启动时输入密码的界面
 */
public class PasswordActivity extends BaseActivity implements View.OnClickListener {

    private CircleView cv;
    private ImageView iv_finger;
    private ImageView iv_delete;
    private TextView tv_0;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private TextView tv_7;
    private TextView tv_8;
    private TextView tv_9;

    private List<Integer> pwd = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    protected void initView() {
        cv = findViewById(R.id.cv);
        iv_finger = findViewById(R.id.iv_finger);
        iv_delete = findViewById(R.id.iv_delete);
        tv_0 = findViewById(R.id.tv_0);
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        tv_4 = findViewById(R.id.tv_4);
        tv_5 = findViewById(R.id.tv_5);
        tv_6 = findViewById(R.id.tv_6);
        tv_7 = findViewById(R.id.tv_7);
        tv_8 = findViewById(R.id.tv_8);
        tv_9 = findViewById(R.id.tv_9);
    }

    @Override
    protected void initData() {
        cv.setCircleNum(4);
        cv.setPaintColor(getResources().getColor(R.color.white));
        if (!SpUtil.getInstance().getBoolean("finger")) {
            iv_finger.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener() {
        tv_0.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        tv_5.setOnClickListener(this);
        tv_6.setOnClickListener(this);
        tv_7.setOnClickListener(this);
        tv_8.setOnClickListener(this);
        tv_9.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        iv_finger.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_0:
                if (pwd.size() >= 4) return;
                pwd.add(0);
                break;
            case R.id.tv_1:
                if (pwd.size() >= 4) return;
                pwd.add(1);
                break;
            case R.id.tv_2:
                if (pwd.size() >= 4) return;
                pwd.add(2);
                break;
            case R.id.tv_3:
                if (pwd.size() >= 4) return;
                pwd.add(3);
                break;
            case R.id.tv_4:
                if (pwd.size() >= 4) return;
                pwd.add(4);
                break;
            case R.id.tv_5:
                if (pwd.size() >= 4) return;
                pwd.add(5);
                break;
            case R.id.tv_6:
                if (pwd.size() >= 4) return;
                pwd.add(6);
                break;
            case R.id.tv_7:
                if (pwd.size() >= 4) return;
                pwd.add(7);
                break;
            case R.id.tv_8:
                if (pwd.size() >= 4) return;
                pwd.add(8);
                break;
            case R.id.tv_9:
                if (pwd.size() >= 4) return;
                pwd.add(9);
                break;
            case R.id.iv_delete:
                if (pwd.size() <= 0) return;
                pwd.remove(pwd.size() - 1);
                break;
            case R.id.iv_finger:
                showBiometric(new OnFingerResultListener() {
                    @Override
                    public void onResult(boolean isSuccess) {
                        if (isSuccess) {
                            Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
        }
        if (v.getId() != R.id.iv_finger) {
            cv.setChoosen(pwd.size());
            if (pwd.size() == 4) check();
        }
    }

    private void check() {
        String ans = "";
        for (Integer i: pwd) ans += i;
        if (ans.equals(SpUtil.getInstance().getString("password"))) {
            showToast("验证通过");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        showToast("密码错误");
    }

    @Override
    protected int getNavigationBarColor() {
        return R.color.logoRed;
    }
}
