package com.example.accountmanager.fragments;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.accountmanager.R;
import com.example.accountmanager.activities.MainActivity;
import com.example.accountmanager.base.BaseFragment;
import com.example.accountmanager.fragments.presenter.ChestFragmentPresenter;

public class ChestFragment extends BaseFragment<ChestFragmentPresenter> {
    private EditText et_key;
    private Button btn_run;
    public final MainActivity mActivity;

    public ChestFragment(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    protected ChestFragmentPresenter getPresenter() {
        return new ChestFragmentPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_chest;
    }

    protected void initView() {
        et_key = view.findViewById(R.id.et_key);
        btn_run = view.findViewById(R.id.btn_run);
    }

    protected void initListener() {
        btn_run.setOnClickListener(v -> {
            String key = et_key.getText().toString();
            if (TextUtils.isEmpty(key)) {
                mActivity.showToast("密钥错误！");
                return;
            }
            boolean success = p.parse(key);
            if (success) et_key.setText("");
        });
    }
}
