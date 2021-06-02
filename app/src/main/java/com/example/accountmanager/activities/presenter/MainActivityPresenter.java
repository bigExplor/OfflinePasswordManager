package com.example.accountmanager.activities.presenter;

import com.example.accountmanager.activities.MainActivity;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.dao.TypeDao;
import com.example.accountmanager.utils.FileUtil;
import com.example.accountmanager.utils.StringUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

/**
 * @author CharlesLu
 * @date 2021/5/27 11:22
 * @description
 */
public class MainActivityPresenter implements BaseActivityPresenter<MainActivity> {
    private MainActivity view;

    private final TypeDao typeDao = new TypeDao();

    private final Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getName().equals("type");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).create();

    @Override
    public void bindView(MainActivity view) {
        this.view = view;
    }

    private int getAccountSize(List<Type> list) {
        int size = 0;
        for (Type type : list) {
            size += type.getAccounts().size();
        }
        return size;
    }

    /* 导出账号信息为字符串 */
    public void cpyStr() {
        List<Type> typeList = typeDao.getAllType();
        int accountSize = getAccountSize(typeList);
        if (accountSize == 0) {
            view.showToast("请先添加账号信息");
            return;
        }
        if (accountSize > 5) {
            view.showToast("账号条目过多，请使用文件备份");
            return;
        }
        view.copy(StringUtil.encode(gson.toJson(typeList)));
        view.showToast("备份字符串复制成功！");
    }

    /* 导出账号信息为文件 */
    public void cpyFile() {
        List<Type> typeList = typeDao.getAllType();
        int accountSize = getAccountSize(typeList);
        if (accountSize == 0) {
            view.showToast("请先添加账号信息");
            return;
        }
        boolean success = false;
        String filePath = "";
        try {
            filePath = FileUtil.getFilePath(view, "accounts.txt");
            success = FileUtil.writeToFile(filePath, StringUtil.encode(gson.toJson(typeList)), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!success) {
            view.showToast("备份失败");
            return;
        }
        view.showToast(FileUtil.share(view, filePath));
    }
}
