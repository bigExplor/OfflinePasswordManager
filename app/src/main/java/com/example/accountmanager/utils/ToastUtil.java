package com.example.accountmanager.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private Context mContext;
    private Toast mToast;

    public ToastUtil(Context context) {
        this.mContext = context;
    }

    public void showToast(String msg) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        mToast.setText(msg);
        mToast.show();
    }
}
