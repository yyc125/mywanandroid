package com.sanki.mywanandroid.presenter;

import com.sanki.mywanandroid.base.BasePresenter;
import com.sanki.mywanandroid.bean.Article;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.Banner;
import com.sanki.mywanandroid.bean.BaseResponse;
import com.sanki.mywanandroid.contract.ArticleContract;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.utils.LogUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticlePresenter extends BasePresenter<ArticleContract.View> implements ArticleContract.Presenter {
    private Disposable disposable;
    private DataManager dataManager;

    public ArticlePresenter(DataManager dataManager) {

        this.dataManager = dataManager;
    }

    @Override
    public void getArticleList(int page) {
        Observable<BaseResponse<Article>> observable = dataManager.getArticleList(page);
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
                        getView().showArticleList(articleBaseResponse.getData().getDatas(), articleBaseResponse.getData().isOver());
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
    public void getTopArticleList() {
        Observable<BaseResponse<List<ArticleInfo>>> topArticleList = dataManager.getTopArticleList();
           topArticleList.subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new Observer<BaseResponse<List<ArticleInfo>>>() {
                       @Override
                       public void onSubscribe(Disposable d) {
                           disposable = d;
                       }

                       @Override
                       public void onNext(BaseResponse<List<ArticleInfo>> listBaseResponse) {
                           if(listBaseResponse.getErrorCode() != 0){
                               getView().showError(listBaseResponse.getErrorMsg());
                               return;
                           }
                           getView().showTopArticleList(listBaseResponse.getData());
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
    public void getBanner() {
        Observable<BaseResponse<List<Banner>>> observable = dataManager.getBannerList();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<Banner>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<List<Banner>> resp) {
                        if(resp.getErrorCode() != 0){
                            getView().showError(resp.getErrorMsg());
                            return;
                        }
                        List<Banner> bannerList = resp.getData();
                        if(!bannerList.isEmpty()){
                            getView().showBanner(bannerList);
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
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
