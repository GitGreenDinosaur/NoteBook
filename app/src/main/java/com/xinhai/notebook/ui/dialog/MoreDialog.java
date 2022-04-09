package com.xinhai.notebook.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.databinding.DialogMoreBinding;
import com.xinhai.notebook.ui.activity.PrePrivateActivity;
import com.xinhai.notebook.ui.activity.SettingActivity;
import com.xinhai.notebook.ui.activity.WasterActivity;
import com.xinhai.notebook.utils.MyToastUtil;
import com.xinhai.notebook.utils.SpfUtil;

public class MoreDialog extends Dialog {

    //viewBinding
    private DialogMoreBinding binding;

    public MoreDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogMoreBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.dialogMoreBtnWaster.setOnClickListener(v ->
                getContext().startActivity(new Intent(getContext(), WasterActivity.class)));

        binding.dialogMoreBtnPrivate.setOnClickListener(v -> {
            if (SpfUtil.getString(Constant.KEY_PRIVATE_PASS).equals("")) {
                MyToastUtil.warn("请开启该功能");
                Intent intent = new Intent(getContext(), SettingActivity.class);
                intent.putExtra("help","helpMe");
                getContext().startActivity(intent);
                return;
            }
            getContext().startActivity(new Intent(getContext(), PrePrivateActivity.class));
        });

    }
}
