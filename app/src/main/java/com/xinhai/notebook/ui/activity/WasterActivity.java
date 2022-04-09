package com.xinhai.notebook.ui.activity;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xinhai.notebook.adapter.MyWasterSlideAdapter;
import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.data.db.DBManager;
import com.xinhai.notebook.data.db.bean.Note;
import com.xinhai.notebook.databinding.ActivityWasterBinding;

import java.util.List;

public class WasterActivity extends AppCompatActivity {

    private ActivityWasterBinding binding;
    private List<Note> mWasterNoteList;
    private MyWasterSlideAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWasterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initData();
        initView();
    }

    private void initData() {
        //获取Waster表的数据
        mWasterNoteList = DBManager.getNoteListFromTab(Constant.TABLE_NAME_WASTER);
        if (mWasterNoteList.size() > 0){
            binding.wasterRlDataTips.setVisibility(View.GONE);
            binding.wasterRvSlide.setVisibility(View.VISIBLE);
//            waster_rl_data_tips waster_rv_slide
        }else{
            binding.wasterRlDataTips.setVisibility(View.VISIBLE);
            binding.wasterRvSlide.setVisibility(View.GONE);
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

        //返回按钮
        binding.wasterIvBack.setOnClickListener(v -> WasterActivity.this.finish());

        //加载RecycleView
        mAdapter = new MyWasterSlideAdapter(mWasterNoteList);
        binding.wasterRvSlide.setHasFixedSize(true);
        binding.wasterRvSlide.setAdapter(mAdapter);
        binding.wasterRvSlide.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //适配recycleView
        binding.wasterRvSlide.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 2;
                outRect.bottom = 2;
            }
        });

    }
}