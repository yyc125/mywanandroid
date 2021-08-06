package com.sanki.mywanandroid.contract;


import com.sanki.mywanandroid.base.BaseContract;
import com.sanki.mywanandroid.bean.MyIntegral;

public interface IntegralContract {

    interface View extends BaseContract.View{
        void showMyIntegral(MyIntegral myIntegral);
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void getMyIntegral();
    }
}
