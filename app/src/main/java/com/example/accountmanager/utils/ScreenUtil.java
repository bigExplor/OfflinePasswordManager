package com.example.accountmanager.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ScreenUtil {
    private static float scale = 0;
    private static final Point point = new Point();

    /* dp转px */
    public static int dp2px(Context context, float dpValue) {
        if (scale == 0) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dpValue * scale + 0.5f);
    }

    /* px转dp */
    public static int px2dp(Context context, float pxValue) {
        if (scale == 0) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (pxValue / scale + 0.5f);
    }

    /* 获取手机屏幕的宽高 */
    public static Point getScreenSize(Context context) {
        Display display = context.getDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        point.x = dm.widthPixels;
        point.y = dm.heightPixels;
        return point;
    }

    /* 隐藏软键盘 */
    public static void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    /* 呼出软键盘 */
    public static void showKeyboard(View view){
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
}
