package com.xinhai.notebook.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xinhai.notebook.R;
import com.xinhai.notebook.adapter.MyMainSlideAdapter;
import com.xinhai.notebook.adapter.OnStartDragListener;
import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.data.SelectEvent;
import com.xinhai.notebook.data.db.DBManager;
import com.xinhai.notebook.data.db.bean.Note;
import com.xinhai.notebook.databinding.ActivityMainBinding;
import com.xinhai.notebook.ui.dialog.MoreDialog;
import com.xinhai.notebook.ui.widget.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements OnStartDragListener {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private List<Note> mData;
    private MyMainSlideAdapter mAdapter;
    private EventBus event;
    private ItemTouchHelper mItemTouchHelper;

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
        event = EventBus.getDefault();
        event.register(this);
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    /**
     * 实现EvenBus
     * @param event event
     */
    public void onEventMainThread(SelectEvent event) {
        int size = event.getSize();
//        if (size < list.size()) {
//            isChange = true;
//            checkbox.setChecked(false);
//        } else {
//            checkbox.setChecked(true);
//            isChange = false;
//        }
//        selected.setText(String.format("已选%d项", size));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadData() {
        //刷新数据
        List<Note> topList = DBManager.getNoteListFromTab(Constant.TABLE_NAME_TOP);
        List<Note> beanList = DBManager.getNoteListFromTab(Constant.TABLE_NAME_NOTE);
        if (beanList.isEmpty() && topList.isEmpty()) return;
        for (int i = 0; i < beanList.size(); i++) {
            if (beanList.get(i).getStatus() == 1)
                beanList.remove(beanList.get(i));
        }
        mData.clear();
        mData.addAll(topList);
        mData.addAll(beanList);
        if (binding.mainRlDataTips.getVisibility() == View.VISIBLE){
            initObject();
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initObject(){
        //有数据  则显示数据
        binding.mainRvSlide.setVisibility(View.VISIBLE);
        binding.mainRlDataTips.setVisibility(View.GONE);
        //加载RecycleView
        mAdapter = new MyMainSlideAdapter(mData, this,event);
        binding.mainRvSlide.setHasFixedSize(true);
        binding.mainRvSlide.setAdapter(mAdapter);
        binding.mainRvSlide.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(recyclerView);

        binding.mainRvSlide.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 2;
                outRect.bottom = 2;
            }
        });
        mItemTouchHelper.attachToRecyclerView(binding.mainRvSlide);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

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
            initObject();
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
                    Intent intent = new Intent(this, EditActivity.class);
                    intent.putExtra(Constant.EDIT_STATUS,-1);
                    startActivity(intent);
                    break;
                default:
            }
            return true;
        });

        //toolbar title的监听点击事件
        binding.mainLlBarTitle.setOnClickListener(v -> showDialog());

        binding.mainIvBarSetting.setOnClickListener(v ->
                startActivity(new Intent(this,SettingActivity.class)));

    }

    private void showDialog(){

        MoreDialog dialog = new MoreDialog(this,R.style.MyDialog);
        /*随意定义个Dialog*/
        Window dialogWindow = dialog.getWindow();
        /*实例化Window*/
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /*实例化Window操作者*/
        lp.x = -250; // 新位置X坐标
        lp.y = -760; // 新位置Y坐标
        dialogWindow.setAttributes(lp);
        dialog.show();

    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        event.unregister(this);
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