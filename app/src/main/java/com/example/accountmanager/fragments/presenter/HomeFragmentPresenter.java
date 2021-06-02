package com.example.accountmanager.fragments.presenter;

import com.example.accountmanager.bean.Type;
import com.example.accountmanager.dao.TypeDao;
import com.example.accountmanager.fragments.HomeFragment;
import com.example.accountmanager.utils.Constants;
import com.example.accountmanager.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CharlesLu
 * @date 2021/6/2 17:44
 * @description
 */
public class HomeFragmentPresenter implements BaseFragmentPresenter<HomeFragment> {
    @SuppressWarnings({"FieldCanBeLocal", "unused", "RedundantSuppression"})
    private HomeFragment view;

    public List<Type> typeList;
    private final TypeDao dao = new TypeDao();

    @Override
    public void bindView(HomeFragment view) {
        this.view = view;
    }

    public void getTypeList() {
        typeList = dao.getAllType();
        if (typeList == null) typeList = new ArrayList<>();
        if (!typeList.isEmpty() && !SpUtil.getInstance().getBoolean("privateSpace")) {
            int i;
            for (i = 0; i < typeList.size(); ++i) {
                if (typeList.get(i).getName().equals(Constants.privateName)) break;
            }
            typeList.remove(i);
        }
    }
}
