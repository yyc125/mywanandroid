package com.sanki.mywanandroid.utils;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sanki.mywanandroid.R;
import com.sanki.mywanandroid.app.MyApp;


/**
 * Created by drakeet on 9/27/14.
 */
public class ToastUtils {

    private ToastUtils() {
    }


    public static void showShort(int resId) {
        Toast.makeText(MyApp.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast toast = Toast.makeText(MyApp.getContext(), message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            View view = toast.getView();
            view.setBackgroundResource(R.drawable.shape_round_corner);
            TextView text = view.findViewById(android.R.id.message);
            Resources resource = MyApp.getContext().getResources();
            ColorStateList csl = resource.getColorStateList(R.color.white);
            if (csl != null) {
                text.setTextColor(csl);
            }
            toast.show();
        }

    }

    public static void showLong(int resId) {
        Toast.makeText(MyApp.getContext(), resId, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(MyApp.getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
