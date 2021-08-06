package com.sanki.mywanandroid.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.bean.ProjectCategory;

import java.util.List;

public class ProjectCategoryAdapter extends BaseQuickAdapter<ProjectCategory, BaseViewHolder> {

    public ProjectCategoryAdapter(int layoutResId, @Nullable List<ProjectCategory> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectCategory item) {
        helper.setText(R.id.tv_category,item.getName());
    }
}
