package com.example.accountmanager.activities;

import android.content.Intent;
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
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.dao.TypeDao;
import com.example.accountmanager.utils.ScreenUtil;

import java.util.List;

public class SearchActivity extends BaseActivity {

    private TextView tv_cancel;
    private EditText et_search;
    private RecyclerView rv_list;
    private SearchItemAdapter adapter;
    private TypeDao typeDao = new TypeDao();
    private AccountDao accountDao = new AccountDao();
    private List<Account> accountList;
    private RelativeLayout rl_empty;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
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
                    search(str);
                }
                return false;
            }
        });
    }

    private void search(String str) {
        accountList = accountDao.getAccountByTitle(str);
        for (Account account: accountList) {
            account.setType(typeDao.getTypeById(account.getTypeId()));
        }
        if (accountList.size() == 0) {
            rv_list.setVisibility(View.GONE);
            rl_empty.setVisibility(View.VISIBLE);
            ScreenUtil.showKeyboard(et_search);
        } else {
            rl_empty.setVisibility(View.GONE);
            rv_list.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new SearchItemAdapter(this, accountList, new SearchItemAdapter.OnItemClickListener() {
                    @Override
                    public void onGo(int position) {
                        Intent intent = new Intent(SearchActivity.this, AccountBookActivity.class);
                        intent.putExtra("id", accountList.get(position).getTypeId());
                        intent.putExtra("name", accountList.get(position).getType().getName());
                        intent.putExtra("accountId", accountList.get(position).getId());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onShare(int position) {
                        Account account = accountList.get(position);
                        String msg = "您的好友使用“账号本子”APP向您分享账号信息：\n" +
                                "账号：" + account.getAccount() + "\n" +
                                "密码：" + account.getPassword();
                        copy(msg);
                        showToast("账号复制完成，快去分享给好友吧");
                    }
                });
                rv_list.setLayoutManager(new LinearLayoutManager(this));
                rv_list.setAdapter(adapter);
            } else {
                adapter.setData(accountList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected int getStatusColor() {
        return R.color.transparent_black_20;
    }
}
