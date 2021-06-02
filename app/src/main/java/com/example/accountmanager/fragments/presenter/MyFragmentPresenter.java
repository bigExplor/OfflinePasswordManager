package com.example.accountmanager.fragments.presenter;

import android.text.TextUtils;
import android.view.View;

import com.example.accountmanager.fragments.MyFragment;
import com.example.accountmanager.utils.BiometricUtil;
import com.example.accountmanager.utils.SpUtil;

/**
 * @author CharlesLu
 * @date 2021/6/2 17:31
 * @description
 */
public class MyFragmentPresenter implements BaseFragmentPresenter<MyFragment> {
    protected MyFragment view;

    @Override
    public void bindView(MyFragment view) {
        this.view = view;
    }

    public void openPrivateSpace() {
        if (TextUtils.isEmpty(SpUtil.getInstance().getString("password"))) {
            view.mActivity.showToast("需先设置二级密码");
            return;
        }
        boolean privateState = SpUtil.getInstance().getBoolean("privateSpace");
        SpUtil.getInstance().putBoolean("privateSpace", !privateState);
        view.setChoosePrivate();
    }

    public void openFingerAuth() {
        if (TextUtils.isEmpty(SpUtil.getInstance().getString("password"))) {
            view.mActivity.showToast("需先设置二级密码");
            return;
        }
        BiometricUtil.getInstance(view.mActivity).isSupportBiometric(code -> {
            switch (code) {
                case BiometricUtil.OnResultListener.SUPPORT_SUCCESS:
                    boolean finger = SpUtil.getInstance().getBoolean("finger");
                    SpUtil.getInstance().putBoolean("finger", !finger);
                    view.setChooseFinger();
                    break;
                case BiometricUtil.OnResultListener.SUPPORT_NO_HARDWARE:
                    view.mActivity.showToast("手机不支持指纹识别");
                    break;
                case BiometricUtil.OnResultListener.SUPPORT_UNAVAILABLE:
                    view.mActivity.showToast("当前指纹识别不可用");
                    break;
                case BiometricUtil.OnResultListener.SUPPORT_NONE_ENROLLED:
                    view.mActivity.showToast("需先在手机添加指纹数据");
                    break;
            }
        });
    }
}
