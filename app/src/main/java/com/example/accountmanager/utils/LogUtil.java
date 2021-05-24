package com.example.accountmanager.utils;

import android.util.Log;

public class LogUtil {
    private boolean showLog = true;
    private static LogUtil logUtil;

    private LogUtil() { }

    public static LogUtil getInstance() {
        if (logUtil == null) {
            logUtil = new LogUtil();
        }
        return logUtil;
    }

    public void d(String tag, String msg) {
        if (!showLog) return;
        Log.d(tag, msg);
    }

    public void e(String tag, String msg) {
        if (!showLog) return;
        Log.e(tag, msg);
    }
}
