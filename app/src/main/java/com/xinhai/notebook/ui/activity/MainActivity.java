package com.xinhai.notebook.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsetsController;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xinhai.notebook.R;
import com.xinhai.notebook.adapter.MySlideAdapter;
import com.xinhai.notebook.data.db.bean.Note;
import com.xinhai.notebook.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private List<Note> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
//        setContentView(R.layout.activity_main);
        setContentView(view);
        initData();
        initView();

    }

    private void initData() {
        mData = new ArrayList<>();


    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        //加载Toolbar
        setSupportActionBar(binding.mainToolbar);
        //将状态栏字体设置为黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            insetsController.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// API30以下
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else this.getWindow().setStatusBarColor(Color.BLACK); //否则将状态栏设置为黑色

        if (mData.size() <= 0) {
            //数据为空
            binding.mainRvSlide.setVisibility(View.GONE);
            binding.mainRlDataTips.setVisibility(View.VISIBLE);
        } else {
            //加载RecycleView
            MySlideAdapter adapter = new MySlideAdapter(this, mData);
            binding.mainRvSlide.setAdapter(adapter);
            binding.mainRvSlide.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.mainRvSlide.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.top = 2;
                    outRect.bottom = 2;
                }
            });
        }


        //初始化floatingButton
        binding.mainFabAdd.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (view.getId() == R.id.main_fab_add) {
                        binding.mainFabAdd.setScaleX((float) 0.85);
                        binding.mainFabAdd.setScaleY((float) 0.85);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (view.getId() == R.id.main_fab_add) {
                        binding.mainFabAdd.setScaleX(1);
                        binding.mainFabAdd.setScaleY(1);
                    }
                    //跳转界面，此处可添加跳转动画
                    startActivity(new Intent(this, EditActivity.class));
                    break;
                default:
            }
            return true;
        });

    }


    private void setAnim1() {
        AnimationSet as = new AnimationSet(true);
        //缩放动画，以中心从原始放大到1.4倍
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
        scaleAnimation.setDuration(800);
//        scaleAnimation.setRepeatCount(0); //0为1次以此类推
//        alphaAnimation.setRepeatCount(0); //Animation.INFINITE 无限次重复
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        binding.mainFabAdd.startAnimation(as);
    }

    private void setAnim2() {
        AnimationSet as = new AnimationSet(true);
        //缩放动画，以中心从1.4倍放大到1.8倍
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1.8f, 1.4f, 1.8f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0.1f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        binding.mainRvSlide.startAnimation(as);
    }

}