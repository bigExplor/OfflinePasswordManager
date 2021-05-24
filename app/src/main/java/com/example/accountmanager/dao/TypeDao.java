package com.example.accountmanager.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.accountmanager.bean.Account;
import com.example.accountmanager.bean.Type;

import java.util.ArrayList;
import java.util.List;

public class TypeDao {
    private MySQLiteHelper helper;
    private SQLiteDatabase db = null;
    private AccountDao dao;

    public TypeDao() {
        helper = MySQLiteHelper.getInstance();
        dao = new AccountDao();
    }

    public List<Type> getAllType() {
        db = helper.getWritableDatabase();
        List<Type> list = new ArrayList<>();
        String sql = "select * from type ";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {
            list.add(getTypeFromCursor(cursor));
        }
        db.close();
        return list;
    }

    public Type getTypeById(int typeId) {
        db = helper.getWritableDatabase();
        String sql = "select * from type where id = ? ";
        Type type = null;
        Cursor cursor = db.rawQuery(sql, new String[]{"" + typeId});
        if (cursor.moveToNext()) {
            type = getTypeFromCursor(cursor);
        }
        db.close();
        return type;
    }

    private Type getTypeFromCursor(Cursor cursor) {
        Type type = new Type();
        type.setId(cursor.getInt(cursor.getColumnIndex("id")));
        type.setName(cursor.getString(cursor.getColumnIndex("name")));
        type.setImgId(type.getImgIdByName());
        List<Account> accounts = dao.getAccountByType(type.getId());
        type.setAccounts(accounts);
        for (Account account: accounts) {
            account.setType(type);
        }
        return type;
    }
}
