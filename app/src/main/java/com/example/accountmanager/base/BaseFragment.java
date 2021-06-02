package com.example.accountmanager.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accountmanager.fragments.presenter.BaseFragmentPresenter;

/**
 * @author CharlesLu
 * @date 2021/6/2 16:46
 * @description
 */
@SuppressWarnings("rawtypes")
public abstract class BaseFragment<P extends BaseFragmentPresenter> extends Fragment {
    protected P p;
    protected View view;

    protected abstract P getPresenter();
    protected abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);

        p = getPresenter();
        if (p != null) p.bindView(this);

        initView();
        initListener();
        return view;
    }

    public void onShow() { }
    protected void initView() { }
    protected void initListener() { }
}
