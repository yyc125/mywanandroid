package com.sanki.mywanandroid.presenter;


import com.sanki.mywanandroid.base.BasePresenter;
import com.sanki.mywanandroid.bean.BaseResponse;
import com.sanki.mywanandroid.bean.System;
import com.sanki.mywanandroid.contract.SystemContract;
import com.sanki.mywanandroid.source.DataManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SystemPresenter extends BasePresenter<SystemContract.View> implements SystemContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public SystemPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void getSystemList() {
        Observable<BaseResponse<List<System>>> observable = dataManager.getSystemInfo();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<System>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<List<System>> systemResp) {
                        if (systemResp.getErrorCode() == 0) {
                            getView().showSystemList(systemResp.getData());
                        } else {
                            getView().showError(systemResp.getErrorMsg());
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
