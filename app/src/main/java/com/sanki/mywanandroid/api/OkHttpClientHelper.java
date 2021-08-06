package com.sanki.mywanandroid.api;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.sanki.mywanandroid.app.MyApp;
import com.sanki.mywanandroid.utils.FileUtils;
import com.sanki.mywanandroid.utils.LogUtils;
import com.sanki.mywanandroid.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @作者JTL.
 * @日期2018/3/15.
 * @说明：OkHttpClient帮助类
 */

public class OkHttpClientHelper {

    private OkHttpClient okHttpClient;
    private static Context sContext;
    public static final int CONNECT_TIME_OUT = 30;
    public static final int READ_TIME_OUT = 30;
    public static final int WRITE_TIME_OUT = 30;

    private OkHttpClientHelper() {
        init();
    }

    public static OkHttpClientHelper getInstance(Context context) {
        sContext = context;
        return OkHttpClientHolder.instance;
    }

    /**
     * OkHttpClient 基本设置
     */
    private void init() {

        ClearableCookieJar cookieJar =  new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApp.getContext()));
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //添加Cache拦截器，有网时添加到缓存中，无网时取出缓存
        File file = FileUtils.getInstance().getCacheFolder();
        Cache cache = new Cache(file, 1024 * 1024 * 100);
        //添加HttpLogging拦截器，方便观察，上传和返回json
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e("okHttp:" + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        builder.addInterceptor(loggingInterceptor);

        //基本设置
        builder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .cache(cache).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtils.isNetwork(sContext)) {
                    Request newRequest = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    return chain.proceed(newRequest);
                } else {
                    int maxTime = 24 * 60 * 60;
                    Response response = chain.proceed(request);
                    Response newResponse = response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                            .removeHeader("Progma")
                            .build();
                    return newResponse;
                }
            }
        }).cookieJar(cookieJar);

        okHttpClient = builder.build();
    }

    private static class OkHttpClientHolder {
        private static OkHttpClientHelper instance = new OkHttpClientHelper();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
