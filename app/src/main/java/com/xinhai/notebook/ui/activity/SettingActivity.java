package com.xinhai.notebook.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;

import androidx.appcompat.app.AppCompatActivity;

import com.xinhai.notebook.NoteBookApplication;
import com.xinhai.notebook.R;
import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.databinding.ActivitySettingBinding;
import com.xinhai.notebook.ui.dialog.CancelDialog;
import com.xinhai.notebook.ui.dialog.ConfirmDialog;
import com.xinhai.notebook.ui.dialog.PrivateDialog;
import com.xinhai.notebook.utils.MyAnimationUtil;
import com.xinhai.notebook.utils.MyToastUtil;
import com.xinhai.notebook.utils.SpfUtil;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private String TAG = "SettingActivity";
    private static final Context mContext = NoteBookApplication.getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initData();
        initView();

    }

    private void initData() {

        Intent intent = getIntent();
        String help = intent.getStringExtra("help");

        if (help != null){
            if (help.equals("helpMe")) {
                //设置左右摇晃动画
                binding.settingRlPrivateSwitchManage.startAnimation(MyAnimationUtil.shakeAnimation(10));
            }
        }

    }

    private void initView() {

        //将状态栏字体设置为黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            insetsController.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// API30以下
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else this.getWindow().setStatusBarColor(Color.BLACK); //否则将状态栏设置为黑色

        //初始化开关设置
        binding.settingSwPrivateSwitch.switchWidget.setChecked(!SpfUtil.getString(Constant.KEY_PRIVATE_PASS).equals(""));
        binding.settingSwSlideSwitch.switchWidget.setChecked(!SpfUtil.getString(Constant.KEY_SETTING_SLIDE).equals(""));
        binding.settingSwBiometricSwitch.switchWidget.setChecked(!SpfUtil.getString(Constant.KEY_BIOMETRIC_PASS).equals(""));

        //初始化监听事件
        binding.settingIvBack.setOnClickListener(v -> SettingActivity.this.finish());

        //滑动开关
        binding.settingSwSlideSwitch.switchWidget.setOnClickListener(v -> {
            if (binding.settingSwSlideSwitch.switchWidget.isChecked()) {
                SpfUtil.saveString(Constant.KEY_SETTING_SLIDE, "true");
            } else SpfUtil.saveString(Constant.KEY_SETTING_SLIDE, "");

        });

        //指纹识别开关
        binding.settingSwBiometricSwitch.switchWidget.setOnClickListener(v -> {
            if (!binding.settingSwPrivateSwitch.switchWidget.isChecked()
                    && SpfUtil.getString(Constant.KEY_PRIVATE_PASS).equals("")){
                //弹出提示
                MyToastUtil.warn("清先开启私密空间！");
                //控件左右摇晃
                binding.settingRlPrivateSwitchManage.startAnimation(MyAnimationUtil.shakeAnimation(10));
                //不打开 开关
                binding.settingSwBiometricSwitch.switchWidget.setChecked(false);
                return;
            }
            if (binding.settingSwBiometricSwitch.switchWidget.isChecked()){
                ConfirmDialog dialog = new ConfirmDialog(SettingActivity.this, R.style.MyDialog);
                dialog.show();
                dialog.setOnDismissListener(d ->
                    binding.settingSwBiometricSwitch.switchWidget.setChecked(!SpfUtil.getString(Constant.KEY_BIOMETRIC_PASS).equals("")));
            }else SpfUtil.saveString(Constant.KEY_BIOMETRIC_PASS,"");


        });

        //私密空间开关
        binding.settingSwPrivateSwitch.switchWidget.setOnClickListener(v -> {
            if (binding.settingSwPrivateSwitch.switchWidget.isChecked()) {
                //开启时
                PrivateDialog dialog = new PrivateDialog(SettingActivity.this, R.style.MyDialog);
//                dialog.setCancelable(false); //空白区域无法取消dialog
                dialog.show();
                dialog.setOnDismissListener(d -> {
                    binding.settingSwPrivateSwitch.switchWidget.setChecked(!SpfUtil.getString(Constant.KEY_PRIVATE_PASS).equals(""));
                });
            } else {
                //关闭时
                CancelDialog dialog = new CancelDialog(SettingActivity.this, R.style.MyDialog);
                dialog.show();
                dialog.setOnDismissListener(d -> {
                    binding.settingSwPrivateSwitch.switchWidget.setChecked(!SpfUtil.getString(Constant.KEY_PRIVATE_PASS).equals(""));
                });
            }
        });

    }




}