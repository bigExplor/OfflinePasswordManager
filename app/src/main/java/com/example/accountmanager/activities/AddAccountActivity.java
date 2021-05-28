package com.example.accountmanager.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.accountmanager.R;
import com.example.accountmanager.base.BaseActivity1;
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.presenter.AddAccountActivityPresenter;
import com.example.accountmanager.ui.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CharlesLu
 * @description 添加账号的界面
 */
public class AddAccountActivity extends BaseActivity1<AddAccountActivityPresenter> {

    private TitleBar titleBar;
    private EditText editTitle;
    private EditText etUsername;
    private EditText etMail;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etUrl;
    private EditText etNote;

    private TextView tvType;
    private TextView tvAccount;

    private int typeId;
    private int accountId;

    private String from;
    public String account_type = "用户名";

    public Type currentType = new Type();
    public Account currentAccount = new Account();

    public String []typeName;
    public List<Type> allTypeList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_account;
    }

    @Override
    protected AddAccountActivityPresenter getPresenter() {
        return new AddAccountActivityPresenter();
    }

    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        editTitle = findViewById(R.id.et_title);
        etUsername = findViewById(R.id.et_username);
        etMail = findViewById(R.id.et_mail);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etUrl = findViewById(R.id.et_url);
        etNote = findViewById(R.id.et_note);
        tvType = findViewById(R.id.tv_type);
        tvAccount = findViewById(R.id.tv_account);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initListener() {
        from = getIntent().getStringExtra("from");
        typeId = getIntent().getIntExtra("typeId", -1);
        accountId = getIntent().getIntExtra("accountId", -1);
        tvAccount.setOnClickListener(v -> showItemListDialog("绑定账号", new String[]{"用户名", "邮箱", "电话"}, (item, position) -> {
            account_type = item;
            tvAccount.setText("与" + account_type + "绑定");
        }));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
        String title = "add".equals(from)? "添加账号信息": "修改账号信息";
        titleBar.setText(title);
        titleBar.setZ(100f);
        titleBar.setLeft(R.drawable.left, v -> finish());
        titleBar.setRight(R.drawable.save, v -> check());
        p.getTypeAndAccount(typeId, accountId);
        if (currentType == null || ("update".equals(from) && currentAccount.getId() <= 0)) {
            showToast("获取分类信息失败！");
            finish();
            return;
        } else {
            currentAccount.setType(currentType);
            currentAccount.setTypeId(currentType.getId());
        }
        if ("update".equals(from)) {
            fillData();
            tvType.setVisibility(View.VISIBLE);
            tvType.setText(currentType.getName());
            tvType.setOnClickListener(v -> showItemListDialog("选择分类", typeName, (item, position) -> {
                currentAccount.setTypeId(allTypeList.get(position).getId());
                tvType.setText(item);
            }));
        }
    }

    private void check() {
        currentAccount.setUrl(etUrl.getText().toString().trim());
        currentAccount.setMail(etMail.getText().toString().trim());
        currentAccount.setNote(etNote.getText().toString().trim());
        currentAccount.setPhone(etPhone.getText().toString().trim());
        currentAccount.setTitle(editTitle.getText().toString().trim());
        currentAccount.setUsername(etUsername.getText().toString().trim());
        currentAccount.setPassword(etPassword.getText().toString().trim());
        p.check();
    }

    /* 填充初始账号信息 */
    private void fillData() {
        currentAccount.removeInfo();
        editTitle.setText(currentAccount.getTitle());
        etPassword.setText(currentAccount.getPassword());

        if (!TextUtils.isEmpty(currentAccount.getUrl())) {
            etUrl.setText(currentAccount.getUrl());
        }
        if (!TextUtils.isEmpty(currentAccount.getNote())) {
            etNote.setText(currentAccount.getNote());
        }
        if (!TextUtils.isEmpty(currentAccount.getMail())) {
            etMail.setText(currentAccount.getMail());
        }
        if (!TextUtils.isEmpty(currentAccount.getPhone())) {
            etPhone.setText(currentAccount.getPhone());
        }
        if (!TextUtils.isEmpty(currentAccount.getUsername())) {
            etUsername.setText(currentAccount.getUsername());
        }
        if (currentAccount.getAccount().equals(currentAccount.getMail())) {
            tvAccount.setText("与邮箱绑定");
            account_type = "邮箱";
        } else if (currentAccount.getAccount().equals(currentAccount.getPhone())) {
            tvAccount.setText("与电话绑定");
            account_type = "电话";
        } else {
            tvAccount.setText("与用户名绑定");
            account_type = "用户名";
        }
    }
}
