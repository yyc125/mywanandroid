package com.sanki.mywanandroid.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sanki.mywanandroid.utils.NetworkUtils;
import com.sanki.mywanandroid.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements BaseContract.View {

    public Context context;
    public View view;
    private Unbinder unbinder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(getLayoutId(),container,false);
        unbinder = ButterKnife.bind(this, view);
        createPresenter();
        init();
        return view;

    }

    public abstract int getLayoutId();
    public abstract void createPresenter();
    public abstract void init();
    @Override
    public void showError(String message) {
        if (!NetworkUtils.isNetwork(context)){
            ToastUtils.showShort("网络异常请检查网络");
        }else {
            ToastUtils.showShort(message);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
