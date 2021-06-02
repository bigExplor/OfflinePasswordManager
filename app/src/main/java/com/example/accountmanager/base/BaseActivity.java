package com.example.accountmanager.base;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accountmanager.R;
import com.example.accountmanager.activities.presenter.BaseActivityPresenter;
import com.example.accountmanager.ui.LoadingDialog;
import com.example.accountmanager.utils.BiometricUtil;
import com.example.accountmanager.utils.LogUtil;
import com.example.accountmanager.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;

import java.util.Objects;

@SuppressWarnings("rawtypes")
public abstract class BaseActivity<P extends BaseActivityPresenter> extends AppCompatActivity {
    private ToastUtil toastUtil;
    private ClipboardManager cm;

    private LoadingDialog loadingDialog;

    protected P p;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        toastUtil = new ToastUtil(this);

        // 去掉ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // 绑定 presenter
        p = getPresenter();
        if (p != null) p.bindView(this);

        initView();
        initListener();
        initData();

        // 变色龙配置
        ImmersionBar.with(this).statusBarColor(getStatusColor()).fitsSystemWindows(true).init();

        // 设置底部虚拟按键的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(getNavigationBarColor()));
        }
    }

    protected void initView() { }
    protected void initData() { }
    protected void initListener() { }
    protected abstract P getPresenter();

    protected int getStatusColor() {
        return R.color.logoRed;
    }

    protected int getNavigationBarColor() {
        return R.color.white;
    }

    protected abstract int getLayoutId();

    public void showToast(String msg) {
        toastUtil.showToast(msg);
    }

    public void log_d(String msg) {
        LogUtil.getInstance().d(getClass().getSimpleName() + ":CharlesLu::", msg);
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public void log_e(String msg) {
        LogUtil.getInstance().e(getClass().getSimpleName() + ":CharlesLu::", msg);
    }

    public void showItemListDialog(String title, final String []items, final OnItemSelectedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setItems(items, (dialog, which) -> listener.onResult(items[which], which)).create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showConfirm(CharSequence msg, final OnItemClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("系统提示：");
        builder.setMessage(msg);
        builder.setCancelable(true);

        builder.setPositiveButton("确定", (dialog, which) -> {
            listener.onResult(true);
            dialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            listener.onResult(false);
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showTip(CharSequence title, CharSequence msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(true);

        builder.setPositiveButton("确定", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showLoading(boolean cancelable) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, cancelable);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public void addOnSoftKeyBoardVisibleListener(OnKeyBoardStateChangedListener listener) {
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            boolean isKeyBoardOpen = (double) (rect.bottom - rect.top) / decorView.getHeight() < 0.8;
            listener.onChange(isKeyBoardOpen);
        });
    }

    public void showBiometric(OnFingerResultListener listener) {
        BiometricUtil.getInstance(this).isSupportBiometric(code -> {
            switch (code) {
                case BiometricUtil.OnResultListener.SUPPORT_SUCCESS:
                    log_d("开始指纹识别");
                    BiometricUtil.getInstance(BaseActivity.this).showBiometricPrompt(code1 -> {
                        switch (code1) {
                            case BiometricUtil.OnResultListener.AUTH_FAIL:
                            case BiometricUtil.OnResultListener.AUTH_ERROR:
                                listener.onResult(false);
                                showToast("验证失败");
                                break;
                            case BiometricUtil.OnResultListener.AUTH_SUCCESS:
                                listener.onResult(true);
                                showToast("验证通过");
                                break;
                        }
                    });
                    break;
                case BiometricUtil.OnResultListener.SUPPORT_NO_HARDWARE:
                    listener.onResult(false);
                    showToast("手机不支持指纹识别");
                    break;
                case BiometricUtil.OnResultListener.SUPPORT_UNAVAILABLE:
                    listener.onResult(false);
                    showToast("当前指纹识别不可用");
                    break;
                case BiometricUtil.OnResultListener.SUPPORT_NONE_ENROLLED:
                    listener.onResult(false);
                    showToast("需先在手机添加指纹数据");
                    break;
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void copy(String msg) {
        if (cm == null) {
            cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }
        cm.setText(msg);
    }

    public interface OnItemClickListener {
        void onResult(boolean isOk);
    }

    public interface OnItemSelectedListener {
        void onResult(String item, int position);
    }

    public interface OnKeyBoardStateChangedListener {
        void onChange(boolean isOpen);
    }

    public interface OnFingerResultListener {
        void onResult(boolean isSuccess);
    }
}
