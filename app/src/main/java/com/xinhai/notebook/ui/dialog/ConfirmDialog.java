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
import com.xinhai.notebook.databinding.DialogConfirmBinding;
import com.xinhai.notebook.utils.MyToastUtil;
import com.xinhai.notebook.utils.SpfUtil;

import java.util.regex.Pattern;

public class ConfirmDialog extends Dialog {

    //viewBinding
    private DialogConfirmBinding binding;

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogConfirmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.dialogConfirmBtnCancel.setOnClickListener(v -> cancel());

        binding.dialogConfirmBtnConfirm.setOnClickListener(v -> {

            String pass = binding.dialogConfirmEtPass.getText().toString();

            //弹出软键盘
            new Handler(Looper.myLooper()).postDelayed(() -> {
                binding.dialogConfirmEtPass.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(binding.dialogConfirmEtPass, InputMethodManager.SHOW_IMPLICIT);
            }, 380);   //0.3秒

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
            if (SpfUtil.getString(Constant.KEY_PRIVATE_PASS).equals(pass)){
                SpfUtil.saveString(Constant.KEY_BIOMETRIC_PASS,"true");
                MyToastUtil.success("设置密码成功");
                cancel();
            }else {
                MyToastUtil.error("密码错误，请重新输入");
                binding.dialogConfirmEtPass.setText("");
            }

        });

    }

}
