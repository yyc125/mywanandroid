package com.sanki.mywanandroid.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sanki.mywanandroid.bean.ProjectCategory;
import com.sanki.mywanandroid.ui.fragment.ProjectListFragment;

import java.util.List;

public class ProjectFragmentAdapter extends FragmentStatePagerAdapter {

    private List<ProjectCategory> categoryList;

    public ProjectFragmentAdapter(FragmentManager fm, List<ProjectCategory> categoryList) {
        super(fm);
        this.categoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        return ProjectListFragment.newInstance(categoryList.get(position).getId(), false);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getName();
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }
}
