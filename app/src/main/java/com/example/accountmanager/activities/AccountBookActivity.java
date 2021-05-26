package com.example.accountmanager.activities;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.accountmanager.R;
import com.example.accountmanager.adapters.AccountAdapter;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.dao.TypeDao;
import com.example.accountmanager.ui.TitleBar;
import com.example.accountmanager.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CharlesLu
 * @description 每一个类别展示的界面
 */
public class AccountBookActivity extends BaseActivity {

    private TitleBar titleBar;
    private RecyclerView rv;
    private int id;
    private String name;
    private AccountAdapter adapter;
    private List<Account> accounts;

    private final TypeDao typeDao = new TypeDao();
    private final AccountDao accountDao = new AccountDao();
    private RelativeLayout rl_empty;
    private boolean isOpenEye = false;
    private SwipeRefreshLayout swipe;
    private int accountId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_book;
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
        swipe.setOnRefreshListener(() -> setAdapter(getAccountList()));
        titleBar.setLeft(R.drawable.left, v -> finish());
        titleBar.setRight(R.drawable.add, v -> {
            Intent intent = new Intent(AccountBookActivity.this, AddAccountActivity.class);
            intent.putExtra("from", "add");
            intent.putExtra("typeId", id);
            startActivity(intent);
        });
        setTitleEye();
    }

    @Override
    protected void initData() {
        id = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        log_d(name + " " + id);
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
                intent.putExtra("name", name);
                startActivity(intent);
            });
        }
        setAdapter(getAccountList());
    }

    private void setTitleEye() {
        int drawable = isOpenEye? R.drawable.eye_open_white: R.drawable.eye_close_white;
        titleBar.setRight2(drawable, v -> {
            isOpenEye = !isOpenEye;
            setTitleEye();
        });
        if (accounts == null) return;
        for (Account account: accounts) {
            account.setMode(isOpenEye? 1: 0);
        }
        setAdapter(accounts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccountList();
        if (accounts == null) return;
        setAdapter(accounts);
        isOpenEye = false;
        setTitleEye();
    }

    private void setAdapter(List<Account> list) {
        Type t = typeDao.getTypeById(id);
        titleBar.setText(t.getName());
        if (list.size() == 0) {
            rv.setVisibility(View.GONE);
            rl_empty.setVisibility(View.VISIBLE);
            return;
        }
        rl_empty.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new AccountAdapter(this, list, listener);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(adapter);
        } else {
            adapter.setAccounts(list);
            adapter.notifyDataSetChanged();
        }
        if (swipe != null && swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }
    }

    private AccountAdapter.OnItemClickListener listener = new AccountAdapter.OnItemClickListener() {
        @Override
        public void onMode(int position) {
            int mode = accounts.get(position).getMode();
            accounts.get(position).setMode(1 - mode);
            int i;
            for (i = 1; i < accounts.size(); i++) {
                if (accounts.get(i).getMode() != accounts.get(i - 1).getMode()) break;
            }
            if (i >= accounts.size()) {
                if (accounts.get(0).getMode() == 0) isOpenEye = false;
                else isOpenEye = true;
                setTitleEye();
            }
            setAdapter(accounts);
        }

        @Override
        public void onShare(int position) {
            Account account = accounts.get(position);
            String msg = "您的好友使用“账号本子”APP向您分享账号信息：\n" +
                    "账号：" + account.getAccount() + "\n" +
                    "密码：" + account.getPassword();
            copy(msg);
            showToast("账号复制完成，快去分享给好友吧");
        }

        @Override
        public void onEdit(int position) {
            Intent intent = new Intent(AccountBookActivity.this, AddAccountActivity.class);
            intent.putExtra("from", "update");
            intent.putExtra("accountId", accounts.get(position).getId());
            startActivity(intent);
        }

        @Override
        public void onDelete(final int position) {
            String msg = "确定删除名为 " + accounts.get(position).getTitle() + " 的记录吗？该动作不可撤回！";
            SpannableString ss = new SpannableString(msg);
            ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.work)), 7, 7+ accounts.get(position).getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            showConfirm(ss, new OnItemClickListener() {
                @Override
                public void onResult(boolean isOk) {
                    if (isOk) {
                        accountDao.deleteAccount(accounts.get(position).getId());
                        setAdapter(getAccountList());
                        showToast("删除成功！");
                    }
                }
            });
        }
    };

    private List<Account> getAccountList() {
        Type type = typeDao.getTypeById(id);
        if (type == null) {
            finish();
            accounts = null;
            return null;
        }
        accounts = type.getAccounts();
        return accounts;
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
