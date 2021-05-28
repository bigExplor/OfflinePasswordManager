package com.example.accountmanager.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.accountmanager.R;
import com.example.accountmanager.utils.ScreenUtil;

public class LoadingDialog extends Dialog {
    private View view;
    private final Context context;
    private final LayoutInflater layoutInflater;
    private TextView tv_loadingTip;

    public LoadingDialog(@NonNull Context context, boolean cancelable) {
        super(context);
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        initView();
        resizeWindow();
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelable);
    }

    @SuppressLint("InflateParams")
    private void initView() {
        view = layoutInflater.inflate(R.layout.layout_loading_dialog, null);
        ImageView iv_loading = view.findViewById(R.id.iv_loading);
        tv_loadingTip = view.findViewById(R.id.tv_loadingTip);
        Glide.with(context).load(R.drawable.loading).into(iv_loading);
    }

    private void resizeWindow() {
        Window dialogWindow = this.getWindow();
        if(dialogWindow == null) {
            return;
        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 50);
        lp.width = ScreenUtil.dp2px(context, 250);
        lp.height = ScreenUtil.dp2px(context, 190) + 50;
        dialogWindow.setAttributes(lp);
        // 将布局与dialog绑定
        addContentView(view, new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setText(String txt) {
        tv_loadingTip.setText(txt);
    }
}
