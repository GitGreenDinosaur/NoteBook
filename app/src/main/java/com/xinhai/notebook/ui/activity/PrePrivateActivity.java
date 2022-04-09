package com.xinhai.notebook.ui.activity;

import static androidx.biometric.BiometricPrompt.ERROR_LOCKOUT;
import static androidx.biometric.BiometricPrompt.ERROR_LOCKOUT_PERMANENT;
import static androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON;
import static androidx.biometric.BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL;
import static androidx.biometric.BiometricPrompt.ERROR_NO_SPACE;
import static androidx.biometric.BiometricPrompt.ERROR_TIMEOUT;
import static androidx.biometric.BiometricPrompt.ERROR_USER_CANCELED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowInsetsController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import com.xinhai.notebook.R;
import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.databinding.ActivityPrePrivateBinding;
import com.xinhai.notebook.utils.BiometricUtil;
import com.xinhai.notebook.utils.MyAnimationUtil;
import com.xinhai.notebook.utils.MyToastUtil;
import com.xinhai.notebook.utils.SpfUtil;

import java.util.HashMap;

public class PrePrivateActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityPrePrivateBinding binding;
    private static String mPass;
    private HashMap<Object, Integer> mMap;
    private StringBuilder input, showStr;
//    private final String TAG = "PrePrivateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrePrivateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initData();
        initView();
    }

    private void initData() {
        //先获取密码
        mPass = SpfUtil.getString(Constant.KEY_PRIVATE_PASS);
        //初始化字符串
        input = new StringBuilder();
        showStr = new StringBuilder();

        mMap = new HashMap<>();
        mMap.put(R.id.pre_private_btn_1, 1);
        mMap.put(R.id.pre_private_btn_2, 2);
        mMap.put(R.id.pre_private_btn_3, 3);
        mMap.put(R.id.pre_private_btn_4, 4);
        mMap.put(R.id.pre_private_btn_5, 5);
        mMap.put(R.id.pre_private_btn_6, 6);
        mMap.put(R.id.pre_private_btn_7, 7);
        mMap.put(R.id.pre_private_btn_8, 8);
        mMap.put(R.id.pre_private_btn_9, 9);
        mMap.put(R.id.pre_private_btn_0, 0);

    }

    private void initView() {

        //将状态栏字体设置为黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            insetsController.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// API30以下
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else this.getWindow().setStatusBarColor(Color.BLACK); //否则将状态栏设置为黑色

        binding.prePrivateBtn1.setOnClickListener(this);
        binding.prePrivateBtn2.setOnClickListener(this);
        binding.prePrivateBtn3.setOnClickListener(this);
        binding.prePrivateBtn4.setOnClickListener(this);
        binding.prePrivateBtn5.setOnClickListener(this);
        binding.prePrivateBtn6.setOnClickListener(this);
        binding.prePrivateBtn7.setOnClickListener(this);
        binding.prePrivateBtn8.setOnClickListener(this);
        binding.prePrivateBtn9.setOnClickListener(this);
        binding.prePrivateBtn0.setOnClickListener(this);

        //初始化控件状态
        if (SpfUtil.getString(Constant.KEY_BIOMETRIC_PASS).equals("true")) {
            binding.prePrivateIvBiometricUnlock.setVisibility(View.VISIBLE);
        } else {
            binding.prePrivateIvBiometricUnlock.setVisibility(View.GONE);
        }

        //返回键
        binding.prePrivateIvBack.setOnClickListener(v -> PrePrivateActivity.this.finish());
        //删除键
        binding.prePrivateBtnDelete.setOnClickListener(v -> {
            if (input.length() - 1 < 0) {
                MyToastUtil.warn("暂时不支持删除空气");
                return;
            }
            if (input.length() - 1 == 0) {
                binding.prePrivateTvTips.setTextColor(Color.parseColor("#95969A"));
                binding.prePrivateTvTips.setText("请输入隐私密码");
                input = new StringBuilder();
                showStr = new StringBuilder();
                return;
            }
            input = input.deleteCharAt(input.length() - 1);
            showStr = showStr.deleteCharAt(showStr.length() - 1);
            binding.prePrivateTvTips.setText(showStr);
        });
        //确认键
        binding.prePrivateBtnConfirm.setOnClickListener(v -> {

            if (input.toString().equals(mPass)) {
                binding.prePrivateTvTips.setText("密码成功，即将进入");
                MyToastUtil.success("即将进入...");
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    startActivity(new Intent(PrePrivateActivity.this, PrivateActivity.class));
                }, 1000);
            } else {
                binding.prePrivateTvTips.setTextColor(Color.parseColor("#95969A"));
                binding.prePrivateTvTips.setText("请再次输入隐私密码");
                input = new StringBuilder();
                showStr = new StringBuilder();
                MyToastUtil.warn("密码错误！");
            }
        });

        //指纹键
        binding.prePrivateIvBiometricUnlock.setOnClickListener(v -> biometricUnlock());

    }


    /**
     * 实现各种按钮的点击事件
     *
     * @param v view
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pre_private_btn_1:
            case R.id.pre_private_btn_2:
            case R.id.pre_private_btn_3:
            case R.id.pre_private_btn_4:
            case R.id.pre_private_btn_5:
            case R.id.pre_private_btn_6:
            case R.id.pre_private_btn_7:
            case R.id.pre_private_btn_8:
            case R.id.pre_private_btn_9:
                checkPass(v.getId());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

    }

    private void checkPass(Object o) {

        if (input.length() + 1 > 6) {
            MyToastUtil.warn("密码仅6位");
            return;
        }

        input = input.append(mMap.get(o));
        showStr = showStr.append("*");
        binding.prePrivateTvTips.setText(showStr);
        binding.prePrivateTvTips.setTextColor(Color.BLACK);

        //获得震动服务
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //震动5毫秒
        vibrator.vibrate(30);

        if (input.length() == 6) {
            if (input.toString().equals(mPass)) {
                MyToastUtil.success("即将进入...");
                binding.prePrivateTvTips.setText("密码成功，即将进入");
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    startActivity(new Intent(PrePrivateActivity.this, PrivateActivity.class));
                }, 1000);
            } else binding.prePrivateTvTips.startAnimation(MyAnimationUtil.shakeAnimation(8));
        }

    }

    private void biometricUnlock() {

        BiometricUtil.authenticate(this, new BiometricPrompt.AuthenticationCallback() {
            /**
             * 验证过程中发生了错误
             * @param errorCode 错误码
             * @param errString 错误信息
             */
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                switch (errorCode) {
                    case ERROR_USER_CANCELED:
                        MyToastUtil.error("取消了指纹识别");
                        break;
                    case ERROR_LOCKOUT:
                        MyToastUtil.error("失败5次，已锁定，请30秒后在试");
                        break;
                    case ERROR_LOCKOUT_PERMANENT:
                        MyToastUtil.error("失败次数太多，指纹验证已锁定，请改用密码，图案等方式解锁");
                    case ERROR_NEGATIVE_BUTTON:
                        MyToastUtil.error("点击了negative button");
                        break;
                    case ERROR_NO_DEVICE_CREDENTIAL:
                        MyToastUtil.error("尚未设置密码，图案等解锁方式");
                        break;
                    case ERROR_NO_SPACE:
                        MyToastUtil.error("可用空间不足");
                        break;
                    case ERROR_TIMEOUT:
                        MyToastUtil.error("验证超时");
                        break;
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                binding.prePrivateTvTips.setText("密码成功，即将进入");
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    startActivity(new Intent(PrePrivateActivity.this, PrivateActivity.class));
                }, 1000);
                MyToastUtil.success("验证成功");
            }

            /**
             * 验证失败
             */
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                MyToastUtil.error("验证失败，请重试");
            }
        });

    }

}