package com.example.accountmanager.activities;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountmanager.R;
import com.example.accountmanager.adapters.ChooseTypeImgAdapter;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.bean.TypeImg;
import com.example.accountmanager.presenter.AddTypeActivityPresenter;
import com.example.accountmanager.ui.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CharlesLu
 * @description 添加类别的界面
 */
public class AddTypeActivity extends BaseActivity<AddTypeActivityPresenter> {

    private TitleBar mTitleBar;
    private EditText mEtType;
    private Button mConfirmBtn;
    private Button mCancelBtn;
    private RecyclerView mRecyclerView;

    public int currentId = -1;
    public Type type = new Type();

    private final List<TypeImg> list = new ArrayList<TypeImg>() {{
        add(new TypeImg(R.drawable.game_cover, false));
        add(new TypeImg(R.drawable.work_cover, false));
        add(new TypeImg(R.drawable.happy_cover, false));
        add(new TypeImg(R.drawable.chat_cover, false));
        add(new TypeImg(R.drawable.education_cover, false));
        add(new TypeImg(R.drawable.xiaolv_cover, false));
        add(new TypeImg(R.drawable.tools_cover, false));
        add(new TypeImg(R.drawable.others_cover, false));
    }};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_type;
    }

    @Override
    protected AddTypeActivityPresenter getPresenter() {
        return new AddTypeActivityPresenter();
    }

    @Override
    protected void initView() {
        mTitleBar = findViewById(R.id.titleBar);
        mEtType = findViewById(R.id.et_type);
        mConfirmBtn = findViewById(R.id.confirm_btn);
        mCancelBtn = findViewById(R.id.cancel_btn);
        mRecyclerView = findViewById(R.id.rv_list);
    }

    @Override
    protected void initData() {
        mTitleBar.setText("添加分类");
        String from = getIntent().getStringExtra("from");
        currentId = getIntent().getIntExtra("id", -1);
        if (!TextUtils.isEmpty(from) && from.equals("setting")) {
            mConfirmBtn.setText("修改");
            mTitleBar.setText("编辑分类");
            mCancelBtn.setVisibility(View.VISIBLE);
            if (p.initSetting()) {
                mEtType.setText(type.getName());
                for (TypeImg img: list) {
                    if (img.imgId == type.getImgId()) {
                        img.hasChosen = true;
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void initListener() {
        mTitleBar.setLeft(R.drawable.left, v -> finish());
        ChooseTypeImgAdapter adapter = new ChooseTypeImgAdapter(list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mRecyclerView.setAdapter(adapter);
        mConfirmBtn.setOnClickListener(v -> {
            int imgId = -1;
            for (TypeImg img: list) {
                if (img.hasChosen) {
                    imgId = img.imgId;
                    break;
                }
            }
            type.setImgId(imgId);
            type.setName(mEtType.getText().toString().trim());
            p.commit();
        });
        mCancelBtn.setOnClickListener(v -> p.delete());
    }
}