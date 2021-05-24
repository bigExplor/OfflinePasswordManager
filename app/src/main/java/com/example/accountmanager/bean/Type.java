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

    public int getImgIdByName() {
        switch (name) {
            case "游戏":
                return R.drawable.game_cover;
            case "工作":
                return R.drawable.work_cover;
            case "娱乐":
                return R.drawable.happy_cover;
            case "社交":
                return R.drawable.chat_cover;
            case "教育":
                return R.drawable.education_cover;
            case "效率":
                return R.drawable.xiaolv_cover;
            case "工具":
                return R.drawable.tools_cover;
            case "私密":
                return R.drawable.private_cover;
            case "其他":
            default:
                return R.drawable.others_cover;
        }
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
