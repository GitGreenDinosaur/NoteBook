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
     * ??????EvenBus
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
//        selected.setText(String.format("??????%d???", size));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadData() {
        //????????????
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
        //?????????  ???????????????
        binding.mainRvSlide.setVisibility(View.VISIBLE);
        binding.mainRlDataTips.setVisibility(View.GONE);
        //??????RecycleView
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

        //?????????????????????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            insetsController.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// API30??????
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else this.getWindow().setStatusBarColor(Color.BLACK); //?????????????????????????????????

        if (mData.size() <= 0) {
            //????????????
            binding.mainRvSlide.setVisibility(View.GONE);
            binding.mainRlDataTips.setVisibility(View.VISIBLE);
        } else {
            initObject();
        }

        //?????????floatingButton
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
                    //??????????????????????????????????????????
                    Intent intent = new Intent(this, EditActivity.class);
                    intent.putExtra(Constant.EDIT_STATUS,-1);
                    startActivity(intent);
                    break;
                default:
            }
            return true;
        });

        //toolbar title?????????????????????
        binding.mainLlBarTitle.setOnClickListener(v -> showDialog());

        binding.mainIvBarSetting.setOnClickListener(v ->
                startActivity(new Intent(this,SettingActivity.class)));

    }

    private void showDialog(){

        MoreDialog dialog = new MoreDialog(this,R.style.MyDialog);
        /*???????????????Dialog*/
        Window dialogWindow = dialog.getWindow();
        /*?????????Window*/
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /*?????????Window?????????*/
        lp.x = -250; // ?????????X??????
        lp.y = -760; // ?????????Y??????
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
        //??????????????????????????????????????????1.4???
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //????????????
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
        scaleAnimation.setDuration(800);
//        scaleAnimation.setRepeatCount(0); //0???1???????????????
//        alphaAnimation.setRepeatCount(0); //Animation.INFINITE ???????????????
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        binding.mainFabAdd.startAnimation(as);
    }

    private void setAnim2() {
        AnimationSet as = new AnimationSet(true);
        //???????????????????????????1.4????????????1.8???
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1.8f, 1.4f, 1.8f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //????????????
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