package com.sanki.mywanandroid.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.adapter.ProjectCategoryAdapter;
import com.sanki.mywanandroid.bean.ProjectCategory;

import java.util.List;

public class ProjectCategoryPop extends PopupWindow {

    private RecyclerView rvCategory;
    private ProjectCategoryAdapter categoryAdapter;

    private OnCategoryClickListener listener;
    public interface OnCategoryClickListener{
        void onCategoryClick(ProjectCategory category, int position);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener){
        this.listener = listener;
    }

    public ProjectCategoryPop(Context context, final List<ProjectCategory> categoryList){
        View window = LayoutInflater.from(context).inflate(R.layout.project_category_list_layout,null);
        rvCategory = window.findViewById(R.id.rv_category);
        rvCategory.setLayoutManager(new LinearLayoutManager(context));
        categoryAdapter = new ProjectCategoryAdapter(R.layout.item_project_category,categoryList);
        rvCategory.setAdapter(categoryAdapter);
        setContentView(window);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(0x4D000000);
        setBackgroundDrawable(dw);

        categoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(listener != null){
                    ProjectCategory projectCategory = (ProjectCategory) adapter.getData().get(position);
                    listener.onCategoryClick(projectCategory,position);
                }
            }
        });

    }


}
