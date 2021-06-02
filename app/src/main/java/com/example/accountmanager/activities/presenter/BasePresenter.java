package com.example.accountmanager.activities.presenter;

import com.example.accountmanager.base.BaseActivity;

/**
 * @author CharlesLu
 * @date 2021/5/27 11:12
 * @description
 */
public interface BasePresenter<V extends BaseActivity> {
    void bindView(V view);
}
