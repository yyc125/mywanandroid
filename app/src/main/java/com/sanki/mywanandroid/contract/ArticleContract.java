package com.sanki.mywanandroid.contract;

import com.sanki.mywanandroid.base.BaseContract;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.Banner;

import java.util.List;

public interface ArticleContract {

    interface View extends BaseContract.View {
        void showArticleList(List<ArticleInfo> articleInfoList, boolean isEnd);

        void showTopArticleList(List<ArticleInfo> topArticleList);

        void showBanner(List<Banner> banners);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void getArticleList(int page);

        void getTopArticleList();

        void getBanner();
    }

}
