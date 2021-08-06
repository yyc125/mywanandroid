package com.sanki.mywanandroid.ui.activity;

import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.base.BaseActivity;
import com.sanki.mywanandroid.utils.CommonUtils;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView tvDesc;
    private TextView tvVerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void createPresenter() {

    }

    @Override
    public void init() {
        toolbar=findViewById(R.id.tool_bar);
        tvDesc=findViewById(R.id.tv_desc);
        tvVerName=findViewById(R.id.tv_ver_name);
        setSupportActionBar(toolbar);
        toolbar.setTitle("关于");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String desc = "该项目本着学习Material Design+MVP+Retrofit2+RxJava2的目的，基于鸿洋大神提供的" + "<a href=\"http://www.wanandroid.com\"><font color=\"#FF0000\">玩Android</font></a>" + "的开放API构建而成。";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            tvDesc.setText(Html.fromHtml(desc,Html.FROM_HTML_MODE_COMPACT));
        }else{
            tvDesc.setText(Html.fromHtml(desc));
        }
        tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
        tvVerName.setText("v" + CommonUtils.getVerName(this));

    }
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
}