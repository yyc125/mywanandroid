package com.sanki.mywanandroid.ui.activity;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.app.ActivityCollector;
import com.sanki.mywanandroid.base.BaseActivity;
import com.sanki.mywanandroid.bean.Login;
import com.sanki.mywanandroid.constants.Constants;
import com.sanki.mywanandroid.contract.LoginContract;
import com.sanki.mywanandroid.presenter.LoginPresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.ui.fragment.ArticleFragment;
import com.sanki.mywanandroid.ui.fragment.MineFragment;
import com.sanki.mywanandroid.ui.fragment.NavFragment;
import com.sanki.mywanandroid.ui.fragment.ProjectFragment;
import com.sanki.mywanandroid.ui.fragment.SystemFragment;
import com.sanki.mywanandroid.utils.NetworkUtils;
import com.sanki.mywanandroid.utils.SPUtils;
import com.sanki.mywanandroid.utils.ToastUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity  {

    private Toolbar toolbar;
    private FragmentManager fm;
    private ActionMenuView menuView;
    private List<Fragment> fragmentList = new ArrayList<>();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("悟Android");
                    addFragment(fragmentList.get(0));
                    showFragment(0);
                    return true;
                case R.id.navigation_project:
                    toolbar.setTitle("项目");
                    addFragment(fragmentList.get(1));
                    showFragment(1);
                    return true;
                case R.id.navigation_system:
                    toolbar.setTitle("体系");
                    addFragment(fragmentList.get(2));
                    showFragment(2);
                    return true;
                case R.id.navigation_nav:
                    toolbar.setTitle("导航");
                    addFragment(fragmentList.get(3));
                    showFragment(3);
                    return true;
                case R.id.navigation_mine:
                    toolbar.setTitle("我的");
                    addFragment(fragmentList.get(4));
                    showFragment(4);
                    return true;
                default:
                    return false;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void createPresenter() {
        if (!NetworkUtils.isNetwork(context)){
            ToastUtils.showShort("网络异常请检查网络");
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void init() {
        toolbar = findViewById(R.id.tool_bar);
        ActivityCollector.getInstance().addActivity(this);
        setSupportActionBar(toolbar);
        //隐藏Toolbar的返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        ColorStateList colorStateList = getResources().getColorStateList(R.color.navigation_menu_item_color);
        navigation.setLabelVisibilityMode(1);
        navigation.setItemTextColor(colorStateList);
        navigation.setItemIconTintList(colorStateList);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm = getSupportFragmentManager();
        initFragments();
        addFragment(fragmentList.get(0));
        showFragment(0);

    }

    private void showFragment(int index) {
        for (int i = 0; i < fragmentList.size(); i++) {
            if (index == i) {
                fm.beginTransaction().show(fragmentList.get(index)).commit();
            } else {
                fm.beginTransaction().hide(fragmentList.get(i)).commit();
            }
        }

    }

    private void addFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    private void initFragments() {
        fragmentList.add(ArticleFragment.newInstance());
        fragmentList.add(ProjectFragment.newInstance());
        fragmentList.add(SystemFragment.newInstance());
        fragmentList.add(NavFragment.newInstance());
        fragmentList.add(MineFragment.newInstance());

    }

    private ActionMenuView reflectToolbarMenuView() throws NoSuchFieldException, IllegalAccessException {
        if(toolbar != null){
            Class<? extends Toolbar> aClass = toolbar.getClass();
            Field mTitleTextView = aClass.getDeclaredField("mMenuView");
            mTitleTextView.setAccessible(true);
            return (ActionMenuView) mTitleTextView.get(toolbar);
        }
        return null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                try {
                    menuView = reflectToolbarMenuView();
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                if(menuView != null){
                    Intent intent = new Intent(this,SearchActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,menuView,"search").toBundle());
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCollector.getInstance().exitApp();
    }


}