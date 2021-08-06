package com.sanki.mywanandroid.api;

public interface ObserverResponseListener<T> {
    void onNext(T o);
    void onError(Throwable e);
}
