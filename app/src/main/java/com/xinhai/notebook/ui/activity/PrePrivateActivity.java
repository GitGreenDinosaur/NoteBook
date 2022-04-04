package com.xinhai.notebook.ui.activity;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.xinhai.notebook.databinding.ActivityPrePrivateBinding;

public class PrePrivateActivity extends AppCompatActivity {

    private ActivityPrePrivateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrePrivateBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


    }
}