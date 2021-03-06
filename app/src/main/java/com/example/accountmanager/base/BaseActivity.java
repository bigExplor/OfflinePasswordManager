package com.example.accountmanager.base;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accountmanager.R;
import com.example.accountmanager.presenter.BasePresenter;
import com.example.accountmanager.ui.LoadingDialog;
import com.example.accountmanager.utils.BiometricUtil;
import com.example.accountmanager.utils.LogUtil;
import com.example.accountmanager.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    private ToastUtil toastUtil;
    private ClipboardManager cm;

    private boolean cancelable;
    private LoadingDialog loadingDialog;

    protected P p;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        toastUtil = new ToastUtil(this);

        // 去掉ActionBar
        getSupportActionBar().hide();

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

    public void log_e(String msg) {
        LogUtil.getInstance().e(getClass().getSimpleName() + ":CharlesLu::", msg);
    }

    public void showItemListDialog(String title, final String []items, final OnItemSelectedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onResult(items[which], which);
            }
        }).create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showConfirm(CharSequence msg, final OnItemClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("系统提示：");
        builder.setMessage(msg);
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onResult(true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onResult(false);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showTip(CharSequence title, CharSequence msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(true);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showLoading(boolean cancelable) {
        if (loadingDialog == null || this.cancelable != cancelable) {
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

    public void addOnSoftKeyBoardVisibleListener(OnKeyBoardStateChangedListener listener) {
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                boolean isKeyBoardOpen = (double) (rect.bottom - rect.top) / decorView.getHeight() < 0.8;
                if (isKeyBoardOpen) {
                    listener.onChange(true);
                } else {
                    listener.onChange(false);
                }
            }
        });
    }

    public void showBiometric(OnFingerResultListener listener) {
        BiometricUtil.getInstance(this).isSupportBiometric(new BiometricUtil.OnResultListener() {
            @Override
            public void onResult(int code) {
                switch (code) {
                    case BiometricUtil.OnResultListener.SUPPORT_SUCCESS:
                        log_d("开始指纹识别");
                        BiometricUtil.getInstance(BaseActivity.this).showBiometricPrompt(new BiometricUtil.OnResultListener() {
                            @Override
                            public void onResult(int code) {
                                switch (code) {
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
            }
        });
    }

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
        void onResult(String item, int positon);
    }

    public interface OnKeyBoardStateChangedListener {
        void onChange(boolean isOpen);
    }

    public interface OnFingerResultListener {
        void onResult(boolean isSuccess);
    }
}
