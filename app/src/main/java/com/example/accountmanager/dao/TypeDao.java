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
    private final AccountDao dao;

    public TypeDao() {
        helper = MySQLiteHelper.getInstance();
        dao = new AccountDao();
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
        Type type = null;
        Cursor cursor = db.rawQuery(sql, new String[]{"" + typeId});
        if (cursor.moveToNext()) {
            type = getTypeFromCursor(cursor);
        }
        db.close();
        return type;
    }

    /* 根据类别名称获取类别信息（精确匹配） */
    public boolean checkTypeByName(String name) {
        db = helper.getWritableDatabase();
        String sql = "select * from type where name = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        boolean typeExist = cursor.moveToNext();
        cursor.close();
        db.close();
        return typeExist;
    }

    /* 添加类别信息 */
    public boolean addType(Type type) {
        if (checkTypeByName(type.getName())) { // 存在同名类名返回 false
            return false;
        }
        db = helper.getWritableDatabase();
        String sql = "insert into type (name, imgId) values (?, ?) ";
        db.execSQL(sql, new String[]{type.getName(), "" + type.getImgId()});
        db.close();
        return true;
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

    private Type getTypeFromCursor(Cursor cursor) {
        Type type = new Type();
        type.setId(cursor.getInt(cursor.getColumnIndex("id")));
        type.setName(cursor.getString(cursor.getColumnIndex("name")));
        int imgId = cursor.getInt(cursor.getColumnIndex("imgId"));
        if (imgId <= 0) imgId = R.drawable.others_cover;
        type.setImgId(imgId);
        List<Account> accounts = dao.getAccountByType(type.getId());
        type.setAccounts(accounts);
        for (Account account: accounts) {
            account.setType(type);
        }
        return type;
    }
}
