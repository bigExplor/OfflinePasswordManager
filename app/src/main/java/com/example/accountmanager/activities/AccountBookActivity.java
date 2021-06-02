package com.example.accountmanager.activities;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.accountmanager.R;
import com.example.accountmanager.adapters.AccountAdapter;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.activities.presenter.AccountBookActivityPresenter;
import com.example.accountmanager.ui.TitleBar;
import com.example.accountmanager.utils.Constants;

/**
 * @author CharlesLu
 * @description 每一个类别展示的界面
 */
public class AccountBookActivity extends BaseActivity<AccountBookActivityPresenter> {

    public int id;
    public String name;
    public int accountId;

    private TitleBar titleBar;
    private RecyclerView rv;
    private RelativeLayout rl_empty;
    private SwipeRefreshLayout swipe;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_book;
    }

    @Override
    protected AccountBookActivityPresenter getPresenter() {
        return new AccountBookActivityPresenter();
    }

    @Override
    protected void initView() {
        titleBar = findViewById(R.id.titleBar);
        rv = findViewById(R.id.rv);
        rl_empty = findViewById(R.id.rl_empty);
        swipe = findViewById(R.id.swipe);
    }

    @Override
    protected void initListener() {
        swipe.setOnRefreshListener(() -> p.setAdapter(true));
        titleBar.setLeft(R.drawable.left, v -> finish());
        titleBar.setRight(R.drawable.add, v -> {
            Intent intent = new Intent(AccountBookActivity.this, AddAccountActivity.class);
            intent.putExtra("from", "add");
            intent.putExtra("typeId", id);
            startActivity(intent);
        });
        p.setTitleEye();
    }

    @Override
    protected void initData() {
        id = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
//        TODO 从搜索进来，跳到这个记录处
        accountId = getIntent().getIntExtra("accountId", -1);
        if (id <= 0) {
            finish();
            return;
        }
        titleBar.setText(name);
        if (id == Constants.privateId) {
            titleBar.setBackground(R.color.black);
        } else {
            titleBar.setRight3(R.drawable.setting, v -> {
                Intent intent = new Intent(this, AddTypeActivity.class);
                intent.putExtra("from", "setting");
                intent.putExtra("id", id);
                startActivity(intent);
            });
        }
        p.setAdapter(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        p.setAdapter(true);
        p.isOpenEye = false;
        p.setTitleEye();
    }

    /* 设置标题栏文字 */
    public void setTitleText(String name) {
        titleBar.setText(name);
    }

    /* 设置标题栏小眼睛图标 */
    public void setTitleRight2(int drawable, View.OnClickListener listener) {
       titleBar.setRight2(drawable, listener);
    }

    /* 是否展示占位图 */
    public void displayEmpty(boolean isEmpty) {
        if (isEmpty) {
            rv.setVisibility(View.GONE);
            rl_empty.setVisibility(View.VISIBLE);
        } else {
            rl_empty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }

    /* 初始化RecyclerView */
    public void initRecyclerView(AccountAdapter adapter) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    /* 设置刷新条显示隐藏 */
    public void setRefresh(boolean isRefresh) {
        if (swipe.isRefreshing() != isRefresh) {
            swipe.setRefreshing(isRefresh);
        }
    }

    @Override
    protected int getStatusColor() {
        int color = R.color.logoRed;
        if (id == Constants.privateId) {
            color = R.color.black;
        }
        return color;
    }
}
