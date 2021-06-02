package com.example.accountmanager.activities.presenter;

import android.content.Intent;

import com.example.accountmanager.activities.AccountBookActivity;
import com.example.accountmanager.activities.SearchActivity;
import com.example.accountmanager.adapters.SearchItemAdapter;
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.dao.TypeDao;

import java.util.List;

/**
 * @author CharlesLu
 * @date 2021/5/27 13:22
 * @description
 */
public class SearchActivityPresenter implements BasePresenter<SearchActivity> {
    private SearchActivity view;

    private SearchItemAdapter adapter;
    private List<Account> accountList;

    private final TypeDao typeDao = new TypeDao();
    private final AccountDao accountDao = new AccountDao();

    @Override
    public void bindView(SearchActivity view) {
        this.view = view;
    }

    public void search(String str) {
        accountList = accountDao.getAccountByTitle(str);
        for (Account account: accountList) {
            account.setType(typeDao.getTypeById(account.getTypeId()));
        }
        if (accountList.size() == 0) {
            view.displayEmpty(true);
        } else {
            view.displayEmpty(false);
            if (adapter == null) {
                adapter = new SearchItemAdapter(view, accountList, new SearchItemAdapter.OnItemClickListener() {
                    @Override
                    public void onGo(int position) {
                        Account account = accountList.get(position);
                        Intent intent = new Intent(view, AccountBookActivity.class);
                        intent.putExtra("id", account.getTypeId());
                        intent.putExtra("name", account.getType().getName());
                        intent.putExtra("accountId", account.getId());
                        view.startActivity(intent);
                        view.finish();
                    }

                    @Override
                    public void onShare(int position) {
                        Account account = accountList.get(position);
                        String msg = "您的好友使用“账号本子”APP向您分享账号信息：\n" +
                                "账号：" + account.getAccount() + "\n" +
                                "密码：" + account.getPassword();
                        view.copy(msg);
                        view.showToast("账号复制完成，快去分享给好友吧");
                    }
                });
                view.initRecyclerView(adapter);
            } else {
                adapter.setData(accountList);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
