package com.xinhai.notebook.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.databinding.DialogRemoveBinding;
import com.xinhai.notebook.utils.MyToastUtil;
import com.xinhai.notebook.utils.SpfUtil;

public class RemoveDialog extends Dialog {

    //viewBinding
    private DialogRemoveBinding binding;

    public RemoveDialog(@NonNull Context context) {
        super(context);
    }

    public RemoveDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogRemoveBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.dialogCancelBtnCancel.setOnClickListener(v -> cancel());
        binding.dialogCancelBtnConfirm.setOnClickListener(v -> {
            SpfUtil.saveString(Constant.FLAG_DIALOG_CALLBACK,"true");
            MyToastUtil.success("删除成功");
            cancel();
        });

    }
}
