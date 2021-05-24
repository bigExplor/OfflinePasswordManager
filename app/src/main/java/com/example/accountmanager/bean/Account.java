package com.example.accountmanager.bean;

import android.text.TextUtils;

public class Account {
    private int id;
    private String title;
    private String username;
    private String mail;
    private String phone;
    private String account;
    private String password;
    private String url;
    private String note;
    private int mode;// 0-精简，1-完全
    private int typeId;
    private Type type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void fillInfo() {
        if (TextUtils.isEmpty(mail)) mail = "暂无提供";
        if (TextUtils.isEmpty(phone)) phone = "暂无提供";
        if (TextUtils.isEmpty(note)) note = "暂无提供";
        if (TextUtils.isEmpty(url)) url = "暂无提供";
    }

    public void removeInfo() {
        if ("暂无提供".equals(mail)) mail = "";
        if ("暂无提供".equals(phone)) phone = "";
        if ("暂无提供".equals(note)) note = "";
        if ("暂无提供".equals(url)) url = "";
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", username='" + username + '\'' +
                ", mail='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", note='" + note + '\'' +
                ", mode=" + mode +
                ", typeId=" + typeId +
                '}';
    }
}
