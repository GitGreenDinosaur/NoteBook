package com.xinhai.notebook.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinhai.notebook.R;

public class MyToastUtil {

    private Context mContext;
    private int mTime; //显示时长

    public MyToastUtil(Context context, int showTime) {
        mContext = context;
        mTime = showTime;
    }

    public MyToastUtil(Context context) {
        mContext = context;
        mTime = Toast.LENGTH_SHORT;
    }

    /**
     * 自定义Toast
     *
     * @param str 提示内容
     */
    @SuppressLint("InflateParams")
    public void msg(String str) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_toast_custom, null);
        TextView tv_msg = view.findViewById(R.id.toast_tv_msg);
        tv_msg.setText(str);
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 50);
        toast.setDuration(mTime);
        toast.setView(view);
        toast.show();
    }

    @SuppressLint("InflateParams")
    public void error(String str) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_toast_custom, null);
        TextView tv_msg = view.findViewById(R.id.toast_tv_msg);
        ImageView iv_icon = view.findViewById(R.id.toast_iv_icon);
        tv_msg.setText(str);
        iv_icon.setImageResource(R.drawable.toast_icon_error);
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 50);
        toast.setDuration(mTime);
        toast.setView(view);
        toast.show();
    }

    @SuppressLint("InflateParams")
    public void success(String str) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_toast_custom, null);
        TextView tv_msg = view.findViewById(R.id.toast_tv_msg);
        ImageView iv_icon = view.findViewById(R.id.toast_iv_icon);
        tv_msg.setText(str);
        iv_icon.setImageResource(R.drawable.toast_icon_success);
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 50);
        toast.setDuration(mTime);
        toast.setView(view);
        toast.show();
    }

    @SuppressLint("InflateParams")
    public void warn(String str) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_toast_custom, null);
        TextView tv_msg = view.findViewById(R.id.toast_tv_msg);
        ImageView iv_icon = view.findViewById(R.id.toast_iv_icon);
        tv_msg.setText(str);
        iv_icon.setImageResource(R.drawable.toast_icon_warn);
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 50);
        toast.setDuration(mTime);
        toast.setView(view);
        toast.show();
    }

}
