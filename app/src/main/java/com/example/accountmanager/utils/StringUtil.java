package com.example.accountmanager.utils;

import android.util.Base64;

public class StringUtil {
    /* base64 加密 */
    public static String encode(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
    }

    /* base64解密 */
    public static String decode(String str) {
        return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
    }
}
