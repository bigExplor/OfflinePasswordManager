package com.example.accountmanager.bean;

import android.annotation.SuppressLint;

import com.example.accountmanager.R;

import java.util.List;

public class Type {
    private int id;
    private String name;
    private int imgId;
    private String imgName;
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

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> list) {
        this.accounts = list;
    }

    @SuppressLint("NonConstantResourceId")
    public String imgId2String() {
        String imgName;
        switch (imgId) {
            case R.drawable.game_cover:
                imgName = "game_cover";
                break;
            case R.drawable.work_cover:
                imgName = "work_cover";
                break;
            case R.drawable.happy_cover:
                imgName = "happy_cover";
                break;
            case R.drawable.chat_cover:
                imgName = "chat_cover";
                break;
            case R.drawable.education_cover:
                imgName = "education_cover";
                break;
            case R.drawable.xiaolv_cover:
                imgName = "xiaolv_cover";
                break;
            case R.drawable.tools_cover:
                imgName = "tools_cover";
                break;
            case R.drawable.private_cover:
                imgName = "private_cover";
                break;
            default:
            case R.drawable.others_cover:
                imgName = "others_cover";
                break;
        }
        return imgName;
    }

    public int string2ImgId() {
        int imgId;
        switch (imgName) {
            case "game_cover":
                imgId = R.drawable.game_cover;
                break;
            case "work_cover":
                imgId = R.drawable.work_cover;
                break;
            case "happy_cover":
                imgId = R.drawable.happy_cover;
                break;
            case "chat_cover":
                imgId = R.drawable.chat_cover;
                break;
            case "education_cover":
                imgId = R.drawable.education_cover;
                break;
            case "xiaolv_cover":
                imgId = R.drawable.xiaolv_cover;
                break;
            case "tools_cover":
                imgId = R.drawable.tools_cover;
                break;
            case "private_cover":
                imgId = R.drawable.private_cover;
                break;
            default:
            case "others_cover":
                imgId = R.drawable.others_cover;
                break;
        }
        return imgId;
    }
}
