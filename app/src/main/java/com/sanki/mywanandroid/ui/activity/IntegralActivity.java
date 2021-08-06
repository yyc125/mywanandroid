package com.sanki.mywanandroid.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.base.BaseActivity;
import com.sanki.mywanandroid.bean.MyIntegral;
import com.sanki.mywanandroid.contract.IntegralContract;
import com.sanki.mywanandroid.presenter.IntegralPresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.ui.widget.DynamicNumberTextView;
import com.sanki.mywanandroid.utils.DensityUtil;
import com.sanki.mywanandroid.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class IntegralActivity extends BaseActivity implements IntegralContract.View, View.OnClickListener {

    private Toolbar toolbar;
    private DynamicNumberTextView tvIntegral;
    private DynamicNumberTextView tvRank;
    private ImageView ivIntegral;
    private ImageView ivRank;
    private LinearLayout ll_integral, ll_rank;

    private IntegralPresenter integralPresenter;
    private DataManager dataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    public void createPresenter() {
        dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        integralPresenter = new IntegralPresenter(dataManager);
        integralPresenter.attachView(this);

    }

    @Override
    public void init() {
        toolbar = findViewById(R.id.tool_bar);
        tvIntegral = findViewById(R.id.tv_integral);
        tvRank = findViewById(R.id.tv_rank);
        ivIntegral = findViewById(R.id.iv_integral);
        ivRank = findViewById(R.id.iv_rank);
        ll_integral = findViewById(R.id.ll_integral);
        ll_rank = findViewById(R.id.ll_rank);
        ll_integral.setOnClickListener(this);
        ll_rank.setOnClickListener(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("我的积分");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        integralPresenter.getMyIntegral();
    }

    @Override
    public void showMyIntegral(MyIntegral myIntegral) {

        tvIntegral.setText(String.valueOf(myIntegral.getCoinCount()));
        tvRank.setText(String.valueOf(myIntegral.getRank()));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        execAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        integralPresenter.detachView();
    }

    private void execAnim() {
        List<View> views = animViewList();

        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            view.setTranslationY(DensityUtil.dip2px(IntegralActivity.this, 20));
            view.setAlpha(0);
        }

        for (int i = 0; i < views.size(); i++) {
            final View view = views.get(i);
            view.animate().translationY(0).alpha(1).setStartDelay(50 + (i * 100)).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        }
    }

    private List<View> animViewList() {
        List<View> views = new ArrayList<>();
        views.add(ivIntegral);
        views.add(tvIntegral);
        views.add(ivRank);
        views.add(tvRank);
        return views;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_integral:
                ToastUtils.showShort("我的积分");
                break;
            case R.id.ll_rank:
                ToastUtils.showShort("积分排名");
                break;
            default:
                break;
        }
    }
}