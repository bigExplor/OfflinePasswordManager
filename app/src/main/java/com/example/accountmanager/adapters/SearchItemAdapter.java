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
import com.example.accountmanager.activities.SearchActivity;
import com.example.accountmanager.bean.Account;

import java.util.ArrayList;
import java.util.List;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {
    private SearchActivity mActivity;
    private List<Account> accountList;
    private OnItemClickListener listener;
    private LayoutInflater layoutInflater;

    public SearchItemAdapter(SearchActivity mActivity, List<Account> accountList, OnItemClickListener listener) {
        this.mActivity = mActivity;
        this.accountList = new ArrayList<>();
        this.accountList.addAll(accountList);
        this.listener = listener;
        this.layoutInflater = LayoutInflater.from(mActivity);
    }

    public void setData(List<Account> accountList) {
        this.accountList.clear();
        this.accountList.addAll(accountList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_account_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = accountList.get(position);
        String title = account.getTitle();
        if (title.length() > 8) title = title.substring(0, 8) + "...";
        holder.tv_title.setText(title);
        holder.tv_tag.setText(account.getType().getName());
        holder.tv_account.setText(account.getAccount());
        holder.tv_password.setText(account.getPassword());

        holder.iv_go.setOnClickListener(v -> listener.onGo(position));
        holder.iv_share.setOnClickListener(v -> listener.onShare(position));
        holder.iv_copy_account.setOnClickListener(v -> copy(account.getAccount()));
        holder.iv_copy_password.setOnClickListener(v -> copy(account.getPassword()));
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    private void copy(String msg) {
        if (TextUtils.isEmpty(msg) || "暂无提供".equals(msg)) {
            mActivity.showToast("该字段为空，不可复制");
            return;
        }
        mActivity.copy(msg);
        mActivity.showToast("复制成功");
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_tag;
        TextView tv_account;
        TextView tv_password;

        ImageView iv_go;
        ImageView iv_share;
        ImageView iv_copy_account;
        ImageView iv_copy_password;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_account = itemView.findViewById(R.id.tv_account);
            tv_password = itemView.findViewById(R.id.tv_password);
            tv_tag = itemView.findViewById(R.id.tv_tag);
            tv_title = itemView.findViewById(R.id.tv_title);
            iv_go = itemView.findViewById(R.id.iv_go);
            iv_share = itemView.findViewById(R.id.iv_share);
            iv_copy_account = itemView.findViewById(R.id.iv_copy_account);
            iv_copy_password = itemView.findViewById(R.id.iv_copy_password);
        }
    }

    public interface OnItemClickListener {
        void onGo(int position);
        void onShare(int position);
    }
}
