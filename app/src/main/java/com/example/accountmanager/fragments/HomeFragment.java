package com.example.accountmanager.fragments;

import android.content.Intent;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.accountmanager.R;
import com.example.accountmanager.activities.MainActivity;
import com.example.accountmanager.activities.SearchActivity;
import com.example.accountmanager.adapters.TypeAdapter;
import com.example.accountmanager.base.BaseFragment;
import com.example.accountmanager.fragments.presenter.HomeFragmentPresenter;

public class HomeFragment extends BaseFragment<HomeFragmentPresenter> {
    private final MainActivity mActivity;
    private TypeAdapter adapter;

    private RecyclerView rv;
    private ImageView ivSearch;
    private SwipeRefreshLayout swipe;

    public HomeFragment(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_home;
    }

    @Override
    protected HomeFragmentPresenter getPresenter() {
        return new HomeFragmentPresenter();
    }

    @Override
    protected void initView() {
        rv = view.findViewById(R.id.rv);
        swipe = view.findViewById(R.id.swipe);
        ivSearch = view.findViewById(R.id.iv_search);
    }

    @Override
    protected void initListener() {
        ivSearch.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, SearchActivity.class);
            startActivity(intent);
        });
        swipe.setOnRefreshListener(this::setAdapter);

        setAdapter();
    }

    public void setAdapter() {
        p.getTypeList();
        if (adapter == null) {
            GridLayoutManager manager = new GridLayoutManager(mActivity, 1);
            rv.setLayoutManager(manager);
            adapter = new TypeAdapter(mActivity, p.typeList);
            rv.setAdapter(adapter);
        } else {
            adapter.setTypes(p.typeList);
            adapter.notifyDataSetChanged();
        }
        if (swipe != null && swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }
    }

    public void onShow() {
        if (rv != null) {
            setAdapter();
        }
    }
}