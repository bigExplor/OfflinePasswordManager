package com.example.accountmanager.presenter;

import com.example.accountmanager.activities.MainActivity;
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.utils.FileUtil;
import com.example.accountmanager.utils.StringUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

/**
 * @author CharlesLu
 * @date 2021/5/27 11:22
 * @description
 */
public class MainActivityPresenter implements BasePresenter<MainActivity> {
    private MainActivity view;

    private final AccountDao accountDao = new AccountDao();

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

    /* 导出账号信息为字符串 */
    public void cpyStr() {
        List<Account> accountList = accountDao.getAllAccounts();
        if (accountList.size() == 0) {
            view.showToast("请先添加账号信息");
            return;
        }
        if (accountList.size() > 5) {
            view.showToast("账号条目过多，请使用文件备份");
            return;
        }
        view.copy(StringUtil.encode(gson.toJson(accountList)));
        view.showToast("备份字符串复制成功！");
    }

    /* 导出账号信息为文件 */
    public void cpyFile() {
        List<Account> accountList = accountDao.getAllAccounts();
        if (accountList.size() == 0) {
            view.showToast("请先添加账号信息");
            return;
        }
        boolean success = false;
        String filePath = "";
        try {
            filePath = FileUtil.getFilePath(view, "accounts.txt");
            success = FileUtil.writeToFile(filePath, StringUtil.encode(gson.toJson(accountList)), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!success) {
            view.showToast("备份失败");
            return;
        }
        view.showToast(FileUtil.share(view, filePath));
    }

    /* 将密文解析为账号信息并导入 */
    public boolean parse(String key) {
        List<Account> accountList;
        try {
            accountList = gson.fromJson(StringUtil.decode(key), new TypeToken<List<Account>>(){}.getType());
        } catch (Exception e) {
            view.showToast("密钥错误");
            return false;
        }
        view.showLoading(false);
        for (Account account: accountList) {
            account.removeInfo();
            accountDao.addAccount(account);
        }
        view.hideLoading();
        view.showToast("导入完成");
        return true;
    }
}
