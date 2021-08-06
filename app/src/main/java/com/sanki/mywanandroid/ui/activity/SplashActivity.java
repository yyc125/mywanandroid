package com.sanki.mywanandroid.ui.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.widget.TextView;

import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.app.ActivityCollector;
import com.sanki.mywanandroid.base.BaseActivity;
import com.sanki.mywanandroid.utils.DensityUtil;

public class SplashActivity extends BaseActivity {

   private TextView tvLogo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void createPresenter() {

    }

    @Override
    public void init() {
        tvLogo=findViewById(R.id.tv_logo);
        ActivityCollector.getInstance().addActivity(this);
        tvLogo.animate().scaleX(0.5f).scaleY(0.5f).translationY(-DensityUtil.dip2px(this,150)).setStartDelay(1000)
                .setDuration(600)
                .setListener(new AnimatorListenerAdapter() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,tvLogo,"logo").toBundle());
//                        finish();
                    }
                }).start();
    }
}