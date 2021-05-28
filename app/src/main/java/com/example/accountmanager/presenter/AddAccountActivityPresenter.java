package com.example.accountmanager.presenter;

import android.text.TextUtils;
import android.webkit.URLUtil;

import com.example.accountmanager.activities.AddAccountActivity;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.dao.TypeDao;

/**
 * @author CharlesLu
 * @date 2021/5/27 17:46
 * @description
 */
public class AddAccountActivityPresenter implements BasePresenter<AddAccountActivity> {
    private AddAccountActivity view;

    private final TypeDao typeDao = new TypeDao();
    private final AccountDao accountDao = new AccountDao();

    @Override
    public void bindView(AddAccountActivity view) {
        this.view = view;
    }

    public void getTypeAndAccount(int typeId, int accountId) {
        if (typeId > 0) {
            view.currentType = typeDao.getTypeById(typeId);
        }
        if (accountId > 0) {
            view.currentAccount = accountDao.getAccountById(accountId);
        }
        view.allTypeList.clear();
        view.allTypeList.addAll(typeDao.getAllType());
        view.typeName = new String[view.allTypeList.size()];
        for (int i = 0; i < view.typeName.length; ++i) view.typeName[i] = view.allTypeList.get(i).getName();
    }

    public void check() {
        if (TextUtils.isEmpty(view.currentAccount.getTitle())) {
            view.showToast("名称不为空");
            return;
        }
        if (TextUtils.isEmpty(view.currentAccount.getPassword())) {
            view.showToast("密码不为空");
            return;
        }
        if (!TextUtils.isEmpty(view.currentAccount.getUrl()) && !URLUtil.isHttpUrl(view.currentAccount.getUrl()) && !URLUtil.isHttpsUrl(view.currentAccount.getUrl())) {
            view.showToast("网址格式不正确");
            return;
        }
        if ("用户名".equals(view.account_type)) {
            if (TextUtils.isEmpty(view.currentAccount.getUsername())) {
                view.showToast("与账号绑定的用户名不可为空");
                return;
            }
            view.currentAccount.setAccount(view.currentAccount.getUsername());
        } else if ("邮箱".equals(view.account_type)) {
            if (TextUtils.isEmpty(view.currentAccount.getMail())) {
                view.showToast("与账号绑定的邮箱不可为空");
                return;
            }
            view.currentAccount.setAccount(view.currentAccount.getMail());
        } else {
            if (TextUtils.isEmpty(view.currentAccount.getPhone())) {
                view.showToast("与账号绑定的电话不可为空");
                return;
            }
            view.currentAccount.setAccount(view.currentAccount.getPhone());
        }
        if (view.currentAccount.getId() > 0) accountDao.updateAccount(view.currentAccount);
        else accountDao.addAccount(view.currentAccount);
        view.finish();
    }
}
