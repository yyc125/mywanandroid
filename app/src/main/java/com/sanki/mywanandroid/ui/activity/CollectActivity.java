package com.sanki.mywanandroid.ui.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.adapter.ArticleAdapter;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.base.BaseActivity;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.MessageEvent;
import com.sanki.mywanandroid.contract.CollectContract;
import com.sanki.mywanandroid.presenter.CollectPresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.utils.NetworkUtils;
import com.sanki.mywanandroid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends BaseActivity implements CollectContract.View, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private RecyclerView rvArticle;
    private SwipeRefreshLayout refreshLayout;

    private CollectPresenter collectPresenter;
    private DataManager dataManager;
    private ArticleAdapter articleAdapter;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();
    private int page = 0;
    private boolean isRefresh;
    private int needRemoveItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        collectPresenter = new CollectPresenter(dataManager);
        collectPresenter.attachView(this);

    }

    @Override
    public void init() {
        toolbar = findViewById(R.id.tool_bar);
        rvArticle = findViewById(R.id.rv_article);
        refreshLayout=findViewById(R.id.refreshLayout);
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("收藏");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        rvArticle.setLayoutManager(new LinearLayoutManager(context));
        articleAdapter = new ArticleAdapter(R.layout.item_article,articleInfoList);
        articleAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        articleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleInfo articleInfo = articleInfoList.get(position);
                WebViewActivity.startAction(context,articleInfo,true,true,position);
            }
        });
        articleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isRefresh = false;
                page++;
                collectPresenter.getCollectList(page);
            }
        },rvArticle);
        rvArticle.setAdapter(articleAdapter);
        collectPresenter.getCollectList(page);

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        page = 0;
        isRefresh = true;
        collectPresenter.getCollectList(page);

    }

    @Override
    public void showArticleList(List<ArticleInfo> list, boolean isEnd) {
        refreshLayout.setRefreshing(false);
        if(isRefresh){
            articleInfoList.clear();
        }else{
            if(isEnd){
                articleAdapter.loadMoreEnd();
            }else{
                articleAdapter.loadMoreComplete();
            }
        }
        articleInfoList.addAll(list);
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoCollectView() {
        refreshLayout.setRefreshing(false);
        View emptyView = View.inflate(context,R.layout.empty_view,null);
        TextView tvContent = emptyView.findViewById(R.id.tv_content);
        tvContent.setText("暂无收藏文章");
        articleAdapter.setEmptyView(emptyView);
    }

    @Override
    public void showCollectSuccess() {

    }

    @Override
    public void showCancelCollectSuccess() {

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isNetwork(context)){
            ToastUtils.showShort("网络异常请检查网络");
        }
        if(needRemoveItemPosition != -1){
            articleInfoList.remove(needRemoveItemPosition);
            if(!articleInfoList.isEmpty()){
                articleAdapter.notifyItemRemoved(needRemoveItemPosition);
            }else{
                articleAdapter.notifyDataSetChanged();
            }
            needRemoveItemPosition = -1;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        collectPresenter.detachView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        needRemoveItemPosition = event.getPosition();
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        refreshLayout.setRefreshing(false);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}