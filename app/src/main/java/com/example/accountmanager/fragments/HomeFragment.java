package com.example.accountmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.accountmanager.R;
import com.example.accountmanager.activities.MainActivity;
import com.example.accountmanager.activities.SearchActivity;
import com.example.accountmanager.adapters.TypeAdapter;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.dao.TypeDao;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private MainActivity mActivity;
    private TypeDao dao;
    private List<Type> typeList;
    private TypeAdapter adapter;
    private RecyclerView rv;
    private SwipeRefreshLayout swipe;
    private ImageView iv_search;

    public HomeFragment(MainActivity mActivity) {
        this.mActivity = mActivity;
        this.dao = new TypeDao();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_home, container, false);
        rv = view.findViewById(R.id.rv);
        swipe = view.findViewById(R.id.swipe);
        iv_search = view.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchActivity.class);
                startActivity(intent);
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAdapter();
            }
        });
        setAdapter();
        return view;
    }

    public void setAdapter() {
        getTypeList();
        if (adapter == null) {
            GridLayoutManager manager = new GridLayoutManager(mActivity, 1);
            rv.setLayoutManager(manager);
            adapter = new TypeAdapter(mActivity, typeList);
            rv.setAdapter(adapter);
        } else {
            adapter.setTypes(typeList);
            adapter.notifyDataSetChanged();
        }
        if (swipe != null && swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }
    }

    private void getTypeList() {
        typeList = dao.getAllType();
        if (typeList == null) typeList = new ArrayList<>();
    }

    public void onShow() {
        if (rv != null) {
            setAdapter();
        }
    }
}