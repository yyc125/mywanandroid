package com.sanki.mywanandroid.contract;

import com.sanki.mywanandroid.base.BaseContract;
import com.sanki.mywanandroid.bean.ProjectCategory;

import java.util.List;

public interface ProjectContract {
    interface View extends BaseContract.View {

        void showProjectCategory(List<ProjectCategory> categoryList);
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void getProjectCategory();
    }
}
