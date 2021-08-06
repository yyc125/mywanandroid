package com.sanki.mywanandroid.adapter;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sanki.mywanandroid.bean.ProjectCategory;
import com.sanki.mywanandroid.ui.fragment.ProjectListFragment;

import java.util.List;

public class SystemDetailFragmentAdapter extends FragmentStatePagerAdapter {

    private List<ProjectCategory> systemDetailList;

    public SystemDetailFragmentAdapter(FragmentManager fm, List<ProjectCategory> systemDetailList) {
        super(fm);
        this.systemDetailList = systemDetailList;
    }

    @Override
    public Fragment getItem(int position) {
        return ProjectListFragment.newInstance(systemDetailList.get(position).getId(),true);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return systemDetailList.get(position).getName();
    }

    @Override
    public int getCount() {
        return systemDetailList.size();
    }
}
