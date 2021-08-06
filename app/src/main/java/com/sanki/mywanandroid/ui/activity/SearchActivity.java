package com.sanki.mywanandroid.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.adapter.ArticleAdapter;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.app.ActivityCollector;
import com.sanki.mywanandroid.base.BaseActivity;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.HotKey;
import com.sanki.mywanandroid.contract.SearchContract;
import com.sanki.mywanandroid.presenter.SearchPresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.ui.widget.FlowLayout;
import com.sanki.mywanandroid.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements SearchContract.View, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private RecyclerView rvArticle;
    private SwipeRefreshLayout refreshLayout;
    private FlowLayout flHot;
    private LinearLayout llHot;
    private EditText etKey;
    private ImageView iv_back;

    private SearchPresenter searchPresenter;
    private DataManager dataManager;
    private ArticleAdapter articleAdapter;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();
    private int page = 0;
    private boolean isRefresh;
    private String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        searchPresenter = new SearchPresenter(dataManager);
        searchPresenter.attachView(this);
    }

    @Override
    public void init() {
        ActivityCollector.getInstance().addActivity(this);
        toolbar = findViewById(R.id.tool_bar);
        rvArticle = findViewById(R.id.rv_article);
        refreshLayout = findViewById(R.id.refreshLayout);
        flHot = findViewById(R.id.fl_hot);
        llHot = findViewById(R.id.ll_hot);
        etKey = findViewById(R.id.et_key);
        iv_back = findViewById(R.id.iv_back);
        initEditText();
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        rvArticle.setLayoutManager(new LinearLayoutManager(context));
        articleAdapter = new ArticleAdapter(R.layout.item_article, articleInfoList);
        articleAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        articleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleInfo articleInfo = articleInfoList.get(position);
                WebViewActivity.startAction(context, articleInfo);
            }
        });
        articleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh = false;
                page++;
                searchPresenter.getSearchList(page, keyword);
            }
        }, rvArticle);
        rvArticle.setAdapter(articleAdapter);
        searchPresenter.getHotKey();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void initEditText() {
        etKey.requestFocus();
        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                if (TextUtils.isEmpty(newText)) {
                    llHot.setVisibility(View.VISIBLE);
                    rvArticle.setVisibility(View.GONE);
                } else {
                    llHot.setVisibility(View.GONE);
                    rvArticle.setVisibility(View.VISIBLE);
                }
                refreshLayout.setRefreshing(false);
                articleInfoList.clear();
                articleAdapter.notifyDataSetChanged();
                keyword = newText;
                if (TextUtils.isEmpty(keyword)) {
                    keyword = "";
                }
                searchPresenter.getSearchList(page, keyword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void showArticleList(List<ArticleInfo> list, boolean isEnd) {
        refreshLayout.setRefreshing(false);
        if (isRefresh) {
            articleInfoList.clear();
        } else {
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
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        page = 0;
        isRefresh = true;
        if (TextUtils.isEmpty(keyword)) {
            refreshLayout.setRefreshing(false);
            return;
        }
        searchPresenter.getSearchList(page, keyword);
    }

    @Override
    public void showNoSearchResultView() {

    }

    @Override
    public void showHotKey(List<HotKey> hotKeyList) {
        initTab(flHot, hotKeyList);

    }

    private void initTab(FlowLayout flowLayout, List<HotKey> tags) {
        if (flowLayout == null) return;
        flowLayout.removeAllViews();
        LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置边距
        layoutParams.setMargins(10, 30, 10, 8);
        for (int i = 0; i < tags.size(); i++) {
            final HotKey hotKey = tags.get(i);
            final TextView textView = new TextView(context);
            textView.setTag(i);
            textView.setTextSize(15);
            textView.setText(hotKey.getName());
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(DensityUtil.dip2px(context, 18), DensityUtil.dip2px(context, 5), DensityUtil.dip2px(context, 18), DensityUtil.dip2px(context, 5));
            textView.setTextColor(ContextCompat.getColor(context, R.color.textColor));
            textView.setBackgroundResource(R.drawable.flow_tab_bg);
            flowLayout.addView(textView, layoutParams);
            // 标签点击事件
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = hotKey.getName();
                    page = 0;
                    keyword = key;
                    etKey.setText(keyword);
                }
            });
        }
    }


    @Override
    public void showError(String message) {
        super.showError(message);
        refreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchPresenter != null) {
            searchPresenter.detachView();
        }


    }
}