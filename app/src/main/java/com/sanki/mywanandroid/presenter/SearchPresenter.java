package com.sanki.mywanandroid.presenter;

import com.sanki.mywanandroid.base.BasePresenter;
import com.sanki.mywanandroid.bean.Article;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.BaseResponse;
import com.sanki.mywanandroid.bean.HotKey;
import com.sanki.mywanandroid.contract.SearchContract;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.utils.LogUtils;

import java.util.List;
import java.util.logging.Logger;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public SearchPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void getHotKey() {
        Observable<BaseResponse<List<HotKey>>> observable = dataManager.getHotKey();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<HotKey>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<List<HotKey>> resp) {
                        if (resp.getErrorCode() == 0) {
                            getView().showHotKey(resp.getData());
                        } else {
                            getView().showError(resp.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getSearchList(int page, String keyword) {
        Observable<BaseResponse<Article>> observable = dataManager.getSearchList(page, keyword);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<Article> articleBaseResponse) {
                        if (articleBaseResponse.getErrorCode() != 0) {
                            getView().showError(articleBaseResponse.getErrorMsg());
                            return;
                        }
                        List<ArticleInfo> articleInfoList = articleBaseResponse.getData().getDatas();
                        if (articleInfoList.isEmpty()) {
                            getView().showNoSearchResultView();
                        } else {
                            LogUtils.d(articleInfoList.toString());
                            getView().showArticleList(articleInfoList, articleBaseResponse.getData().isOver());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void detachView() {
        super.detachView();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
