package com.example.accountmanager.activities;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountmanager.R;
import com.example.accountmanager.adapters.SearchItemAdapter;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.presenter.SearchActivityPresenter;

/**
 * @author CharlesLu
 * @description 搜索界面
 */
public class SearchActivity extends BaseActivity<SearchActivityPresenter> {

    private TextView tv_cancel;
    private EditText et_search;
    private RecyclerView rv_list;
    private RelativeLayout rl_empty;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected SearchActivityPresenter getPresenter() {
        return new SearchActivityPresenter();
    }

    @Override
    protected void initView() {
        tv_cancel = findViewById(R.id.tv_cancel);
        et_search = findViewById(R.id.et_search);
        rv_list = findViewById(R.id.rv_list);
        rl_empty = findViewById(R.id.rl_empty);

        et_search.requestFocus();
    }

    @Override
    protected void initListener() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String str = et_search.getText().toString().trim();
                    if (TextUtils.isEmpty(str)) {
                        showToast("输入关键字");
                        et_search.requestFocus();
                        return false;
                    }
                    p.search(str);
                }
                return false;
            }
        });
    }

    /* 是否展示占位图 */
    public void displayEmpty(boolean isEmpty) {
        if (isEmpty) {
            rv_list.setVisibility(View.GONE);
            rl_empty.setVisibility(View.VISIBLE);
        } else {
            rl_empty.setVisibility(View.GONE);
            rv_list.setVisibility(View.VISIBLE);
        }
    }

    /* 初始化RecyclerView */
    public void initRecyclerView(SearchItemAdapter adapter) {
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.setAdapter(adapter);
    }

    @Override
    protected int getStatusColor() {
        return R.color.transparent_black_20;
    }
}
