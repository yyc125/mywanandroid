package com.sanki.mywanandroid.ui.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.google.android.material.tabs.TabLayout;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.adapter.SystemDetailFragmentAdapter;
import com.sanki.mywanandroid.base.BaseActivity;
import com.sanki.mywanandroid.bean.ProjectCategory;
import com.sanki.mywanandroid.bean.System;
import com.sanki.mywanandroid.ui.widget.ProjectCategoryPop;

import java.util.List;

public class SystemDetailActivity extends BaseActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager vpProject;
    private ImageView ivExpand;

    public static final String SYSTEM = "system";
    private System system;

    private ProjectCategoryPop categoryPop;

    private List<ProjectCategory> systemDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_system_detail;
    }

    @Override
    public void createPresenter() {

    }

    @Override
    public void init() {
        toolbar = findViewById(R.id.tool_bar);
        tabLayout = findViewById(R.id.tab_layout);
        vpProject = findViewById(R.id.vp_project);
        ivExpand = findViewById(R.id.iv_expand);
        system = (System) getIntent().getSerializableExtra(SYSTEM);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(system.getName());
        this.systemDetailList = system.getChildren();
        initTab(system.getChildren());
        initViewPager(system.getChildren());
        tabLayout.setupWithViewPager(vpProject);
        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initProjectCategoryPop();
            }
        });
    }

    private void initProjectCategoryPop() {
        if (systemDetailList != null && !systemDetailList.isEmpty()) {
            if (categoryPop == null) {
                categoryPop = new ProjectCategoryPop(this, systemDetailList);
                categoryPop.setOnCategoryClickListener(new ProjectCategoryPop.OnCategoryClickListener() {
                    @Override
                    public void onCategoryClick(ProjectCategory category, int position) {
                        categoryPop.dismiss();
                        vpProject.setCurrentItem(position, true);
                    }
                });
            }
            categoryPop.showAsDropDown(tabLayout);
            ivExpand.animate().rotation(180).setDuration(200).start();
            categoryPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ivExpand.animate().rotation(0).setDuration(200).start();
                }
            });
        }

    }

    private void initTab(List<ProjectCategory> systemDetailList) {
        for (ProjectCategory systemDetail : systemDetailList) {
            tabLayout.addTab(tabLayout.newTab().setText(systemDetail.getName()));
        }
        tabLayout.addOnTabSelectedListener(tabListener);
    }

    private void initViewPager(List<ProjectCategory> systemDetailList) {
        vpProject.setAdapter(new SystemDetailFragmentAdapter(getSupportFragmentManager(), systemDetailList));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tabLayout != null) {
            tabLayout.removeOnTabSelectedListener(tabListener);
        }
    }
}