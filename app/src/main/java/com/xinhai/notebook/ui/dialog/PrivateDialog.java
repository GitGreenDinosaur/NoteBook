package com.xinhai.notebook.ui.dialog;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.databinding.DialogPrivateBinding;
import com.xinhai.notebook.utils.MyToastUtil;
import com.xinhai.notebook.utils.SpfUtil;

import java.util.regex.Pattern;

public class PrivateDialog extends Dialog {

    //viewBinding
    private DialogPrivateBinding binding;

    public PrivateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogPrivateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //弹出键盘
        new Handler(Looper.myLooper()).postDelayed(() -> {
            binding.dialogPrivateEtPass.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.dialogPrivateEtPass, InputMethodManager.SHOW_IMPLICIT);
        }, 380);   //0.3秒

        binding.dialogPrivateBtnCancel.setOnClickListener(v -> cancel());

        binding.dialogPrivateBtnConfirm.setOnClickListener(v -> {

            String pass = binding.dialogPrivateEtPass.getText().toString();

            if (pass.isEmpty()) {
                MyToastUtil.error("密码不能为空！");
                return;
            }
            if (pass.length() != 6) {
                MyToastUtil.error("密码只能6位数！");
                return;
            }
            if (!Pattern.compile("^\\d{6}$").matcher(pass).matches()){
                MyToastUtil.error("密码格式不规范！");
                return;
            }
            SpfUtil.saveString(Constant.KEY_PRIVATE_PASS,pass);
            MyToastUtil.success("设置密码成功");
            cancel();
        });

    }
}
