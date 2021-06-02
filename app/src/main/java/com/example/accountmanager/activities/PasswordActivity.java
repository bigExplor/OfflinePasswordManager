package com.example.accountmanager.activities;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accountmanager.R;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.presenter.PasswordActivityPresenter;
import com.example.accountmanager.ui.CircleView;
import com.example.accountmanager.utils.SpUtil;

/**
 * @author CharlesLu
 * @description 启动时输入密码的界面
 */
public class PasswordActivity extends BaseActivity<PasswordActivityPresenter> implements View.OnClickListener {

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    protected PasswordActivityPresenter getPresenter() {
        return new PasswordActivityPresenter();
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
        String from = getIntent().getStringExtra("from");
        if ("private".equals(from)) {
            p.from = from;
            p.id = getIntent().getIntExtra("id", -1);
            p.name = getIntent().getStringExtra("name");
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_0:
                p.onClick(0, "");
                break;
            case R.id.tv_1:
                p.onClick(1, "");
                break;
            case R.id.tv_2:
                p.onClick(2, "");
                break;
            case R.id.tv_3:
                p.onClick(3, "");
                break;
            case R.id.tv_4:
                p.onClick(4, "");
                break;
            case R.id.tv_5:
                p.onClick(5, "");
                break;
            case R.id.tv_6:
                p.onClick(6, "");
                break;
            case R.id.tv_7:
                p.onClick(7, "");
                break;
            case R.id.tv_8:
                p.onClick(8, "");
                break;
            case R.id.tv_9:
                p.onClick(9, "");
                break;
            case R.id.iv_delete:
                p.onClick(10, "delete");
                break;
            case R.id.iv_finger:
                p.onClick(11, "finger");
                break;
        }
    }

    /* 设置小圆点指示器选中的个数 */
    public void setChosen(int num) {
        cv.setChosen(num);
    }

    @Override
    protected int getNavigationBarColor() {
        return R.color.logoRed;
    }
}
