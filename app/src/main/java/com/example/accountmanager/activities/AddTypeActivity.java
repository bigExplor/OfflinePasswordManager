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
import com.example.accountmanager.bean.Account;
import com.example.accountmanager.bean.Type;
import com.example.accountmanager.bean.TypeImg;
import com.example.accountmanager.dao.AccountDao;
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
    private Button mConfirmBtn;
    private Button mCancelBtn;
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

    private Type type = new Type();
    private final TypeDao typeDao = new TypeDao();
    private final AccountDao accountDao = new AccountDao();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_type;
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
        if (!TextUtils.isEmpty(from) && from.equals("setting")) {
            int id = getIntent().getIntExtra("id", -1);
            String name = getIntent().getStringExtra("name");
            mEtType.setText(name);
            mConfirmBtn.setText("修改");
            mTitleBar.setText("编辑分类");
            type = typeDao.getTypeById(id);
            if (id < 0 || TextUtils.isEmpty(name) || type == null) {
                showToast("参数错误");
                finish();
                return;
            }
            for (TypeImg img: list) {
                if (img.imgId == type.getImgId()) {
                    img.hasChosen = true;
                    break;
                }
            }
            mCancelBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initListener() {
        mTitleBar.setLeft(R.drawable.left, v -> finish());
        ChooseTypeImgAdapter adapter = new ChooseTypeImgAdapter(list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        mRecyclerView.setAdapter(adapter);
        mConfirmBtn.setOnClickListener(v -> {
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
            type.setName(str);
            type.setImgId(imgId);
            if (type.getId() > 0) {
                typeDao.updateType(type);
                showToast("修改成功！");
                finish();
            } else {
                if (!typeDao.addType(type)) {
                    showToast("添加失败！请检查分类名是否重复");
                } else {
                    showToast("添加成功!");
                    finish();
                }
            }
        });
        mCancelBtn.setOnClickListener(v -> {
            List<Account> accounts = accountDao.getAccountByType(type.getId());
            String msg = "";
            if (accounts.size() > 0) {
                msg = "当前分类存有账号信息，删除将不可找回，确认删除？";
            } else {
                msg = "确认删除当前分类？";
            }
            showConfirm(msg, isOk -> {
                if (isOk) {
                    typeDao.removeType(type.getId());
                    showToast("删除成功！");
                    finish();
                }
            });
        });
    }
}