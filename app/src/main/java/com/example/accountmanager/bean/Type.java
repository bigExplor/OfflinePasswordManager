package com.example.accountmanager.bean;

import com.example.accountmanager.R;

import java.util.List;

public class Type {
    private int id;
    private String name;
    private int imgId;
    private List<Account> accounts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> list) {
        this.accounts = list;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imgId=" + imgId +
                ", accounts=" + accounts +
                '}';
    }
}
