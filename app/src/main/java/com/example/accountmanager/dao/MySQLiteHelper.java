package com.example.accountmanager.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.accountmanager.base.BaseApplication;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static MySQLiteHelper helper = null;

    public static String[] types = new String[]{"游戏", "娱乐", "工作", "效率", "社交", "教育", "工具", "其他", "私密"};
    private int[] typeIdx = new int[]{1, 10, 20, 30, 40, 50, 60, 100, 200};

    private MySQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 默认创建三张表，和一个管理员账号
    @Override
    public void onCreate(SQLiteDatabase db) {
        String typeSQL = "create table type(\n" +
                "id integer primary key autoincrement,\n" +
                "name varchar(20),\n" +
                "imgId integer\n" +
                ") ";
        db.execSQL(typeSQL);

        for (int i = 0; i < types.length; ++i) {
            String name = types[i];
            String sql = "insert into type (id, name) values (?, ?) ";
            db.execSQL(sql, new Object[]{typeIdx[i], name});
        }

        String accountSQL = "create table account(\n" +
                "id integer primary key autoincrement,\n" +
                "title varchar(20),\n" +
                "username varchar(20),\n" +
                "mail varchar(50),\n" +
                "phone varchar(15),\n" +
                "account varchar(20),\n" +
                "password varchar(20),\n" +
                "url varchar(100),\n" +
                "note varchar(100),\n" +
                "type integer\n" +
                ") ";
        db.execSQL(accountSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    // 获取实例
    public static MySQLiteHelper getInstance() {
        if (helper == null) {
            helper = new MySQLiteHelper(BaseApplication.instance, "account", null, 1);
        }
        return helper;
    }
}
