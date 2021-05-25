package com.example.accountmanager.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.accountmanager.R;

public class TitleBar extends RelativeLayout {

    private ImageView iv_left;
    private ImageView iv_right;
    private ImageView iv_right2;
    private ImageView iv_right3;
    private TextView tv_title;
    private RelativeLayout rl_container;

    private Context context;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this);
        this.context = context;
        initView();
    }

    private void initView() {
        iv_left = findViewById(R.id.iv_left);
        iv_right = findViewById(R.id.iv_right);
        iv_right2 = findViewById(R.id.iv_right2);
        iv_right3 = findViewById(R.id.iv_right3);
        tv_title = findViewById(R.id.tv_title);
        rl_container = findViewById(R.id.rl_container);
    }

    public void setText(String text) {
        if (tv_title != null) {
            tv_title.setText(text);
        }
    }

    public void setBackground(int color) {
        if (rl_container != null) {
            rl_container.setBackgroundColor(context.getResources().getColor(color));
        }
    }

    public void setLeft(int resource, OnClickListener listener) {
        if (iv_left != null) {
            iv_left.setVisibility(View.VISIBLE);
            iv_left.setImageResource(resource);
            iv_left.setOnClickListener(listener);
        }
    }

    public void setRight(int resource, OnClickListener listener) {
        if (iv_right != null) {
            iv_right.setVisibility(View.VISIBLE);
            iv_right.setImageResource(resource);
            iv_right.setOnClickListener(listener);
        }
    }

    public void setRightVisibility(boolean visibility) {
        if (visibility) iv_right.setVisibility(View.VISIBLE);
        else iv_right.setVisibility(View.GONE);
    }

    public void setRight2(int resource, OnClickListener listener) {
        if (iv_right2 != null) {
            iv_right2.setVisibility(View.VISIBLE);
            iv_right2.setImageResource(resource);
            iv_right2.setOnClickListener(listener);
        }
    }

    public void setRight3(int resource, OnClickListener listener) {
        if (iv_right3 != null) {
            iv_right3.setVisibility(View.VISIBLE);
            iv_right3.setImageResource(resource);
            iv_right3.setOnClickListener(listener);
        }
    }
}
