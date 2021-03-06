package com.sanki.mywanandroid.presenter;

import com.sanki.mywanandroid.base.BasePresenter;
import com.sanki.mywanandroid.bean.Article;
import com.sanki.mywanandroid.bean.BaseResponse;
import com.sanki.mywanandroid.contract.ProjectListContract;
import com.sanki.mywanandroid.source.DataManager;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectListPresenter extends BasePresenter<ProjectListContract.View> implements ProjectListContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public ProjectListPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void getProjectList(int page, int cid, boolean isSystem) {
        Observable<BaseResponse<Article>> observable;
        if (isSystem) {
            observable = dataManager.getSystemArticleList(page, cid);
        } else {
            observable = dataManager.getProjectList(page, cid);

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<Article> articleBaseResponse) {
                        if (articleBaseResponse.getErrorCode() == 0) {
                            getView().showProjectList(articleBaseResponse.getData().getDatas(), articleBaseResponse.getData().isOver());
                        } else {
                            getView().showError(articleBaseResponse.getErrorMsg());
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
