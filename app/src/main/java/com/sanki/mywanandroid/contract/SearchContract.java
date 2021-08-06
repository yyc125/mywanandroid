package com.sanki.mywanandroid.contract;


import com.sanki.mywanandroid.base.BaseContract;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.HotKey;

import java.util.List;

public interface SearchContract {

    interface View extends BaseContract.View{

        void showNoSearchResultView();
        void showHotKey(List<HotKey> hotKeyList);
        void showArticleList(List<ArticleInfo> articleInfoList, boolean isEnd);

    }

    interface Presenter extends BaseContract.Presenter<View>{
        void getHotKey();
        void getSearchList(int page,String keyword);
    }
}
