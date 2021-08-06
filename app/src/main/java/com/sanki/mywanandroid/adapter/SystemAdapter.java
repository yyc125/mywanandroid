package com.sanki.mywanandroid.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.bean.ProjectCategory;
import com.sanki.mywanandroid.bean.System;
import com.sanki.mywanandroid.ui.widget.FlowLayout;
import com.sanki.mywanandroid.utils.DensityUtil;

import java.util.List;

public class SystemAdapter extends BaseQuickAdapter<System, BaseViewHolder> {

    public SystemAdapter(int layoutResId, @Nullable List<System> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, System item) {
        helper.setText(R.id.tv_title,item.getName());
        FlowLayout flowLayout = helper.getView(R.id.fl_system_content);
        initTab(flowLayout,item.getChildren());
    }

    private void initTab(FlowLayout flowLayout, final List<ProjectCategory> tags) {
        flowLayout.removeAllViews();
        LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置边距
        layoutParams.setMargins(5, 3, 5, 3);
        for (int i = 0; i < tags.size(); i++) {
            final TextView textView = new TextView(mContext);
            textView.setTag(i);
            textView.setTextSize(14);
            textView.setText(tags.get(i).getName());
            textView.setPadding(DensityUtil.dip2px(mContext,8), DensityUtil.dip2px(mContext,3), DensityUtil.dip2px(mContext,8), DensityUtil.dip2px(mContext,3));
            textView.setTextColor(ContextCompat.getColor(mContext,R.color.textColorTint));
            flowLayout.addView(textView, layoutParams);
        }
    }
}
