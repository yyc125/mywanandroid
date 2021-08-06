package com.sanki.mywanandroid.presenter;

import com.sanki.mywanandroid.base.BasePresenter;
import com.sanki.mywanandroid.bean.Article;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.BaseResponse;
import com.sanki.mywanandroid.contract.CollectContract;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.utils.LogUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CollectPresenter extends BasePresenter<CollectContract.View> implements CollectContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public CollectPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void getCollectList(int page) {
        //LogUtils.d("pppp",page+"-----");
        Observable<BaseResponse<Article>> observable = dataManager.getCollectList(page);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<Article> articleBaseResponse) {
                       // LogUtils.d("pppp",articleBaseResponse.getErrorMsg());
                        if(articleBaseResponse.getErrorCode() != 0){
                            getView().showError(articleBaseResponse.getErrorMsg());
                            return;
                        }
                        List<ArticleInfo> articleInfoList = articleBaseResponse.getData().getDatas();
                        if(articleInfoList == null){
                            getView().showNoCollectView();
                        }else{
                            getView().showArticleList(articleInfoList,articleBaseResponse.getData().isOver());
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
    public void collectArticle(int id) {
        LogUtils.d("pppp","======"+id);
        Observable<BaseResponse> observable = dataManager.collectArticle(id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse resp) {

                        if(resp.getErrorCode() == 0){
                            getView().showCollectSuccess();
                        }else{
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
    public void cancelCollectArticle(int id,int originId) {
        Observable<BaseResponse> observable = dataManager.cancelCollectArticle(id,originId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse resp) {
                        if(resp.getErrorCode() == 0){
                            getView().showCancelCollectSuccess();
                        }else{
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
    public void detachView() {
        super.detachView();
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
