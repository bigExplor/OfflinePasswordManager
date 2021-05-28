package com.example.accountmanager.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class BiometricUtil {
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static BiometricUtil biometricUtil;

    private BiometricUtil(Context context) {
        this.context = context;
    }
    public static BiometricUtil getInstance(FragmentActivity activity) {
        if (biometricUtil == null) {
            biometricUtil = new BiometricUtil(activity);
        }
        return biometricUtil;
    }

    public void isSupportBiometric(OnResultListener listener) {
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                if (listener != null) {
                    listener.onResult(OnResultListener.SUPPORT_SUCCESS);
                }
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                if (listener != null) {
                    listener.onResult(OnResultListener.SUPPORT_NO_HARDWARE);
                }
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                if (listener != null) {
                    listener.onResult(OnResultListener.SUPPORT_UNAVAILABLE);
                }
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                if (listener != null) {
                    listener.onResult(OnResultListener.SUPPORT_NONE_ENROLLED);
                }
                break;
        }
    }

    private final Handler handler = new Handler();

    private final Executor executor = handler::post;

    public void showBiometricPrompt(final OnResultListener listener) {
        BiometricPrompt.PromptInfo promptInfo =
                new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Biometric login for my app") //设置大标题
                        .setSubtitle("Log in using your biometric credential") // 设置标题下的提示
                        .setNegativeButtonText("取消") //设置取消按钮
                        .build();

        //需要提供的参数callback
        BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context,
                executor, new BiometricPrompt.AuthenticationCallback() {
            //各种异常的回调
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (listener != null) {
                    listener.onResult(OnResultListener.AUTH_ERROR);
                }
            }

            //认证成功的回调
            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (listener != null) {
                    listener.onResult(OnResultListener.AUTH_SUCCESS);
                }
            }

            //认证失败的回调
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                if (listener != null) {
                    listener.onResult(OnResultListener.AUTH_FAIL);
                }
            }
        });

        // 显示认证对话框
        biometricPrompt.authenticate(promptInfo);
    }

    public interface OnResultListener {
        // 设备可以使用生物识别
        int SUPPORT_SUCCESS = 0;

        // 设备硬件不支持生物识别
        int SUPPORT_NO_HARDWARE = 1;

        // 生物识别当前不可用
        int SUPPORT_UNAVAILABLE = 2;

        // 用户没有录入生物识别数据
        int SUPPORT_NONE_ENROLLED = 3;

        // 验证失败
        int AUTH_FAIL = 4;

        // 验证出错
        int AUTH_ERROR = 5;

        // 验证成功
        int AUTH_SUCCESS = 6;

        void onResult(int code);
    }
}
