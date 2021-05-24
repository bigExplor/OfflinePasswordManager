package com.example.accountmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.accountmanager.base.BaseApplication;

public class SpUtil {
    private Context mContext;
    private static SpUtil instance;
    private String spName = "Configuration";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private SpUtil() { }
    private SpUtil(Context context) {
        this.mContext = context;
    }

    public static SpUtil getInstance() {
        if(instance == null) {
            instance = new SpUtil(BaseApplication.instance);
        }
        return instance;
    }

    public static SpUtil getInstance(Context context) {
        if(instance == null) {
            instance = new SpUtil(context);
        }
        return instance;
    }

    // 获取SP实例
    private SharedPreferences getSP() {
        if(sp == null) {
            sp = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        }
        return sp;
    }

    // 获取SP编辑器
    private SharedPreferences.Editor getEditor() {
        if(editor == null) {
            editor = getSP().edit();
        }
        return editor;
    }

    // 添加String类型值
    public void putString(String key, String value) {
        getEditor().putString(key, value);
        getEditor().commit();
    }

    // 获取String类型值
    public String getString(String key) {
        return getSP().getString(key, "");
    }

    // 添加boolean数据
    public void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value);
        getEditor().commit();
    }

    // 获取boolean数据
    public boolean getBoolean(String key) {
        return getSP().getBoolean(key, false);
    }

    // 添加float数据
    public void putFloat(String key, float value) {
        getEditor().putFloat(key, value);
        getEditor().commit();
    }

    // 获取float数据
    public float getFloat(String key) {
        return getSP().getFloat(key, 0f);
    }

    // 添加long数据
    public void putLong(String key, long value) {
        getEditor().putLong(key, value);
        getEditor().commit();
    }

    // 获取long数据
    public Long getLong(String key) {
        return getSP().getLong(key, 0l);
    }

    // 添加int数据
    public void putInt(String key, int value) {
        getEditor().putInt(key, value);
        getEditor().commit();
    }

    // 获取int数据
    public int getInt(String key) {
        return getSP().getInt(key, 0);
    }

    // 删除某一个值
    public void removeValue(String key) {
        getEditor().remove(key);
        getEditor().commit();
    }

    public void clear() {
        getEditor().clear();
        getEditor().commit();
    }

    // 切换SP文件
    public void switchSp(String spName) {
        this.spName = spName;
        this.sp = null;
        this.editor = null;
    }

    // 切换到默认配置
    public void reset() {
        switchSp("Configuration");
    }
}
