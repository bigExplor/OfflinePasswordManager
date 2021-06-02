package com.example.accountmanager.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.accountmanager.R;
import com.example.accountmanager.activities.MainActivity;
import com.example.accountmanager.activities.SetPasswordActivity;
import com.example.accountmanager.base.BaseFragment;
import com.example.accountmanager.fragments.presenter.MyFragmentPresenter;
import com.example.accountmanager.utils.SpUtil;

public class MyFragment extends BaseFragment<MyFragmentPresenter> implements View.OnClickListener {
    private LinearLayout llPwd;
    private LinearLayout llFingerPwd;
    private LinearLayout llPrivate;

    private ImageView iv_cpy_str;
    private ImageView iv_cpy_file;
    private ImageView iv_why_str;
    private ImageView iv_why_file;
    private ImageView ivFingerChoose;
    private ImageView ivPrivateChoose;

    private boolean hasInit = false;
    public final MainActivity mActivity;

    public MyFragment(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_my;
    }

    @Override
    protected MyFragmentPresenter getPresenter() {
        return new MyFragmentPresenter();
    }

    protected void initView() {
        llPwd = view.findViewById(R.id.ll_pwd);
        llPrivate = view.findViewById(R.id.ll_private);
        llFingerPwd = view.findViewById(R.id.ll_finger_pwd);
        iv_cpy_str = view.findViewById(R.id.iv_cpy_str);
        iv_cpy_file = view.findViewById(R.id.iv_cpy_file);
        iv_why_str = view.findViewById(R.id.iv_why_str);
        iv_why_file = view.findViewById(R.id.iv_why_file);
        ivFingerChoose = view.findViewById(R.id.iv_finger_choose);
        ivPrivateChoose = view.findViewById(R.id.iv_private_choose);

        hasInit = true;
    }

    protected void initListener() {
        llPwd.setOnClickListener(this);
        llPrivate.setOnClickListener(this);
        llFingerPwd.setOnClickListener(this);
        iv_cpy_str.setOnClickListener(this);
        iv_cpy_file.setOnClickListener(this);
        iv_why_str.setOnClickListener(this);
        iv_why_file.setOnClickListener(this);

        setChooseFinger();
        setChoosePrivate();
    }

    public void setChooseFinger() {
        if (SpUtil.getInstance().getBoolean("finger")) {
            ivFingerChoose.setImageResource(R.drawable.chosen);
        } else {
            ivFingerChoose.setImageResource(R.drawable.no_chosen);
        }
    }

    public void setChoosePrivate() {
        if (SpUtil.getInstance().getBoolean("privateSpace")) {
            ivPrivateChoose.setImageResource(R.drawable.chosen);
        } else {
            ivPrivateChoose.setImageResource(R.drawable.no_chosen);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        SpannableString msg;
        switch (v.getId()) {
            case R.id.ll_pwd:
                Intent intent = new Intent(mActivity, SetPasswordActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.ll_finger_pwd:
                p.openFingerAuth();
                break;
            case R.id.ll_private:
                p.openPrivateSpace();
                break;
            case R.id.iv_cpy_str:
                mActivity.getP().cpyStr();
                break;
            case R.id.iv_cpy_file:
                mActivity.getP().cpyFile();
                break;
            case R.id.iv_why_str:
                msg = new SpannableString("仅当数据不超过 5 条时，字符备份才会起作用。大批量数据的备份请使用 文件备份");
                msg.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.logoRed)), 8, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                msg.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.work)), 35, 39, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mActivity.showTip("小贴士", msg);
                break;
            case R.id.iv_why_file:
                msg = new SpannableString("文件备份支持使用本地数据生成备份文件并分享的功能。数据恢复的入口在 工具箱 中。");
                msg.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.work)), 34, 37, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mActivity.showTip("小贴士", msg);
                break;
        }
    }

    public void onShow() {
        if (hasInit) {
            setChooseFinger();
            setChoosePrivate();
        }
    }
}
