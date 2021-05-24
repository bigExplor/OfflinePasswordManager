package com.example.accountmanager.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accountmanager.R;
import com.example.accountmanager.activities.MainActivity;

public class ChestFragment extends Fragment {
    private MainActivity mActivity;
    private View view;
    private EditText et_key;
    private Button btn_run;

    public ChestFragment(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_chest, container, false);
        initView();
        initListener();
        return view;
    }

    private void initView() {
        et_key = view.findViewById(R.id.et_key);
        btn_run = view.findViewById(R.id.btn_run);
    }

    private void initListener() {
        btn_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = et_key.getText().toString();
                if (TextUtils.isEmpty(key)) return;
                boolean success = mActivity.parse(key);
                if (success) et_key.setText("");
            }
        });
    }

    public void onShow() {

    }
}
