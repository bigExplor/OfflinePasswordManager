package com.example.accountmanager.bean;

/**
 * @author CharlesLu
 * @date 2021/5/25 15:51
 * @description 类别图标类
 */
public class TypeImg {
    public int imgId;
    public boolean hasChosen;

    public TypeImg(int imgId, boolean hasChosen) {
        this.imgId = imgId;
        this.hasChosen = hasChosen;
    }
}
