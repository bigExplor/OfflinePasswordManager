package com.example.accountmanager.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountmanager.R;
import com.example.accountmanager.activities.AccountBookActivity;
import com.example.accountmanager.bean.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private final OnItemClickListener listener;
    private final AccountBookActivity mActivity;
    private final List<Account> accounts = new ArrayList<>();

    public AccountAdapter(AccountBookActivity mActivity, List<Account> accounts, OnItemClickListener listener) {
        this.mActivity = mActivity;
        this.listener = listener;
        this.accounts.addAll(accounts);
    }

    public void setAccounts(List<Account> list) {
        accounts.clear();
        accounts.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) { // 精简模式
            view = LayoutInflater.from(mActivity).inflate(R.layout.layout_account_item, parent, false);
        } else { // 完全模式
            view = LayoutInflater.from(mActivity).inflate(R.layout.layout_account_item_more, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Account account = accounts.get(position);
        String title = account.getTitle();
        if (title.length() > 8) title = title.substring(0, 8) + "...";
        holder.tv_title.setText(title);
        String username = account.getUsername();
        if ("暂无提供".equals(username)) {
            username = "";
        }
        holder.tv_username.setText(username);
        holder.tv_account.setText(account.getAccount());
        holder.tv_password.setText(account.getPassword());

        if (getItemViewType(position) == 1) {
            holder.tv_mail.setText(account.getMail());
            holder.tv_phone.setText(account.getPhone());
            holder.tv_note.setText(account.getNote());
            holder.tv_url.setText(account.getUrl());
        }

        holder.iv_mode.setOnClickListener(v -> listener.onMode(position));
        holder.iv_share.setOnClickListener(v -> listener.onShare(position));
        holder.iv_edit.setOnClickListener(v -> listener.onEdit(position));
        holder.iv_delete.setOnClickListener(v -> listener.onDelete(position));
        holder.iv_copy_account.setOnClickListener(v -> copy(account.getAccount()));
        holder.iv_copy_password.setOnClickListener(v -> copy(account.getPassword()));
        if (getItemViewType(position) == 1) {
            holder.iv_copy_mail.setOnClickListener(v -> copy(account.getMail()));
            holder.iv_copy_phone.setOnClickListener(v -> copy(account.getPhone()));
            holder.iv_copy_url.setOnClickListener(v -> copy(account.getUrl()));
        }
    }

    private void copy(String msg) {
        if (TextUtils.isEmpty(msg) || "暂无提供".equals(msg)) {
            mActivity.showToast("该字段为空，不可复制");
            return;
        }
        mActivity.copy(msg);
        mActivity.showToast("复制成功");
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return accounts.get(position).getMode();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_username;
        TextView tv_account;
        TextView tv_password;
        TextView tv_mail;
        TextView tv_phone;
        TextView tv_note;
        TextView tv_url;

        ImageView iv_mode;
        ImageView iv_share;
        ImageView iv_edit;
        ImageView iv_delete;

        ImageView iv_copy_account;
        ImageView iv_copy_password;
        ImageView iv_copy_mail;
        ImageView iv_copy_phone;
        ImageView iv_copy_url;

        public ViewHolder(@NonNull View itemView, int mode) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_account = itemView.findViewById(R.id.tv_account);
            tv_password = itemView.findViewById(R.id.tv_password);
            iv_mode = itemView.findViewById(R.id.iv_mode);
            iv_share = itemView.findViewById(R.id.iv_share);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_copy_account = itemView.findViewById(R.id.iv_copy_account);
            iv_copy_password = itemView.findViewById(R.id.iv_copy_password);
            if (mode == 1) {
                tv_mail = itemView.findViewById(R.id.tv_mail);
                tv_phone = itemView.findViewById(R.id.tv_phone);
                tv_note = itemView.findViewById(R.id.tv_note);
                tv_url = itemView.findViewById(R.id.tv_url);
                iv_copy_mail = itemView.findViewById(R.id.iv_copy_mail);
                iv_copy_phone = itemView.findViewById(R.id.iv_copy_phone);
                iv_copy_url = itemView.findViewById(R.id.iv_copy_url);
            }
        }
    }

    public interface OnItemClickListener {
        void onMode(int position);
        void onShare(int position);
        void onEdit(int position);
        void onDelete(int position);
    }
}
