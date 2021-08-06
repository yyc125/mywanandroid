package com.sanki.mywanandroid.contract;

import com.sanki.mywanandroid.base.BaseContract;
import com.sanki.mywanandroid.bean.NavTitle;

import java.util.List;

public interface NavContract {

    interface View extends BaseContract.View{
        void showNavInfo(List<NavTitle> navTitleList);
    }

    interface Presenter extends BaseContract.Presenter<NavContract.View>{
        void getNavInfo();
    }
}
