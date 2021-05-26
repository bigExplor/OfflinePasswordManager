package com.example.accountmanager.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountmanager.R;
import com.example.accountmanager.activities.AccountBookActivity;
import com.example.accountmanager.activities.MainActivity;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.utils.Constants;

import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {
    private MainActivity mActivity;
    private LayoutInflater inflater;
    private List<Type> typeList;

    public TypeAdapter(MainActivity mActivity, List<Type> types) {
        this.mActivity = mActivity;
        this.inflater = LayoutInflater.from(mActivity);
        this.typeList = types;
    }

    public void setTypes(List<Type> list) {
        this.typeList.clear();
        this.typeList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_type_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Type type = typeList.get(position);
        holder.iv_cover.setImageResource(type.getImgId());
        holder.tv_name.setText(type.getName());
        holder.tv_size.setText("" + type.getAccounts().size());
        int drawable = R.drawable.shape_size;
        holder.tv_size.setBackgroundDrawable(mActivity.getResources().getDrawable(drawable));
        holder.ll_container.setOnClickListener(v -> {
            if (type.getId() == Constants.privateId) {
                if (!Constants.hasIntoPrivateSpace) {
                    mActivity.showBiometric(isSuccess -> {
                        if (isSuccess) {
                            Constants.hasIntoPrivateSpace = true;
                            Intent intent = new Intent(mActivity, AccountBookActivity.class);
                            intent.putExtra("id", type.getId());
                            intent.putExtra("name", type.getName());
                            mActivity.startActivity(intent);
                        }
                    });
                    return;
                }
            }
            Intent intent = new Intent(mActivity, AccountBookActivity.class);
            intent.putExtra("id", type.getId());
            intent.putExtra("name", type.getName());
            mActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_cover;
        TextView tv_name;
        TextView tv_size;
        LinearLayout ll_container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_cover = itemView.findViewById(R.id.iv_cover);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_size = itemView.findViewById(R.id.tv_size);
            ll_container = itemView.findViewById(R.id.ll_container);
        }
    }
}
