package com.example.accountmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountmanager.R;
import com.example.accountmanager.bean.TypeImg;

import java.util.List;

/**
 * @author CharlesLu
 * @date 2021/5/25 15:14
 * @description
 */
public class ChooseTypeImgAdapter extends RecyclerView.Adapter<ChooseTypeImgAdapter.ViewHolder> {
    private Context mContext;
    private final List<TypeImg> list;

    public ChooseTypeImgAdapter(List<TypeImg> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_choose_type_img, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TypeImg img = list.get(position);
        holder.ivCover.setImageDrawable(mContext.getDrawable(img.imgId));
        if (img.hasChosen) {
            holder.ivChosen.setImageDrawable(mContext.getDrawable(R.drawable.chosen));
        } else {
            holder.ivChosen.setImageDrawable(mContext.getDrawable(R.drawable.no_chosen));
        }
        holder.rlContainer.setOnClickListener(v -> {
            boolean currentState = img.hasChosen;
            for (TypeImg item : list) {
                item.hasChosen = false;
            }
            img.hasChosen = !currentState;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCover;
        public ImageView ivChosen;
        public RelativeLayout rlContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            ivChosen = itemView.findViewById(R.id.iv_chosen);
            rlContainer = itemView.findViewById(R.id.rl_container);
        }
    }
}