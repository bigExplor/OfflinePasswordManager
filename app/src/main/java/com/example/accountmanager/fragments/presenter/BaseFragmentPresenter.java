package com.example.accountmanager.fragments.presenter;

import com.example.accountmanager.base.BaseFragment;

/**
 * @author CharlesLu
 * @date 2021/6/2 17:07
 * @description
 */
@SuppressWarnings("rawtypes")
public interface BaseFragmentPresenter<V extends BaseFragment> {
    void bindView(V view);
}
