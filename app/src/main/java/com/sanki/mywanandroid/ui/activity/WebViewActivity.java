package com.sanki.mywanandroid.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.api.ApiService;
import com.sanki.mywanandroid.api.RetrofitUtils;
import com.sanki.mywanandroid.app.ActivityCollector;
import com.sanki.mywanandroid.base.BaseActivity;
import com.sanki.mywanandroid.base.BaseContract;
import com.sanki.mywanandroid.bean.ArticleInfo;
import com.sanki.mywanandroid.bean.MessageEvent;
import com.sanki.mywanandroid.contract.CollectContract;
import com.sanki.mywanandroid.presenter.CollectPresenter;
import com.sanki.mywanandroid.source.DataManager;
import com.sanki.mywanandroid.utils.LogUtils;
import com.sanki.mywanandroid.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class WebViewActivity extends BaseActivity implements CollectContract.View {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private WebView webView;
    private CollectPresenter collectPresenter;
    private String url;
    private String title;
    private int id;
    private int originId;
    private boolean enableCollect;
    private MenuItem menuItem;
    //是否从收藏列表跳转过来的
    private boolean isFromCollect;
    //发送取消收藏发送通知到收藏列表，但需要等到回到收藏列表后再移除，视觉效果好些
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static void startAction(Context context, ArticleInfo articleInfo) {
        startAction(context, articleInfo, true, false, 0);
    }

    public static void startAction(Context context, ArticleInfo articleInfo, boolean enableCollect, boolean isFromCollect, int position) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("ARTICLE_INFO", articleInfo);
        intent.putExtra("ENABLE_COLLECT", enableCollect);
        intent.putExtra("IS_FROM_COLLECT", isFromCollect);
        intent.putExtra("POSITION", position);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void createPresenter() {
        DataManager dataManager = new DataManager(RetrofitUtils.get().retrofit().create(ApiService.class));
        collectPresenter = new CollectPresenter(dataManager);
        collectPresenter.attachView(this);
    }

    @Override
    public void init() {
        toolbar = findViewById(R.id.tool_bar);
        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.web_view_progressbar);
        Intent intent = getIntent();
        ArticleInfo articleInfo = (ArticleInfo) intent.getSerializableExtra("ARTICLE_INFO");
        url = articleInfo.getLink();
        title = articleInfo.getTitle();
        id = articleInfo.getId();
        originId = articleInfo.getOriginId() == 0 ? -1 : articleInfo.getOriginId();
        enableCollect = intent.getBooleanExtra("ENABLE_COLLECT", true);
        isFromCollect = intent.getBooleanExtra("IS_FROM_COLLECT", false);
        position = intent.getIntExtra("POSITION", 0);
        toolbar.setTitle(title != null ? title : "");
        ActivityCollector.getInstance().addActivity(this);
        setSupportActionBar(toolbar);
        //隐藏Toolbar的返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        //允许android调用javascript
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (progressBar != null) {
                    progressBar.setProgress(newProgress);
                    if (newProgress >= 100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                 String uri = request.getUrl().toString();
                if (uri == null) return false;
                if (uri.startsWith("http:") || uri.startsWith("https:")) {
                    view.loadUrl(uri);
                    return false;
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                        //ToastUtils.showShort("暂无应用打开此链接");
                    }
                    return true;


                }
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_view_menu, menu);
        menuItem = menu.getItem(0);
        if (!enableCollect) {
            //如果是banner点进来的则不显示收藏按钮
            menuItem.setVisible(false);
        } else {
            if (isFromCollect) {
                menuItem.setTitle("取消收藏");
            } else {
                menuItem.setTitle("收藏");
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();

                }
                break;
            case R.id.collect:
                if (isFromCollect) {
                    //取消收藏
                    collectPresenter.cancelCollectArticle(id, originId);
                } else {
                    //收藏
                    collectPresenter.collectArticle(id);
                }
                break;
            case R.id.open_by_browser:
                openByBrowser();
                break;
            case R.id.refresh:
                webView.reload();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openByBrowser() {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }

    @Override
    public void showArticleList(List<ArticleInfo> articleInfoList, boolean isEnd) {

    }

    @Override
    public void showNoCollectView() {

    }

    @Override
    public void showCollectSuccess() {
        ToastUtils.showShort("收藏成功");
    }

    @Override
    public void showCancelCollectSuccess() {
        ToastUtils.showShort("取消收藏成功");
        MessageEvent event = new MessageEvent();
        event.setPosition(position);
        EventBus.getDefault().post(event);
    }

    @Override
    public void showError(String message) {
        super.showError(message);

    }
}