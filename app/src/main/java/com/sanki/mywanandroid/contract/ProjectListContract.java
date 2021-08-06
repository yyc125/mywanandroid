package com.sanki.mywanandroid.contract;

import com.sanki.mywanandroid.base.BaseContract;
import com.sanki.mywanandroid.bean.ArticleInfo;

import java.util.List;

public interface ProjectListContract {

    interface View extends BaseContract.View{
        void showProjectList(List<ArticleInfo> projectList, boolean isEnd);
    }

    interface Presenter extends BaseContract.Presenter<View>{
        void getProjectList(int page,int cid,boolean isSystem);
    }
}
