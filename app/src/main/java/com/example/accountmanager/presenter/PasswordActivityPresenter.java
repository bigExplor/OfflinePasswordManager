package com.example.accountmanager.presenter;

import android.content.Intent;

import com.example.accountmanager.activities.MainActivity;
import com.example.accountmanager.activities.PasswordActivity;
import com.example.accountmanager.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CharlesLu
 * @date 2021/5/27 12:55
 * @description
 */
public class PasswordActivityPresenter implements BasePresenter<PasswordActivity> {
    private PasswordActivity view;

    private final List<Integer> pwd = new ArrayList<>();

    @Override
    public void bindView(PasswordActivity view) {
        this.view = view;
    }

    /* 检查二级密码才能进入 */
    public void check() {
        StringBuilder ans = new StringBuilder();
        for (Integer i: pwd) ans.append(i);
        if (ans.toString().equals(SpUtil.getInstance().getString("password"))) {
            view.showToast("验证通过");
            Intent intent = new Intent(view, MainActivity.class);
            view.startActivity(intent);
            view.finish();
            return;
        }
        pwd.clear();
        view.setChosen(0);
        view.showToast("密码错误");
    }

    /* 处理点击事件 */
    public void onClick(int position, String name) {
        if (position <= 9 && position >=0) {
            if (pwd.size() >= 4) return;
            pwd.add(position);
            view.setChosen(pwd.size());
            if (pwd.size() == 4) check();
            return;
        }
        switch (name) {
            case "delete":
                if (pwd.size() <= 0) return;
                pwd.remove(pwd.size() - 1);
                view.setChosen(pwd.size());
                break;
            case "finger":
                view.showBiometric(isSuccess -> {
                    if (isSuccess) {
                        Intent intent = new Intent(view, MainActivity.class);
                        view.startActivity(intent);
                        view.finish();
                    }
                });
                break;
        }
    }
}
