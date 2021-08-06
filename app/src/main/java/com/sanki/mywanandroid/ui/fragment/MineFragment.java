package com.sanki.mywanandroid.ui.fragment;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.base.BaseFragment;
import com.sanki.mywanandroid.bean.Login;
import com.sanki.mywanandroid.constants.Constants;
import com.sanki.mywanandroid.contract.LoginContract;
import com.sanki.mywanandroid.presenter.LoginPresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.ui.activity.AboutActivity;
import com.sanki.mywanandroid.ui.activity.CollectActivity;
import com.sanki.mywanandroid.ui.activity.IntegralActivity;
import com.sanki.mywanandroid.utils.LogUtils;
import com.sanki.mywanandroid.utils.SPUtils;
import com.sanki.mywanandroid.utils.ToastUtils;

public class MineFragment extends BaseFragment implements LoginContract.View, View.OnClickListener {

    private LinearLayout llUser;
    private TextView tvUsername;
    private ImageView ivIntegral;
    private AlertDialog loginDialog;
    private LinearLayout ll_collect;
    private LinearLayout ll_integral;
    private LinearLayout ll_update;
    private LinearLayout ll_about;

    private DataManager dataManager;
    private LoginPresenter loginPresenter;

    public static MineFragment newInstance() {

        return new MineFragment();

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        loginPresenter = new LoginPresenter(dataManager);
        loginPresenter.attachView(this);
    }

    @Override
    public void init() {
        llUser = view.findViewById(R.id.ll_user);
        tvUsername = view.findViewById(R.id.tv_username);
        ivIntegral = view.findViewById(R.id.iv_integral);
        ll_collect=view.findViewById(R.id.ll_collect);
        ll_integral=view.findViewById(R.id.ll_integral);
        ll_update=view.findViewById(R.id.ll_update);
        ll_about=view.findViewById(R.id.ll_about);
        ll_update.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_integral.setOnClickListener(this);
        ll_collect.setOnClickListener(this);
        llUser.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        String username = (String) SPUtils.get(context, Constants.USERNAME, "");
        String password = (String) SPUtils.get(context, Constants.PASSWORD, "");
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            tvUsername.setText(username);
        }
    }

    @Override
    public void showUsername(Login login) {
        if(login != null){
            tvUsername.setText(login.getUsername());
            if(loginDialog != null && loginDialog.isShowing()){
                loginDialog.dismiss();
            }
        }

    }

    @Override
    public void logoutSuccess() {
        tvUsername.setText("未登录");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_user:
                String username = (String) SPUtils.get(context, Constants.USERNAME, "");
                String password = (String) SPUtils.get(context, Constants.PASSWORD, "");
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    showLogoutDialog();
                } else {
                    showLoginDialog();
                }
                break;
            case R.id.ll_collect:
                Intent intent = new Intent(context, CollectActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_integral:
                Intent intent2 = new Intent(context, IntegralActivity.class);
                startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(getActivity(),ivIntegral,"integral").toBundle());
                break;
            case R.id. ll_update:
                ToastUtils.showShort("已是最新版本");
                break;
            case R.id.ll_about:
                Intent intent3 = new Intent(context, AboutActivity.class);
                startActivity(intent3);
                break;

        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("注销")
                .setMessage("确认退出登录吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginPresenter.logout();
                    }
                });
        builder.create().show();
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("登录");
        View view = LayoutInflater.from(context).inflate(R.layout.login_layout, null);
        final TextInputLayout tUsername = view.findViewById(R.id.til_username);
        final TextInputLayout tPassword = view.findViewById(R.id.til_password);
        final TextInputEditText etUsername = view.findViewById(R.id.et_username);
        final TextInputEditText etPassword = view.findViewById(R.id.et_password);
        TextView tvRegister = view.findViewById(R.id.tv_register);
        Button bnLogin = view.findViewById(R.id.bn_login);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (isValidate(tUsername, tPassword, username, password)) {
                    loginPresenter.register(username, password, password);
                }
            }
        });
        bnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (isValidate(tUsername, tPassword, username, password)) {
                    loginPresenter.login(username, password);

                }
            }
        });
        builder.setView(view);
        loginDialog = builder.create();
        loginDialog.show();
    }

    private boolean isValidate(TextInputLayout tUsername, TextInputLayout tPassword, String username, String password) {
        boolean flag = true;
        if (TextUtils.isEmpty(username)) {
            tUsername.setError("请输入用户名");
            flag = false;
        }
        if (TextUtils.isEmpty(password)) {
            tPassword.setError("请输入密码");
            flag = false;
        }
        return flag;
    }

}
