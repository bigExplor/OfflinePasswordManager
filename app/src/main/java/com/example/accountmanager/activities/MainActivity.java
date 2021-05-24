package com.example.accountmanager.activities;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.fragment.app.FragmentTransaction;

import com.example.accountmanager.R;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.fragments.ChestFragment;
import com.example.accountmanager.fragments.HomeFragment;
import com.example.accountmanager.fragments.MyFragment;
import com.example.accountmanager.ui.TitleBar;
import com.example.accountmanager.utils.FileUtil;
import com.example.accountmanager.utils.StringUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private TitleBar titleBar;
    private RadioButton rb_home;
    private RadioButton rb_my;
    private RadioButton rb_chest;

    private MyFragment myFragment;
    private HomeFragment homeFragment;
    private ChestFragment chestFragment;

    private AccountDao accountDao = new AccountDao();

    private boolean isBack = false;
    private Handler handler = new Handler();

    private Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getName().equals("type");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).create();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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

    private View.OnClickListener listener = new View.OnClickListener() {
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

    private void resizeDrawable(RadioButton rb) {
        Drawable[] drawable = rb.getCompoundDrawables();
        drawable[1].setBounds(0, 0, 65, 65);
        rb.setCompoundDrawables(drawable[0], drawable[1], drawable[2], drawable[3]);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_my:
                    switchFragment(MyFragment.class.getSimpleName());
                    break;
                case R.id.rb_home:
                    switchFragment(HomeFragment.class.getSimpleName());
                    break;
                case R.id.rb_chest:
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isBack = false;
            }
        }, 1500);
    }

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

    public void cpyStr() {
        List<Account> accountList = accountDao.getAllAccounts();
        if (accountList.size() == 0) {
            showToast("请先添加账号信息");
            return;
        }
//        if (accountList.size() > 5) {
//            showToast("账号条目过多，请使用文件备份");
//            return;
//        }
        copy(StringUtil.encode(gson.toJson(accountList)));
        showToast("备份字符串复制成功！");
    }

    public void cpyFile() {
        List<Account> accountList = accountDao.getAllAccounts();
        if (accountList.size() == 0) {
            showToast("请先添加账号信息");
            return;
        }
        boolean success = false;
        String filePath = "";
        try {
            filePath = FileUtil.getFilePath(this, "accounts.txt");
            success = FileUtil.writeToFile(filePath, StringUtil.encode(gson.toJson(accountList)), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!success) {
            showToast("备份失败");
            return;
        }
        showToast(FileUtil.share(this, filePath));
    }

    public boolean parse(String key) {
        List<Account> accountList = null;
        try {
            accountList = gson.fromJson(StringUtil.decode(key), new TypeToken<List<Account>>(){}.getType());
        } catch (Exception e) {
            showToast("密钥错误");
            return false;
        }
        showLoading(false);
        for (Account account: accountList) {
            account.removeInfo();
            accountDao.addAccount(account);
        }
        hideLoading();
        showToast("导入完成");
        return true;
    }
}
