package com.xinhai.notebook.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.databinding.DialogRecoverBinding;
import com.xinhai.notebook.utils.SpfUtil;

public class RecoverDialog extends Dialog {

    //viewBinding
    private DialogRecoverBinding binding;

    public RecoverDialog(@NonNull Context context) {
        super(context);
    }

    public RecoverDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogRecoverBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.dialogRecoverBtnCancel.setOnClickListener(v -> cancel());

        binding.dialogRecoverBtnConfirm.setOnClickListener(v -> {
            //恢复数据
            SpfUtil.saveString(Constant.FLAG_DIALOG_CALLBACK,"true");
            cancel();

        });

    }

    //设置Dialog的布局和屏幕尺寸一致
    public void setDialogSize(){
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int) d.getWidth();//对话框的宽度
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }


}
