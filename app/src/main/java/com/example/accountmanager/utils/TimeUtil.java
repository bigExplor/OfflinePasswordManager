package com.example.accountmanager.utils;

import android.os.Handler;

public class TimeUtil {
    private Handler handler;
    private static TimeUtil utils;

    private TimeUtil() { }
    public static TimeUtil getInstance() {
        if (utils == null) {
            utils = new TimeUtil();
            utils.handler = new Handler();
        }
        return utils;
    }

    public void postDelayed(Runnable runnable, long delay) {
        handler.postDelayed(runnable, delay);
    }
}
