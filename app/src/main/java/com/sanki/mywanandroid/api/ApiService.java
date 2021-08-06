package com.sanki.mywanandroid.api;

import com.sanki.mywanandroid.bean.Article;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.Banner;
import com.sanki.mywanandroid.bean.BaseResponse;
import com.sanki.mywanandroid.bean.HotKey;
import com.sanki.mywanandroid.bean.Login;
import com.sanki.mywanandroid.bean.MyIntegral;
import com.sanki.mywanandroid.bean.NavTitle;
import com.sanki.mywanandroid.bean.ProjectCategory;
import com.sanki.mywanandroid.bean.System;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 首页文章列表
     *
     * @param page
     * @return
     */
    @GET("/article/list/{page}/json")
    Observable<BaseResponse<Article>> getArticleList(@Path("page") int page);

    /**
     * 获取置顶文章
     *
     * @return
     */
    @GET("/article/top/json")
    Observable<BaseResponse<List<ArticleInfo>>> getTopArticleList();

    /**
     * banner
     *
     * @return
     */
    @GET("/banner/json")
    Observable<BaseResponse<List<Banner>>> getBanner();

    /**
     * 搜索
     *
     * @param page
     * @param keyword
     * @return
     */
    @POST("/article/query/{page}/json")
    Observable<BaseResponse<Article>> getSearchList(@Path("page") int page, @Query("k") String keyword);

    /**
     * 搜索热词
     *
     * @return
     */
    @GET("/hotkey/json")
    Observable<BaseResponse<List<HotKey>>> getHotKey();

    /**
     * 项目分类
     *
     * @return
     */
    @GET("/project/tree/json")
    Observable<BaseResponse<List<ProjectCategory>>> getProjectCategory();

    /**
     * 项目列表数据
     *
     * @return
     */
    @GET("/project/list/{page}/json")
    Observable<BaseResponse<Article>> getProjectList(@Path("page") int page, @Query("cid") int cid);

    /**
     * 获取体系下的文章
     *
     * @param page
     * @param cid
     * @return
     */
    @GET("/article/list/{page}/json")
    Observable<BaseResponse<Article>> getSystemArticleList(@Path("page") int page, @Query("cid") int cid);

    /**
     * 体系数据
     *
     * @return
     */
    @GET("/tree/json")
    Observable<BaseResponse<List<System>>> getSystemInfo();

    /**
     * 导航数据
     *
     * @return
     */
    @GET("/navi/json")
    Observable<BaseResponse<List<NavTitle>>> getNavInfo();

    /**
     *注册
     * @return
     */
    @POST("/user/register")
    Observable<BaseResponse<Login>> register(@Query("username") String username, @Query("password") String password, @Query("repassword") String repassword);
    /**
     *登录
     * @return
     */
    @POST("/user/login")
    Observable<BaseResponse<Login>> login(@Query("username") String username,@Query("password") String password);

    /**
     *注销
     * @return
     */
    @GET("/user/logout/json")
    Observable<BaseResponse> logout();

    /**
     * 获取收藏文章列表
     * @param page
     * @return
     */
    @GET("/lg/collect/list/{page}/json")
    Observable<BaseResponse<Article>> getCollectList(@Path("page") int page);

    /**
     * 收藏文章
     * @param id
     * @return
     */
    @POST("/lg/collect/{id}/json")
    Observable<BaseResponse> collectArticle(@Path("id") int id);

    /**
     * 取消收藏文章
     * @param id
     * @param originId
     * @return
     */
    @POST("/lg/uncollect/{id}/json")
    Observable<BaseResponse> cancelCollectArticle(@Path("id") int id,@Query("originId") int originId);

    /**
     * 获取个人积分
     * @return
     */
    @GET("/lg/coin/userinfo/json")
    Observable<BaseResponse<MyIntegral>> getMyIntegral();
}
