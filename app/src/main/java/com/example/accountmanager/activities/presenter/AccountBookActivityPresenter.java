package com.example.accountmanager.activities.presenter;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.example.accountmanager.R;
import com.example.accountmanager.activities.AccountBookActivity;
import com.example.accountmanager.activities.AddAccountActivity;
import com.example.accountmanager.adapters.AccountAdapter;
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.dao.AccountDao;
import com.example.accountmanager.dao.TypeDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CharlesLu
 * @date 2021/5/27 13:43
 * @description
 */
public class AccountBookActivityPresenter implements BaseActivityPresenter<AccountBookActivity> {
    private AccountBookActivity view;

    private AccountAdapter adapter;
    private final List<Account> accounts = new ArrayList<>();

    private final TypeDao typeDao = new TypeDao();
    private final AccountDao accountDao = new AccountDao();

    public boolean isOpenEye = false;

    @Override
    public void bindView(AccountBookActivity view) {
        this.view = view;
    }

    /* 从数据库刷新当前最新的账号列表 */
    private void refreshAccountList() {
        Type currentType = typeDao.getTypeById(view.id);
        if (currentType == null) {
            view.finish();
            return;
        }
        accounts.clear();
        accounts.addAll(currentType.getAccounts());
        // 类型名称可能会发生变化
        if (!currentType.getName().equals(view.name)) {
            view.name = currentType.getName();
            view.setTitleText(currentType.getName());
        }
    }

    /* 控制标题栏小眼睛的开关 */
    public void setTitleEye() {
        int drawable = isOpenEye? R.drawable.eye_open_white: R.drawable.eye_close_white;
        view.setTitleRight2(drawable, v -> {
            isOpenEye = !isOpenEye;
            setTitleEye();
        });
        if (accounts.isEmpty()) return;
        for (Account account: accounts) {
            account.setMode(isOpenEye? 1: 0);
        }
        setAdapter(false);
    }

    /* 设置账号适配器 */
    public void setAdapter(boolean isNeedForceRefresh) {
        if (isNeedForceRefresh) {
            refreshAccountList();
        }
        if (accounts.isEmpty()) {
            view.displayEmpty(true);
            return;
        }
        view.displayEmpty(false);
        if (adapter == null) {
            adapter = new AccountAdapter(view, accounts, listener);
            view.initRecyclerView(adapter);
        } else {
            adapter.setAccounts(accounts);
            adapter.notifyDataSetChanged();
        }
        view.setRefresh(false);
    }

    private final AccountAdapter.OnItemClickListener listener = new AccountAdapter.OnItemClickListener() {
        @Override
        public void onMode(int position) {
            Account account = accounts.get(position);
            account.setMode(1 - account.getMode());
            int i;
            for (i = 1; i < accounts.size(); i++) {
                if (accounts.get(i).getMode() != accounts.get(i - 1).getMode()) break;
            }
            if (i >= accounts.size()) {
                isOpenEye = accounts.get(0).getMode() != 0;
                setTitleEye();
            }
            setAdapter(false);
        }

        @Override
        public void onShare(int position) {
            Account account = accounts.get(position);
            String msg = "您的好友使用“账号本子”APP向您分享账号信息：\n" +
                    "账号：" + account.getAccount() + "\n" +
                    "密码：" + account.getPassword();
            view.copy(msg);
            view.showToast("账号复制完成，快去分享给好友吧");
        }

        @Override
        public void onEdit(int position) {
            Intent intent = new Intent(view, AddAccountActivity.class);
            intent.putExtra("from", "update");
            intent.putExtra("typeId", view.id);
            intent.putExtra("accountId", accounts.get(position).getId());
            view.startActivity(intent);
        }

        @Override
        public void onDelete(final int position) {
            Account account = accounts.get(position);
            String msg = "确定删除名为 " + account.getTitle() + " 的记录吗？该动作不可撤回！";
            SpannableString spannableString = new SpannableString(msg);
            spannableString.setSpan(new ForegroundColorSpan(view.getResources().getColor(R.color.work)), 7, 7+ account.getTitle().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.showConfirm(spannableString, isOk -> {
                if (isOk) {
                    accountDao.deleteAccount(account.getId());
                    setAdapter(true);
                    view.showToast("删除成功！");
                }
            });
        }
    };
}
