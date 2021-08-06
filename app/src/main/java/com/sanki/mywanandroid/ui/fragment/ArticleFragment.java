package com.sanki.mywanandroid.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.adapter.ArticleAdapter;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.app.ActivityCollector;
import com.sanki.mywanandroid.base.BaseFragment;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.Banner;
import com.sanki.mywanandroid.contract.ArticleContract;
import com.sanki.mywanandroid.presenter.ArticlePresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.ui.activity.SplashActivity;
import com.sanki.mywanandroid.ui.activity.WebViewActivity;
import com.sanki.mywanandroid.utils.GlideImageLoader;
import com.sanki.mywanandroid.utils.LogUtils;
import com.sanki.mywanandroid.utils.NetworkUtils;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;

public class ArticleFragment extends BaseFragment implements ArticleContract.View, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView rvArticle;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fabTop;

    private boolean isRefresh;
    private boolean isClear;
    private int page = 0;
    private ArticlePresenter articlePresenter;
    private DataManager dataManager;
    private ArticleAdapter articleAdapter;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();
    private com.youth.banner.Banner bannerView;
    private View bannerLayout;
    private TextView tv_netLoad;
    private LinearLayout ll_lode_data;


    public static ArticleFragment newInstance() {
        ArticleFragment fragment = new ArticleFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_article;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        articlePresenter = new ArticlePresenter(dataManager);
        articlePresenter.attachView(this);
    }

    @Override
    public void init() {
        rvArticle = view.findViewById(R.id.rv_article);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        fabTop = view.findViewById(R.id.fab_top);
        tv_netLoad = view.findViewById(R.id.tv_netLoad);
        ll_lode_data = view.findViewById(R.id.ll_lode_data);
        bannerLayout = LayoutInflater.from(context).inflate(R.layout.banner_layout, null);
        bannerView = bannerLayout.findViewById(R.id.banner);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        rvArticle.setLayoutManager(new LinearLayoutManager(getContext()));
        articleAdapter = new ArticleAdapter(R.layout.item_article, articleInfoList);
        articleAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rvArticle.setAdapter(articleAdapter);
        rvArticle.addOnScrollListener(scrollListener);
        refreshLayout.setRefreshing(true);
        articlePresenter.getArticleList(page);
        articlePresenter.getTopArticleList();
        articlePresenter.getBanner();
        articleAdapter.addHeaderView(bannerLayout);
        articleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleInfo articleInfo = articleInfoList.get(position);
                WebViewActivity.startAction(getContext(), articleInfo);

            }
        });
        articleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                isRefresh = false;
                page++;
                articlePresenter.getArticleList(page);


            }
        }, rvArticle);
        fabTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvArticle.smoothScrollToPosition(0);
            }
        });
        tv_netLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClear = true;
                articlePresenter.getArticleList(page);
                articlePresenter.getTopArticleList();
                articlePresenter.getBanner();
            }
        });
    }

    private final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        private boolean isDragging = false;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == SCROLL_STATE_DRAGGING) {
                isDragging = true;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (isDragging) {
                if (dy > 0) {
                    //上拉
                    fabTop.animate().scaleX(0).scaleY(0).setDuration(200).start();
                } else {
                    //下拉
                    fabTop.animate().scaleX(1).scaleY(1).setDuration(200).start();
                }
                isDragging = false;
            }
            LogUtils.d("dy" + dy);
        }
    };

    @Override
    public void showArticleList(List<ArticleInfo> list, boolean isEnd) {
        refreshLayout.setRefreshing(false);
        if (isClear) {
            refreshLayout.setVisibility(View.VISIBLE);
            ll_lode_data.setVisibility(View.GONE);
            articleInfoList.clear();
            isClear = false;
        }
        if (!isRefresh) {
            if (isEnd) {
                articleAdapter.loadMoreEnd();
            } else {
                articleAdapter.loadMoreComplete();
            }
        }
        articleInfoList.addAll(list);
        articleAdapter.notifyDataSetChanged();

    }

    @Override
    public void showTopArticleList(List<ArticleInfo> topArticleList) {
        if (isClear) {
            refreshLayout.setVisibility(View.VISIBLE);
            ll_lode_data.setVisibility(View.GONE);
            articleInfoList.clear();
            isClear = false;
        }
        articleInfoList.addAll(0, topArticleList);
        articleAdapter.notifyDataSetChanged();
        rvArticle.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityCollector.getInstance().finishActivity(SplashActivity.class);
            }
        }, 500);
    }

    @Override
    public void showBanner(List<Banner> banners) {
        if (banners.isEmpty()) {
            return;
        }
        List<String> images = new ArrayList<>();
        final List<String> titles = new ArrayList<>();
        final List<String> urls = new ArrayList<>();
        final List<Integer> ids = new ArrayList<>();
        for (Banner banner : banners) {
            images.add(banner.getImagePath());
            titles.add(banner.getTitle());
            urls.add(banner.getUrl());
            ids.add(banner.getId());
        }

        bannerView.isAutoPlay(true);
        bannerView.setImages(images);
        bannerView.setBannerTitles(titles);
        bannerView.setImageLoader(new GlideImageLoader());
        bannerView.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String link = urls.get(position);
                String title = titles.get(position);
                ArticleInfo articleInfo = new ArticleInfo();
                articleInfo.setLink(link);
                articleInfo.setTitle(title);
                WebViewActivity.startAction(context, articleInfo, false, false, position);
            }
        });
        bannerView.start();

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        page = 0;
        isRefresh = true;
        isClear = true;
        articlePresenter.getArticleList(page);
        articlePresenter.getTopArticleList();
        articlePresenter.getBanner();


    }

    @Override
    public void showError(String message) {
        super.showError(message);
        refreshLayout.setRefreshing(false);
        articleAdapter.loadMoreFail();

        if (articleInfoList.isEmpty()) {
            refreshLayout.setVisibility(View.GONE);
            ll_lode_data.setVisibility(View.VISIBLE);
        }


    }
}
