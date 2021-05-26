package com.example.accountmanager.activities;

import android.annotation.SuppressLint;
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
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.dao.TypeDao;
import com.example.accountmanager.ui.TitleBar;

import java.util.List;

/**
 * @author CharlesLu
 * @description 添加账号的界面
 */
public class AddAccountActivity extends BaseActivity implements View.OnFocusChangeListener {

    private TitleBar titleBar;
    private EditText editTitle;
    private EditText etUsername;
    private EditText etMail;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etUrl;
    private EditText etNote;

    private TextView tvAccount;
    private TextView tvType;

    private String account_type = "用户名";

    private final TypeDao typeDao = new TypeDao();
    private final AccountDao accountDao = new AccountDao();
    private int selectedTypeId = -1;

    private List<Type> list;

    private int typeId;
    private String from;
    private int accountId;
    private Account account;
    private LinearLayout llContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_account;
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
        llContainer = findViewById(R.id.ll_container);
    }

    @SuppressLint("SetTextI18n")
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
        tvAccount.setOnClickListener(v -> showItemListDialog("绑定账号", new String[]{"用户名", "邮箱", "电话"}, (item, position) -> {
            account_type = item;
            tvAccount.setText("与" + account_type + "绑定");
        }));
        etPassword.setOnFocusChangeListener(this);
        etUrl.setOnFocusChangeListener(this);
        etNote.setOnFocusChangeListener(this);
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
            tvType.setVisibility(View.VISIBLE);
            Type t = typeDao.getTypeById(typeId);
            if (t == null) {
                finish();
                return;
            }
            tvType.setText(t.getName());
            account = accountDao.getAccountById(accountId);
            fillData();
            list = typeDao.getAllType();
            tvType.setOnClickListener(v -> {
                String []typeName = new String[list.size()];
                for (int i = 0; i < typeName.length; ++i) typeName[i] = list.get(i).getName();
                showItemListDialog("选择分类", typeName, (item, position) -> {
                    account.setTypeId(list.get(position).getId());
                    tvType.setText(item);
                });
            });
        }
    }

    private void check() {
        if (account == null) account = new Account();
        String title = editTitle.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String mail = etMail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String url = etUrl.getText().toString().trim();
        String note = etNote.getText().toString().trim();
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
        editTitle.setText(account.getTitle());
        if (!"暂无提供".equals(account.getUsername())) {
            etUsername.setText(account.getUsername());
        }
        if (!"暂无提供".equals(account.getMail())) {
            etMail.setText(account.getMail());
        }
        if (!"暂无提供".equals(account.getPhone())) {
            etPhone.setText(account.getPhone());
        }
        etPassword.setText(account.getPassword());
        if (!"暂无提供".equals(account.getUrl())) {
            etUrl.setText(account.getUrl());
        }
        if (!"暂无提供".equals(account.getNote())) {
            etNote.setText(account.getNote());
        }
        if (account.getAccount().equals(account.getMail())) {
            tvAccount.setText("与邮箱绑定");
            account_type = "邮箱";
        } else if (account.getAccount().equals(account.getPhone())) {
            tvAccount.setText("与电话绑定");
            account_type = "电话";
        } else {
            tvAccount.setText("与用户名绑定");
            account_type = "用户名";
        }
    }
}
