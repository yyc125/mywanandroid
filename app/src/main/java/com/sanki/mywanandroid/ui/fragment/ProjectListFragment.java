package com.sanki.mywanandroid.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.adapter.ArticleAdapter;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.base.BaseFragment;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.contract.ProjectListContract;
import com.sanki.mywanandroid.presenter.ProjectListPresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.ui.activity.WebViewActivity;
import com.sanki.mywanandroid.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ProjectListFragment extends BaseFragment implements ProjectListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String CID = "cid";
    private static final String IS_SYSTEM = "isSystem";

    private int cid;
    private int page = 0;
    private boolean isRefresh;
    private boolean isSystem;

    private ProjectListPresenter projectListPresenter;
    private DataManager dataManager;
    private ArticleAdapter articleAdapter;
    private List<ArticleInfo> articleInfoList = new ArrayList<>();
    private RecyclerView rvArticle;
    private SwipeRefreshLayout refreshLayout;
    private TextView tv_netLoad;
    private LinearLayout ll_lode_data;
    //private FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cid = getArguments().getInt(CID);
            isSystem = getArguments().getBoolean(IS_SYSTEM);
        }
    }


    /**
     * @param cid
     * @param isSystem 是否是体系下文章
     * @return
     */
    public static ProjectListFragment newInstance(int cid, boolean isSystem) {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();
        args.putInt(CID, cid);
        args.putBoolean(IS_SYSTEM, isSystem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_projectlist;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        projectListPresenter = new ProjectListPresenter(dataManager);
        projectListPresenter.attachView(this);
    }

    @Override
    public void init() {
        rvArticle = view.findViewById(R.id.rv_article);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        //floatingActionButton = view.findViewById(R.id.fab_top);
        tv_netLoad = view.findViewById(R.id.tv_netLoad);
        ll_lode_data = view.findViewById(R.id.ll_lode_data);
        rvArticle.setLayoutManager(new LinearLayoutManager(getContext()));
        articleAdapter = new ArticleAdapter(R.layout.item_article, articleInfoList);
        articleAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
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
                projectListPresenter.getProjectList(page, cid, isSystem);
            }
        }, rvArticle);
        rvArticle.setAdapter(articleAdapter);
        refreshLayout.setRefreshing(true);
        projectListPresenter.getProjectList(page, cid, isSystem);
        tv_netLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectListPresenter.getProjectList(page, cid, isSystem);
            }
        });
    }

    @Override
    public void showProjectList(List<ArticleInfo> projectList, boolean isEnd) {

        if (refreshLayout == null) return;
        refreshLayout.setRefreshing(false);
        refreshLayout.setVisibility(View.VISIBLE);
        ll_lode_data.setVisibility(View.GONE);
        if (isRefresh) {
            articleInfoList.clear();
        } else {
            if (isEnd) {
                articleAdapter.loadMoreEnd();
            } else {
                articleAdapter.loadMoreComplete();
            }
        }
        articleInfoList.addAll(projectList);
        articleAdapter.notifyDataSetChanged();

    }

    @Override
    public void showError(String message) {
        super.showError(message);
        refreshLayout.setRefreshing(false);
        articleAdapter.loadMoreFail();
        if (articleInfoList.isEmpty()){
            refreshLayout.setVisibility(View.GONE);
            ll_lode_data.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRefresh() {

        refreshLayout.setRefreshing(true);
        page = 0;
        isRefresh = true;
        projectListPresenter.getProjectList(page, cid, isSystem);
    }
}
