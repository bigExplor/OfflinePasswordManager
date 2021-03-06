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
                        .setTitle("Biometric login for my app") //???????????????
                        .setSubtitle("Log in using your biometric credential") // ????????????????????????
                        .setNegativeButtonText("??????") //??????????????????
                        .build();

        //?????????????????????callback
        BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context,
                executor, new BiometricPrompt.AuthenticationCallback() {
            //?????????????????????
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (listener != null) {
                    listener.onResult(OnResultListener.AUTH_ERROR);
                }
            }

            //?????????????????????
            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (listener != null) {
                    listener.onResult(OnResultListener.AUTH_SUCCESS);
                }
            }

            //?????????????????????
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                if (listener != null) {
                    listener.onResult(OnResultListener.AUTH_FAIL);
                }
            }
        });

        // ?????????????????????
        biometricPrompt.authenticate(promptInfo);
    }

    public interface OnResultListener {
        // ??????????????????????????????
        int SUPPORT_SUCCESS = 0;

        // ?????????????????????????????????
        int SUPPORT_NO_HARDWARE = 1;

        // ???????????????????????????
        int SUPPORT_UNAVAILABLE = 2;

        // ????????????????????????????????????
        int SUPPORT_NONE_ENROLLED = 3;

        // ????????????
        int AUTH_FAIL = 4;

        // ????????????
        int AUTH_ERROR = 5;

        // ????????????
        int AUTH_SUCCESS = 6;

        void onResult(int code);
    }
}
