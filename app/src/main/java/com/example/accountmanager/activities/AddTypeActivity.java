package com.example.accountmanager.activities;

import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountmanager.R;
import com.example.accountmanager.adapters.ChooseTypeImgAdapter;
import com.example.accountmanager.base.BaseActivity;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.bean.TypeImg;
import com.example.accountmanager.dao.TypeDao;
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
    private List<TypeImg> list = new ArrayList<TypeImg>() {{
        add(new TypeImg(R.drawable.game_cover, false));
        add(new TypeImg(R.drawable.work_cover, false));
        add(new TypeImg(R.drawable.happy_cover, false));
        add(new TypeImg(R.drawable.chat_cover, false));
        add(new TypeImg(R.drawable.education_cover, false));
        add(new TypeImg(R.drawable.xiaolv_cover, false));
        add(new TypeImg(R.drawable.tools_cover, false));
        add(new TypeImg(R.drawable.others_cover, false));
    }};

    private final TypeDao typeDao = new TypeDao();

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
        ChooseTypeImgAdapter adapter = new ChooseTypeImgAdapter(list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mRecyclerView.setAdapter(adapter);
        mButton.setOnClickListener(v -> {
            String str = mEtType.getText().toString();
            if ("".equals(str.trim())) {
                showToast("请填写分类名称");
                return;
            }
            int imgId = -1;
            for (TypeImg img: list) {
                if (img.hasChosen) {
                    imgId = img.imgId;
                    break;
                }
            }
            if (imgId < 0) {
                showToast("请选择分类图标");
            }
            Type type = new Type();
            type.setName(str);
            type.setImgId(imgId);
            if (!typeDao.addType(type)) {
                showToast("添加失败！请检查分类名是否重复");
            } else {
                showToast("添加成功!");
                finish();
            }
        });
    }
}