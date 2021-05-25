package com.example.accountmanager.activities;

import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountmanager.R;
import com.example.accountmanager.adapters.ChooseTypeImgAdapter;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.bean.TypeImg;
import com.example.accountmanager.ui.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CharlesLu
 * @description 添加类别的界面
 */
public class AddTypeActivity extends BaseActivity {

    private TitleBar mTitleBar;
    private EditText mEtType;
    private Button mButton;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_type;
    }

    @Override
    protected void initView() {
        mTitleBar = findViewById(R.id.titleBar);
        mEtType = findViewById(R.id.et_type);
        mButton = findViewById(R.id.btn);
        mRecyclerView = findViewById(R.id.rv_list);
    }

    @Override
    protected void initData() {
        mTitleBar.setText("添加分类");
    }

    @Override
    protected void initListener() {
        mTitleBar.setLeft(R.drawable.left, v -> finish());
        List<TypeImg> list = new ArrayList<TypeImg>() {{
            add(new TypeImg(R.drawable.game_cover, false));
            add(new TypeImg(R.drawable.work_cover, false));
            add(new TypeImg(R.drawable.happy_cover, false));
            add(new TypeImg(R.drawable.chat_cover, false));
            add(new TypeImg(R.drawable.education_cover, false));
            add(new TypeImg(R.drawable.xiaolv_cover, false));
            add(new TypeImg(R.drawable.tools_cover, false));
            add(new TypeImg(R.drawable.others_cover, false));
        }};
        ChooseTypeImgAdapter adapter = new ChooseTypeImgAdapter(list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mRecyclerView.setAdapter(adapter);
    }
}