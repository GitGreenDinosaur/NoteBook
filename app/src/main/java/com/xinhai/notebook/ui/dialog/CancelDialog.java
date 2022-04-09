package com.xinhai.notebook.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.databinding.DialogCancelBinding;
import com.xinhai.notebook.utils.MyToastUtil;
import com.xinhai.notebook.utils.SpfUtil;

public class CancelDialog extends Dialog {

    //viewBinding
    private DialogCancelBinding binding;

    public CancelDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogCancelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.dialogCancelBtnCancel.setOnClickListener(v -> cancel());

        binding.dialogCancelBtnConfirm.setOnClickListener(v -> {

            SpfUtil.saveString(Constant.KEY_PRIVATE_PASS,"");
            //同时关闭指纹识别
            SpfUtil.saveString(Constant.KEY_BIOMETRIC_PASS,"");
            MyToastUtil.success("关闭成功");
            cancel();

        });

    }
}
