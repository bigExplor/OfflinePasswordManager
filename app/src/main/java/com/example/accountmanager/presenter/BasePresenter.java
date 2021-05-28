package com.example.accountmanager.presenter;

import com.example.accountmanager.base.BaseActivity1;

/**
 * @author CharlesLu
 * @date 2021/5/27 11:12
 * @description
 */
public interface BasePresenter<V extends BaseActivity1> {
    void bindView(V view);
}
