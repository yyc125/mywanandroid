package com.sanki.mywanandroid.base;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.app.ActivityCollector;
import com.sanki.mywanandroid.utils.NetworkUtils;
import com.sanki.mywanandroid.utils.ToastUtils;

import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.View {

    public BaseActivity context;
    private Unbinder unbinder;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",


    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //7.0只用NoTitle无法去掉标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        context = this;
        ActivityCollector.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            initPermission();
        }else {
            createPresenter();
            init();

        }



    }
    //Android 6.0以上的权限申请
    private void initPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BaseActivity.this, PERMISSIONS_STORAGE, 0);
        }
        else{
            createPresenter();
            init();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
            createPresenter();
            init();

        }
        else{
            finish();
        }
    }

    @Override
    public void showError(String message) {
        if (!NetworkUtils.isNetwork(context)){
            ToastUtils.showShort("网络异常请检查网络");
        }else {
            ToastUtils.showShort(message);
        }

    }

    public abstract int getLayoutId();
    public abstract void createPresenter();
    public abstract void init();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.getInstance().removeActivity(this);
        if(unbinder != null && unbinder != Unbinder.EMPTY){
            unbinder.unbind();
            unbinder = null;
        }
    }
}
