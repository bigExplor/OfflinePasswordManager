package com.example.accountmanager.presenter;

import android.text.TextUtils;

import com.example.accountmanager.activities.AddTypeActivity;
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.bean.TypeImg;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.dao.TypeDao;

import java.util.List;

/**
 * @author CharlesLu
 * @date 2021/5/27 16:52
 * @description
 */
public class AddTypeActivityPresenter implements BasePresenter<AddTypeActivity> {
    private AddTypeActivity view;

    private final TypeDao typeDao = new TypeDao();
    private final AccountDao accountDao = new AccountDao();

    @Override
    public void bindView(AddTypeActivity view) {
        this.view = view;
    }

    /* 获取type信息 */
    public boolean initSetting() {
        view.type = typeDao.getTypeById(view.currentId);
        if (view.type == null) {
            view.showToast("参数错误");
            view.finish();
            return false;
        }
        return true;
    }

    /* 提交修改或添加 */
    public void commit() {
        Type type = view.type;
        if (TextUtils.isEmpty(type.getName())) {
            view.showToast("请填写分类名称");
            return;
        }
        if (type.getImgId() <= 0) {
            view.showToast("请选择分类的图标");
            return;
        }
        Type t = typeDao.checkTypeByName(type.getName());
        if (type.getId() > 0) { // 修改的逻辑
            if (t != null && t.getId() != type.getId()) {
                view.showToast("分类名重复！");
                return;
            }
            typeDao.updateType(type);
            view.showToast("修改成功！");
        } else { // 添加的逻辑
            if (t != null) {
                view.showToast("分类名重复！");
                return;
            }
            typeDao.addType(type);
            view.showToast("添加成功!");
        }
        view.finish();
    }

    /* 删除分类 */
    public void delete() {
        List<Account> accounts = accountDao.getAccountByType(view.type.getId());
        String msg;
        if (accounts.size() > 0) {
            msg = "删除当前分类将同时删除分类中已有的 " + accounts.size() + " 条账号信息，确认删除？";
        } else {
            msg = "确认删除当前分类？";
        }
        view.showConfirm(msg, isOk -> {
            if (isOk) {
                for (Account account : view.type.getAccounts()) {
                    accountDao.deleteAccount(account.getId());
                }
                typeDao.removeType(view.type.getId());
                view.showToast("删除成功！");
                view.finish();
            }
        });
    }
}
