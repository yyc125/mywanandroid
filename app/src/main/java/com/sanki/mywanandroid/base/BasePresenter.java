package com.sanki.mywanandroid.base;

public class BasePresenter<V extends BaseContract.View> implements BaseContract.Presenter<V> {

    private V view;

    public BasePresenter() {

    }

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public V getView() {
        return view;
    }
}
