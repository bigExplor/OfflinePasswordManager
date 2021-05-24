package com.example.accountmanager.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.accountmanager.bean.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    private MySQLiteHelper helper;
    private SQLiteDatabase db = null;

    public AccountDao() {
        helper = MySQLiteHelper.getInstance();
    }

    public void addAccount(Account account) {
        db = helper.getWritableDatabase();
        String sql = "insert into account (title, username, mail, phone, account, password, url, note, type) values (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        db.execSQL(sql, new Object[]{account.getTitle(), account.getUsername(), account.getMail(), account.getPhone(), account.getAccount(), account.getPassword(), account.getUrl(), account.getNote(), account.getTypeId()});
        db.close();
    }

    public void deleteAccount(int id) {
        db = helper.getWritableDatabase();
        String sql = "delete from account where id = ? ";
        db.execSQL(sql, new Object[]{""+id});
        db.close();
    }

    public void updateAccount(Account account) {
        db = helper.getWritableDatabase();
        String sql = "update account set title = ?, username = ?, mail = ?, phone = ?, account = ?, password = ?, url = ?, note = ?, type = ? where id = ? ";
        db.execSQL(sql, new Object[]{account.getTitle(), account.getUsername(), account.getMail(), account.getPhone(), account.getAccount(), account.getPassword(), account.getUrl(), account.getNote(), account.getTypeId(), account.getId()});
        db.close();
    }

    public Account getAccountById(int accountId) {
        db = helper.getWritableDatabase();
        Account account = null;
        String sql = "select * from account where id = ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{""+accountId});
        if (cursor.moveToNext()) {
            account = getAccountFromCursor(cursor);
        }
        db.close();
        return account;
    }

    public List<Account> getAccountByType(int typeId) {
        db = helper.getWritableDatabase();
        String sql = "select * from account where type = ? ";
        List<Account> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{""+typeId});
        while (cursor.moveToNext()) {
            list.add(getAccountFromCursor(cursor));
        }
        db.close();
        return list;
    }

    public List<Account> getAccountByTitle(String title) {
        db = helper.getWritableDatabase();
        String sql = "select * from account where title like ? order by type";
        List<Account> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{"%"+ title +"%"});
        while (cursor.moveToNext()) {
            list.add(getAccountFromCursor(cursor));
        }
        db.close();
        return list;
    }

    public List<Account> getAllAccounts() {
        db = helper.getWritableDatabase();
        String sql = "select * from account ";
        List<Account> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {
            list.add(getAccountFromCursor(cursor));
        }
        db.close();
        return list;
    }

    private Account getAccountFromCursor(Cursor cursor) {
        Account account = new Account();
        account.setId(cursor.getInt(cursor.getColumnIndex("id")));
        account.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        account.setUsername(cursor.getString(cursor.getColumnIndex("username")));
        account.setMail(cursor.getString(cursor.getColumnIndex("mail")));
        account.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
        account.setAccount(cursor.getString(cursor.getColumnIndex("account")));
        account.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        account.setUrl(cursor.getString(cursor.getColumnIndex("url")));
        account.setNote(cursor.getString(cursor.getColumnIndex("note")));
        account.setTypeId(cursor.getInt(cursor.getColumnIndex("type")));
        account.setMode(0);
        account.fillInfo();
        return account;
    }
}
