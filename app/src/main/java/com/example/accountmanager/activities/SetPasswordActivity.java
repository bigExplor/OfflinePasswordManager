package com.example.accountmanager.activities;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.accountmanager.R;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.presenter.SetPasswordActivityPresenter;
import com.example.accountmanager.ui.TitleBar;
import com.example.accountmanager.utils.SpUtil;

/**
 * @author CharlesLu
 * @description 设置二级密码的界面
 */
public class SetPasswordActivity extends BaseActivity<SetPasswordActivityPresenter> implements View.OnClickListener {

    private TitleBar titleBar;
    private EditText et_pwd;
    private EditText et_confirm;
    private Button btn;
    private boolean hasPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_password;
    }

    @Override
    protected SetPasswordActivityPresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        et_pwd = findViewById(R.id.et_pwd);
        et_confirm = findViewById(R.id.et_confirm);
        btn = findViewById(R.id.btn);
    }

    @Override
    protected void initListener() {
        btn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        titleBar.setText("设置二级密码");
        titleBar.setLeft(R.drawable.left, v -> finish());
        hasPwd = !TextUtils.isEmpty(SpUtil.getInstance().getString("password"));
        if (hasPwd) {
            btn.setText("确认关闭");
            btn.setBackground(getResources().getDrawable(R.drawable.shape_btn_nagtive));
        } else {
            btn.setText("确定");
            btn.setBackground(getResources().getDrawable(R.drawable.shape_btn_positive));
        }
    }

    @Override
    public void onClick(View v) {
        String pwd = et_pwd.getText().toString();
        String confirm = et_confirm.getText().toString();
        if (TextUtils.isEmpty(pwd) || pwd.length() < 4) {
            showToast("输入4位密码");
            return;
        }
        if (!pwd.equals(confirm)) {
            showToast("两次密码不匹配");
            return;
        }
        if (hasPwd) {
            SpUtil.getInstance().removeValue("finger");
            SpUtil.getInstance().removeValue("password");
            SpUtil.getInstance().removeValue("privateSpace");
            showToast("关闭成功");
        } else {
            SpUtil.getInstance().putString("password", pwd);
            showToast("开启成功");
        }
        finish();
    }
}
