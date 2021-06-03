package com.example.accountmanager.fragments.presenter;

import com.example.accountmanager.bean.Account;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.dao.TypeDao;
import com.example.accountmanager.fragments.ChestFragment;
import com.example.accountmanager.utils.StringUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author CharlesLu
 * @date 2021/6/2 17:06
 * @description
 */
public class ChestFragmentPresenter implements BaseFragmentPresenter<ChestFragment> {
    private ChestFragment view;
    private final TypeDao typeDao = new TypeDao();
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
    public void bindView(ChestFragment view) {
        this.view = view;
    }

    /* 将密文解析为账号信息并导入 */
    public boolean parse(String key) {
        List<Type> typeList = null;
        try {
            view.mActivity.log_d(StringUtil.decode(key));
            typeList = gson.fromJson(StringUtil.decode(key), new TypeToken<List<Type>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (typeList == null) {
            view.mActivity.showToast("密钥错误！");
            return false;
        }
        view.mActivity.showLoading(false);
        for (Type type : typeList) {
            if (type.getImgName() == null) type.setImgName("");
            addType(type);
        }
        view.mActivity.hideLoading();
        view.mActivity.showToast("导入完成");
        return true;
    }

    private void addType(Type type) {
        Type t = typeDao.getTypeByName(type.getName());
        if (t == null) {
            typeDao.addType(type);
            t = typeDao.getTypeByName(type.getName());
        }
        for (Account account : type.getAccounts()) {
            Account ac = accountDao.getAccountById(account.getId());
            if (ac != null && ac.getTitle().equals(account.getTitle())) continue;
            account.setTypeId(t.getId());
            accountDao.addAccount(account);
        }
    }
}
