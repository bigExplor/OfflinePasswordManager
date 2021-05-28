package com.example.accountmanager.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.accountmanager.R;
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.bean.Type;

import java.util.ArrayList;
import java.util.List;

public class TypeDao {
    private final MySQLiteHelper helper;
    private SQLiteDatabase db = null;
    private final AccountDao accountDao;

    public TypeDao() {
        helper = MySQLiteHelper.getInstance();
        accountDao = new AccountDao();
    }

    /* 获取所有类别信息 */
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

    /* 根据id获取类别信息 */
    public Type getTypeById(int typeId) {
        db = helper.getWritableDatabase();
        String sql = "select * from type where id = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{"" + typeId});
        Type type = null;
        if (cursor.moveToNext()) {
            type = getTypeFromCursor(cursor);
        }
        db.close();
        return type;
    }

    /* 根据类别名称获取类别信息（精确匹配） */
    public Type checkTypeByName(String name) {
        db = helper.getWritableDatabase();
        String sql = "select * from type where name = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        Type type = null;
        if (cursor.moveToNext()) {
            type = getTypeFromCursor(cursor);
        }
        db.close();
        return type;
    }

    /* 添加类别信息 */
    public void addType(Type type) {
        db = helper.getWritableDatabase();
        String sql = "insert into type (name, imgId) values (?, ?) ";
        db.execSQL(sql, new String[]{type.getName(), "" + type.getImgId()});
        db.close();
    }

    /* 删除类别信息 */
    public void removeType(int typeId) {
        db = helper.getWritableDatabase();
        String sql = "delete from type where id = ? ";
        db.execSQL(sql, new String[]{"" + typeId});
        db.close();
    }

    /* 更新类别信息 */
    public void updateType(Type type) {
        db = helper.getWritableDatabase();
        String sql = "update type set name = ?, imgId = ? where id = ? ";
        db.execSQL(sql, new String[]{type.getName(), "" + type.getImgId(), "" + type.getId()});
        db.close();
    }

    /* 从cursor中解析类别信息 */
    private Type getTypeFromCursor(Cursor cursor) {
        Type type = new Type();
        type.setId(cursor.getInt(cursor.getColumnIndex("id")));
        type.setName(cursor.getString(cursor.getColumnIndex("name")));
        int imgId = cursor.getInt(cursor.getColumnIndex("imgId"));
        if (imgId <= 0) imgId = R.drawable.others_cover;
        type.setImgId(imgId);
        List<Account> accounts = accountDao.getAccountByType(type.getId());
        type.setAccounts(accounts);
        for (Account account: accounts) {
            account.setType(type);
        }
        return type;
    }
}
