package com.sanki.mywanandroid.contract;

import com.sanki.mywanandroid.base.BaseContract;
import com.sanki.mywanandroid.bean.System;

import java.util.List;

public interface SystemContract {
    interface View extends BaseContract.View {
        void showSystemList(List<System> systemList);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void getSystemList();
    }

}
