package com.example.accountmanager.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accountmanager.R;
import com.example.accountmanager.activities.MainActivity;
import com.example.accountmanager.activities.SetPasswordActivity;
import com.example.accountmanager.utils.SpUtil;

public class MyFragment extends Fragment implements View.OnClickListener {
    private MainActivity mActivity;
    private View view;
    private LinearLayout ll_pwd;
    private LinearLayout ll_finger_pwd;

    private ImageView iv_cpy_str;
    private ImageView iv_cpy_file;
    private ImageView iv_choose;
    private boolean hasInit = false;
    private ImageView iv_why_str;
    private ImageView iv_why_file;

    public MyFragment(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_my, container, false);
        initView();
        initListener();
        setChooseFinger();
        return view;
    }

    private void initView() {
        iv_choose = view.findViewById(R.id.iv_choose);
        ll_pwd = view.findViewById(R.id.ll_pwd);
        ll_finger_pwd = view.findViewById(R.id.ll_finger_pwd);
        iv_cpy_str = view.findViewById(R.id.iv_cpy_str);
        iv_cpy_file = view.findViewById(R.id.iv_cpy_file);
        iv_why_str = view.findViewById(R.id.iv_why_str);
        iv_why_file = view.findViewById(R.id.iv_why_file);
        hasInit = true;
    }

    private void initListener() {
        ll_pwd.setOnClickListener(this);
        ll_finger_pwd.setOnClickListener(this);
        iv_cpy_str.setOnClickListener(this);
        iv_cpy_file.setOnClickListener(this);
        iv_why_str.setOnClickListener(this);
        iv_why_file.setOnClickListener(this);
    }

    private void setChooseFinger() {
        if (SpUtil.getInstance().getBoolean("finger")) {
            iv_choose.setImageResource(R.drawable.chosen);
        } else {
            iv_choose.setImageResource(R.drawable.no_chosen);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        SpannableString msg = null;
        switch (v.getId()) {
            case R.id.ll_pwd:
                Intent intent = new Intent(mActivity, SetPasswordActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.ll_finger_pwd:
                if (TextUtils.isEmpty(SpUtil.getInstance().getString("password"))) {
                    mActivity.showToast("????????????????????????");
                    return;
                }
                boolean finger = SpUtil.getInstance().getBoolean("finger");
                SpUtil.getInstance().putBoolean("finger", !finger);
                setChooseFinger();
                if (finger) mActivity.showToast("??????????????????");
                else mActivity.showToast("??????????????????");
                break;
            case R.id.iv_cpy_str:
                mActivity.getP().cpyStr();
                break;
            case R.id.iv_cpy_file:
                mActivity.getP().cpyFile();
                break;
            case R.id.iv_why_str:
                msg = new SpannableString("????????????????????? 5 ???????????????????????????????????????????????????????????????????????? ????????????");
                msg.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.logoRed)), 8, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                msg.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.work)), 35, 39, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mActivity.showTip("?????????", msg);
                break;
            case R.id.iv_why_file:
                msg = new SpannableString("??????????????????????????????????????????????????????????????????????????????????????????????????? ????????? ??????");
                msg.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.work)), 34, 37, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mActivity.showTip("?????????", msg);
                break;
        }
    }

    public void onShow() {
        if (hasInit) {
            setChooseFinger();
        }
    }
}
