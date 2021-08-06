package com.sanki.mywanandroid.presenter;

import com.sanki.mywanandroid.app.MyApp;
import com.sanki.mywanandroid.base.BasePresenter;
import com.sanki.mywanandroid.bean.BaseResponse;
import com.sanki.mywanandroid.bean.Login;
import com.sanki.mywanandroid.constants.Constants;
import com.sanki.mywanandroid.contract.LoginContract;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.utils.SPUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private Disposable disposable;
    private DataManager dataManager;

    public LoginPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void login(final String username, final String password) {
        Observable<BaseResponse<Login>> observable = dataManager.login(username,password);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Login>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<Login> resp) {
                        if(resp.getErrorCode() == 0){
                            getView().showUsername(resp.getData());
                            //保存用户名和密码，以便下次自动登录
                            SPUtils.put(MyApp.getContext(), Constants.USERNAME,username);
                            SPUtils.put(MyApp.getContext(), Constants.PASSWORD,password);
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
    public void register(final String username, final String password, String repassword) {
        Observable<BaseResponse<Login>> observable = dataManager.register(username,password,repassword);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Login>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(BaseResponse<Login> resp) {
                        if(resp.getErrorCode() == 0){
                            getView().showUsername(resp.getData());
                            //保存用户名和密码，以便下次自动登录
                            SPUtils.put(MyApp.getContext(), Constants.USERNAME,username);
                            SPUtils.put(MyApp.getContext(), Constants.PASSWORD,password);
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
    public void logout() {
        Observable<BaseResponse> observable = dataManager.logout();
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
                            getView().logoutSuccess();
                            //清除缓存的用户名和密码
                            SPUtils.remove(MyApp.getContext(),Constants.USERNAME);
                            SPUtils.remove(MyApp.getContext(),Constants.PASSWORD);
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
