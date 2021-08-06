package com.sanki.mywanandroid.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.adapter.ProjectFragmentAdapter;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.base.BaseFragment;
import com.sanki.mywanandroid.bean.ProjectCategory;
import com.sanki.mywanandroid.contract.ProjectContract;
import com.sanki.mywanandroid.presenter.ProjectPresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.ui.widget.ProjectCategoryPop;

import java.util.ArrayList;
import java.util.List;

public class ProjectFragment extends BaseFragment implements ProjectContract.View {
    private TabLayout tabLayout;
    private ViewPager vpProject;
    private ImageView ivExpand;
    private DataManager dataManager;
    private ProjectPresenter projectPresenter;
    private List<ProjectCategory> categoryList=new ArrayList<>();
    private ProjectCategoryPop categoryPop;
    private TextView tv_netLoad;
    private LinearLayout ll_lode_data;
    private RelativeLayout rel_tab;

    public static ProjectFragment newInstance() {
        return new ProjectFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_project;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        projectPresenter = new ProjectPresenter(dataManager);
        projectPresenter.attachView(this);

    }

    @Override
    public void init() {
        tabLayout = view.findViewById(R.id.tab_layout);
        vpProject = view.findViewById(R.id.vp_project);
        ivExpand = view.findViewById(R.id.iv_expand);
        tv_netLoad=view.findViewById(R.id.tv_netLoad);
        ll_lode_data=view.findViewById(R.id.ll_lode_data);
        rel_tab=view.findViewById(R.id.rel_tab);
        projectPresenter.getProjectCategory();
        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initProjectCategoryPop();
            }
        });
        tv_netLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectPresenter.getProjectCategory();
            }
        });

    }

    private void initProjectCategoryPop() {
        if (categoryList != null && !categoryList.isEmpty()) {
            if (categoryPop == null) {
                categoryPop = new ProjectCategoryPop(getActivity(), categoryList);
                categoryPop.setOnCategoryClickListener(new ProjectCategoryPop.OnCategoryClickListener() {
                    @Override
                    public void onCategoryClick(ProjectCategory category, int position) {
                        categoryPop.dismiss();
                        vpProject.setCurrentItem(position, true);
                    }
                });
            }
            ivExpand.animate().rotation(180).setDuration(200).start();
            categoryPop.showAsDropDown(tabLayout);
            categoryPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ivExpand.animate().rotation(0).setDuration(200).start();
                }
            });
        }
    }

    @Override
    public void showProjectCategory(List<ProjectCategory>list) {
        categoryList.clear();
        categoryList.addAll(list);
        vpProject.setVisibility(View.VISIBLE);
        rel_tab.setVisibility(View.VISIBLE);
        ll_lode_data.setVisibility(View.GONE);
        initTab(categoryList);
        initViewPager(categoryList);
        tabLayout.setupWithViewPager(vpProject);

    }

    private void initViewPager(List<ProjectCategory> categoryList) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            vpProject.setAdapter(new ProjectFragmentAdapter(activity.getSupportFragmentManager(), categoryList));
        }
    }

    private TabLayout.OnTabSelectedListener tabListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            vpProject.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void initTab(List<ProjectCategory> categoryList) {
        for (ProjectCategory category : categoryList) {
            tabLayout.addTab(tabLayout.newTab().setText(category.getName()));
        }
        tabLayout.addOnTabSelectedListener(tabListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tabLayout != null) {
            tabLayout.removeOnTabSelectedListener(tabListener);
        }
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        if (categoryList.isEmpty()){
            vpProject.setVisibility(View.GONE);
            rel_tab.setVisibility(View.GONE);
            ll_lode_data.setVisibility(View.VISIBLE);
        }


    }
}
