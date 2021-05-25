package com.example.accountmanager.activities;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.accountmanager.R;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.ui.TitleBar;
import com.example.accountmanager.utils.ScreenUtil;

/**
 * @author CharlesLu
 * @description 添加账号的界面
 */
public class AddAccountActivity extends BaseActivity implements View.OnFocusChangeListener {

    private TitleBar titleBar;
    private EditText et_title;
    private EditText et_username;
    private EditText et_mail;
    private EditText et_phone;
    private TextView tv_account;
    private EditText et_password;
    private EditText et_url;
    private EditText et_note;

    private String account_type = "用户名";

    private AccountDao accountDao = new AccountDao();
    private int typeId;
    private String from;
    private int accountId;
    private Account account;
    private LinearLayout ll_container;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_account;
    }

    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        et_title = findViewById(R.id.et_title);
        et_username = findViewById(R.id.et_username);
        et_mail = findViewById(R.id.et_mail);
        et_phone = findViewById(R.id.et_phone);
        tv_account = findViewById(R.id.tv_account);
        et_password = findViewById(R.id.et_password);
        et_url = findViewById(R.id.et_url);
        et_note = findViewById(R.id.et_note);
        ll_container = findViewById(R.id.ll_container);
    }

    @Override
    protected void initListener() {
        from = getIntent().getStringExtra("from");
        typeId = getIntent().getIntExtra("typeId", -1);
        accountId = getIntent().getIntExtra("accountId", -1);
        if (("add".equals(from) && typeId <= 0) || ("update".equals(from) && accountId <= 0)) {
            showToast("获取分类信息失败");
            finish();
            return;
        }
        tv_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemListDialog("绑定账号", new String[]{"用户名", "邮箱", "电话"}, new OnItemSelectedListener() {
                    @Override
                    public void onResult(String item) {
                        account_type = item;
                        tv_account.setText("与" + account_type + "绑定");
                    }
                });
            }
        });
        et_password.setOnFocusChangeListener(this);
        et_url.setOnFocusChangeListener(this);
        et_note.setOnFocusChangeListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
        String title = "add".equals(from)? "添加账号信息": "修改账号信息";
        titleBar.setText(title);
        titleBar.setZ(100f);
        titleBar.setLeft(R.drawable.left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setRight(R.drawable.save, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
        if ("update".equals(from)) {
            account = accountDao.getAccountById(accountId);
            fillData();
        }
    }

    private void check() {
        if (account == null) account = new Account();
        String title = et_title.getText().toString().trim();
        String username = et_username.getText().toString().trim();
        String mail = et_mail.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String url = et_url.getText().toString().trim();
        String note = et_note.getText().toString().trim();
        if (!TextUtils.isEmpty(title)) {
            account.setTitle(title);
        } else {
            showToast("名称不为空");
            return;
        }
        if (!TextUtils.isEmpty(password)) {
            account.setPassword(password);
        } else {
            showToast("密码不为空");
            return;
        }
        if (TextUtils.isEmpty(url) || URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {
            account.setUrl(url);
        } else {
            showToast("网址格式不正确");
            return;
        }
        if ("add".equals(from))  account.setTypeId(typeId);
        account.setUsername(username);
        account.setMail(mail);
        account.setPhone(phone);
        account.setNote(note);
        if ("用户名".equals(account_type)) {
            if (TextUtils.isEmpty(username)) {
                showToast("与账号绑定的用户名不可为空");
                return;
            }
            account.setAccount(username);
        } else if ("邮箱".equals(account_type)) {
            if (TextUtils.isEmpty(mail)) {
                showToast("与账号绑定的邮箱不可为空");
                return;
            }
            account.setAccount(mail);
        } else {
            if (TextUtils.isEmpty(phone)) {
                showToast("与账号绑定的电话不可为空");
                return;
            }
            account.setAccount(phone);
        }
        if ("add".equals(from)) accountDao.addAccount(account);
        else accountDao.updateAccount(account);
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    private void fillData() {
        et_title.setText(account.getTitle());
        if (!"暂无提供".equals(account.getUsername())) {
            et_username.setText(account.getUsername());
        }
        if (!"暂无提供".equals(account.getMail())) {
            et_mail.setText(account.getMail());
        }
        if (!"暂无提供".equals(account.getPhone())) {
            et_phone.setText(account.getPhone());
        }
        et_password.setText(account.getPassword());
        if (!"暂无提供".equals(account.getUrl())) {
            et_url.setText(account.getUrl());
        }
        if (!"暂无提供".equals(account.getNote())) {
            et_note.setText(account.getNote());
        }
        if (account.getAccount().equals(account.getMail())) {
            tv_account.setText("与邮箱绑定");
            account_type = "邮箱";
        } else if (account.getAccount().equals(account.getPhone())) {
            tv_account.setText("与电话绑定");
            account_type = "电话";
        } else {
            tv_account.setText("与用户名绑定");
            account_type = "用户名";
        }
    }
}
