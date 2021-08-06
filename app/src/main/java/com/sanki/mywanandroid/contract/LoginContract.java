package com.sanki.mywanandroid.contract;


import com.sanki.mywanandroid.base.BaseContract;
import com.sanki.mywanandroid.bean.Login;

public interface LoginContract {

    interface View extends BaseContract.View{
        void showUsername(Login login);
        void logoutSuccess();
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void login(String username, String password);
        void register(String username, String password, String repassword);
        void logout();
    }
}
