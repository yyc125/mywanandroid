package com.sanki.mywanandroid.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.bean.NavTitle;


import java.util.List;

public class NavAdapter extends BaseQuickAdapter<NavTitle, BaseViewHolder> {

    private int highLightItemPosition;

    public NavAdapter(int layoutResId, @Nullable List<NavTitle> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavTitle item) {
        if (helper.getLayoutPosition() == highLightItemPosition) {
            helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.grayBtn));
        } else {
            helper.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.grayBg));
        }
        helper.setText(R.id.tv_title, item.getName());
    }

    public void setHighLightItem(int position) {
        this.highLightItemPosition = position;
        notifyDataSetChanged();
    }
}
