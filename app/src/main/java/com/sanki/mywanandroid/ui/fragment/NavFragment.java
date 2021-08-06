package com.sanki.mywanandroid.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.adapter.NavAdapter;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.base.BaseFragment;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.NavInfo;
import com.sanki.mywanandroid.bean.NavTitle;
import com.sanki.mywanandroid.contract.NavContract;
import com.sanki.mywanandroid.presenter.NavPresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.ui.activity.WebViewActivity;
import com.sanki.mywanandroid.ui.widget.FlowLayout;
import com.sanki.mywanandroid.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class NavFragment extends BaseFragment implements NavContract.View {
    private FlowLayout flNav;
    private RecyclerView rvNavTitle;

    private NavPresenter navPresenter;
    private DataManager dataManager;
    private NavAdapter navAdapter;
    private TextView tv_netLoad;
    private LinearLayout ll_lode_data;
    private List<NavTitle> navTitles = new ArrayList<>();

    public static NavFragment newInstance() {
        return new NavFragment();

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        navPresenter = new NavPresenter(dataManager);
        navPresenter.attachView(this);
    }

    @Override
    public void init() {
        flNav = view.findViewById(R.id.fl_nav);
        rvNavTitle = view.findViewById(R.id.rv_nav_title);
        tv_netLoad = view.findViewById(R.id.tv_netLoad);
        ll_lode_data = view.findViewById(R.id.ll_lode_data);
        rvNavTitle.setLayoutManager(new LinearLayoutManager(getActivity()));
        navAdapter = new NavAdapter(R.layout.item_nav, navTitles);
        rvNavTitle.setAdapter(navAdapter);
        navPresenter.getNavInfo();
        navAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NavTitle navTitle = navTitles.get(position);
                initTab(flNav, navTitle.getArticles());
                navAdapter.setHighLightItem(position);
            }
        });
        tv_netLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navPresenter.getNavInfo();
            }
        });
    }

    @Override
    public void showNavInfo(List<NavTitle> navTitleList) {
        rvNavTitle.setVisibility(View.VISIBLE);
        flNav.setVisibility(View.VISIBLE);
        ll_lode_data.setVisibility(View.GONE);
        navTitles.clear();
        navTitles.addAll(navTitleList);
        navAdapter.notifyDataSetChanged();
        initTab(flNav,navTitleList.get(0).getArticles());
    }

    private void initTab(FlowLayout flowLayout, final List<NavInfo> tags) {
        flowLayout.removeAllViews();
        LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置边距
        layoutParams.setMargins(30, 30, 10, 10);
        for (int i = 0; i < tags.size(); i++) {
            final NavInfo navInfo = tags.get(i);
            final TextView textView = new TextView(getActivity());
            textView.setTag(i);
            textView.setTextSize(15);
            textView.setText(tags.get(i).getTitle());
            textView.setPadding(DensityUtil.dip2px(getActivity(), 18), DensityUtil.dip2px(getActivity(), 5), DensityUtil.dip2px(getActivity(), 18), DensityUtil.dip2px(getActivity(), 5));
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
            textView.setBackgroundResource(R.drawable.flow_tab_bg);
            flowLayout.addView(textView, layoutParams);
            // 标签点击事件
            final ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setTitle(navInfo.getTitle());
            articleInfo.setLink(navInfo.getLink());
            articleInfo.setId(navInfo.getId());
            articleInfo.setCollect(navInfo.isCollect());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.startAction(getActivity(), articleInfo);
                }
            });
        }
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        if (navTitles.isEmpty()){
            rvNavTitle.setVisibility(View.GONE);
            flNav.setVisibility(View.GONE);
            ll_lode_data.setVisibility(View.VISIBLE);
        }

    }
}
