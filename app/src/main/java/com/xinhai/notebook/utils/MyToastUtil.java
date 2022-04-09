package com.xinhai.notebook.utils;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinhai.notebook.NoteBookApplication;
import com.xinhai.notebook.R;

public class MyToastUtil {

    /**
     * 自定义Toast
     *
     * @param str 提示内容
     */
    @SuppressLint("InflateParams")
    public static void msg(String str) {
        View view = LayoutInflater.from(NoteBookApplication.getContext()).inflate(R.layout.view_toast_custom, null);
        TextView tv_msg = view.findViewById(R.id.toast_tv_msg);
        tv_msg.setText(str);
        Toast toast = new Toast(NoteBookApplication.getContext());
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    @SuppressLint("InflateParams")
    public static void error(String str) {
        View view = LayoutInflater.from(NoteBookApplication.getContext()).inflate(R.layout.view_toast_custom, null);
        TextView tv_msg = view.findViewById(R.id.toast_tv_msg);
        ImageView iv_icon = view.findViewById(R.id.toast_iv_icon);
        tv_msg.setText(str);
        iv_icon.setImageResource(R.drawable.toast_icon_error);
        Toast toast = new Toast(NoteBookApplication.getContext());
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    @SuppressLint("InflateParams")
    public static void success(String str) {
        View view = LayoutInflater.from(NoteBookApplication.getContext()).inflate(R.layout.view_toast_custom, null);
        TextView tv_msg = view.findViewById(R.id.toast_tv_msg);
        ImageView iv_icon = view.findViewById(R.id.toast_iv_icon);
        tv_msg.setText(str);
        iv_icon.setImageResource(R.drawable.toast_icon_success);
        Toast toast = new Toast(NoteBookApplication.getContext());
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    @SuppressLint("InflateParams")
    public static void warn(String str) {
        View view = LayoutInflater.from(NoteBookApplication.getContext()).inflate(R.layout.view_toast_custom, null);
        TextView tv_msg = view.findViewById(R.id.toast_tv_msg);
        ImageView iv_icon = view.findViewById(R.id.toast_iv_icon);
        tv_msg.setText(str);
        iv_icon.setImageResource(R.drawable.toast_icon_warn);
        Toast toast = new Toast(NoteBookApplication.getContext());
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

}
