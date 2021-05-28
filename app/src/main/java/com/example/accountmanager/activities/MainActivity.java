package com.example.accountmanager.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.fragment.app.FragmentTransaction;

import com.example.accountmanager.R;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.fragments.ChestFragment;
import com.example.accountmanager.fragments.HomeFragment;
import com.example.accountmanager.fragments.MyFragment;
import com.example.accountmanager.presenter.MainActivityPresenter;
import com.example.accountmanager.ui.TitleBar;
import com.example.accountmanager.utils.TimeUtil;

/**
 * @author CharlesLu
 * @description 首页
 */
public class MainActivity extends BaseActivity<MainActivityPresenter> implements CompoundButton.OnCheckedChangeListener {

    private TitleBar titleBar;
    private RadioButton rb_home;
    private RadioButton rb_my;
    private RadioButton rb_chest;

    private MyFragment myFragment;
    private HomeFragment homeFragment;
    private ChestFragment chestFragment;

    private boolean isBack = false;
    private final Handler handler = new Handler();

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            rb_my.setChecked(false);
            rb_my.setTextColor(getResources().getColor(R.color.grey_no_choose));
            rb_home.setChecked(false);
            rb_home.setTextColor(getResources().getColor(R.color.grey_no_choose));
            rb_chest.setChecked(false);
            rb_chest.setTextColor(getResources().getColor(R.color.grey_no_choose));
            rb.setChecked(true);
            rb.setTextColor(getResources().getColor(R.color.logoRed));
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainActivityPresenter getPresenter() {
        return new MainActivityPresenter();
    }

    public MainActivityPresenter getP() {
        return p;
    }

    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        rb_my = findViewById(R.id.rb_my);
        rb_home = findViewById(R.id.rb_home);
        rb_chest = findViewById(R.id.rb_chest);
    }

    @Override
    protected void initListener() {
        rb_my.setOnClickListener(listener);
        rb_home.setOnClickListener(listener);
        rb_chest.setOnClickListener(listener);

        rb_my.setOnCheckedChangeListener(this);
        rb_home.setOnCheckedChangeListener(this);
        rb_chest.setOnCheckedChangeListener(this);

        titleBar.setRight(R.drawable.add, v -> {
            Intent intent = new Intent(MainActivity.this, AddTypeActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void initData() {
        titleBar.setText("首页");
        resizeDrawable(rb_my);
        resizeDrawable(rb_home);
        resizeDrawable(rb_chest);
        rb_home.setChecked(true);
        rb_home.setTextColor(getResources().getColor(R.color.logoRed));
        switchFragment(HomeFragment.class.getSimpleName());
    }

    /* 设置tab栏图片的大小 */
    private void resizeDrawable(RadioButton rb) {
        Drawable[] drawable = rb.getCompoundDrawables();
        drawable[1].setBounds(0, 0, 65, 65);
        rb.setCompoundDrawables(drawable[0], drawable[1], drawable[2], drawable[3]);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    /* 点击tab切换界面 */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_my:
                    titleBar.setRightVisibility(false);
                    switchFragment(MyFragment.class.getSimpleName());
                    break;
                case R.id.rb_home:
                    titleBar.setRightVisibility(true);
                    switchFragment(HomeFragment.class.getSimpleName());
                    break;
                case R.id.rb_chest:
                    titleBar.setRightVisibility(false);
                    switchFragment(ChestFragment.class.getSimpleName());
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myFragment != null) myFragment.onShow();
        if (homeFragment != null) homeFragment.onShow();
        if (chestFragment != null) chestFragment.onShow();
    }

    @Override
    public void onBackPressed() {
        if (isBack) {
            finish();
            return;
        }
        isBack = true;
        showToast("再次点击退出应用");
        TimeUtil.getInstance().postDelayed(() -> {
            isBack = false;
        }, 1500);
    }

    /* 切换 fragment */
    private void switchFragment(String name) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if (MyFragment.class.getSimpleName().equals(name)) {
            if (myFragment == null) {
                myFragment = new MyFragment(this);
                transaction.add(R.id.fl_content, myFragment);
            }
            titleBar.setText("我的");
            myFragment.onShow();
            transaction.show(myFragment);
        }
        else if (HomeFragment.class.getSimpleName().equals(name)) {
            if (homeFragment == null) {
                homeFragment = new HomeFragment(this);
                transaction.add(R.id.fl_content, homeFragment);
            }
            titleBar.setText("首页");
            homeFragment.onShow();
            transaction.show(homeFragment);
        }
        else if (ChestFragment.class.getSimpleName().equals(name)) {
            if (chestFragment == null) {
                chestFragment = new ChestFragment(this);
                transaction.add(R.id.fl_content, chestFragment);
            }
            titleBar.setText("工具箱");
            chestFragment.onShow();
            transaction.show(chestFragment);
        }
        transaction.commit();
    }

    /* 隐藏所有fragment */
    private void hideAllFragment(FragmentTransaction transaction) {
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (chestFragment != null) {
            transaction.hide(chestFragment);
        }
    }
}
