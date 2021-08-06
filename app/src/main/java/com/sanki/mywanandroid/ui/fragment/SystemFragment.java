package com.sanki.mywanandroid.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.adapter.SystemAdapter;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.base.BaseFragment;
import com.sanki.mywanandroid.bean.System;
import com.sanki.mywanandroid.contract.SystemContract;
import com.sanki.mywanandroid.presenter.SystemPresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.ui.activity.SystemDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class SystemFragment extends BaseFragment implements SystemContract.View, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView rvSystem;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout ll_lode_data;
    private Button tv_netLoad;
    private boolean isRefresh;

    private SystemPresenter systemPresenter;
    private DataManager dataManager;
    private SystemAdapter systemAdapter;
    private List<System> systemList = new ArrayList<>();

    public static SystemFragment newInstance() {
        return new SystemFragment();

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_system;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        systemPresenter = new SystemPresenter(dataManager);
        systemPresenter.attachView(this);
    }

    @Override
    public void init() {
        rvSystem = view.findViewById(R.id.rv_system);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        ll_lode_data = view.findViewById(R.id.ll_lode_data);
        tv_netLoad = view.findViewById(R.id.tv_netLoad);

        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);
        rvSystem.setLayoutManager(new LinearLayoutManager(getContext()));
        systemAdapter = new SystemAdapter(R.layout.item_system, systemList);
        systemAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        systemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), SystemDetailActivity.class);
                intent.putExtra(SystemDetailActivity.SYSTEM, systemList.get(position));
                startActivity(intent);
            }
        });
        tv_netLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                systemPresenter.getSystemList();
            }
        });
        rvSystem.setAdapter(systemAdapter);
        refreshLayout.setRefreshing(true);
        systemPresenter.getSystemList();
    }

    @Override
    public void showSystemList(List<System> systemList) {
        refreshLayout.setRefreshing(false);
        refreshLayout.setVisibility(View.VISIBLE);
        ll_lode_data.setVisibility(View.GONE);
        if (isRefresh) {
            this.systemList.clear();
        } else {
            systemAdapter.loadMoreComplete();
        }
        this.systemList.addAll(systemList);
        systemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        isRefresh = true;
        systemPresenter.getSystemList();
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        refreshLayout.setRefreshing(false);
        if (systemList.isEmpty()){
            refreshLayout.setVisibility(View.GONE);
            ll_lode_data.setVisibility(View.VISIBLE);
        }


    }
}
