package com.example.accountmanager.utils;

import android.util.Base64;

public class StringUtil {
    public static String encode(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
    }

    public static String decode(String str) {
        return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
    }
}
