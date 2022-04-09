package com.xinhai.notebook.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.View;
import android.view.WindowInsetsController;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.xinhai.notebook.R;
import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.data.db.DBManager;
import com.xinhai.notebook.data.db.bean.Note;
import com.xinhai.notebook.databinding.ActivityEditBinding;
import com.xinhai.notebook.utils.DateUtils;
import com.xinhai.notebook.utils.KeyboardStateObserver;
import com.xinhai.notebook.utils.MyToastUtil;
import com.xinhai.notebook.utils.SnowflakeIdWorker;

import ren.qinc.edit.PerformEdit;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;
    private static final String TAG = "EditActivity";
    private static int edit_status = -1;

    private static boolean isTitle = false;

    private static int initLength = 0;

    private static String mTitle = ""; //原来的标题
    private static String mContent = ""; //原来的内容

    // 显示隐藏键盘用的
    InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initData();
        initView();
    }

    private void initData() {
        //初始化编辑状态
        Intent it = getIntent();
        edit_status = it.getIntExtra(Constant.EDIT_STATUS, -1);
        //恢复数据
        if (edit_status == Constant.NOTE_STATUS_NORMAL) {
            Note noteInfo = (Note) it.getSerializableExtra(Constant.KEY_NOTE_INFO);
            String title = noteInfo.getTitle();
            String content = noteInfo.getContent();
            if (!title.isEmpty()) {
                binding.editEtTitle.setText(title);
                mTitle = title;
            }
            if (!content.isEmpty()) {
                binding.editEtContent.setText(content);
                mContent = content;
                initLength = content.length();
            }
        }
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

        //弹出软键盘
        if (edit_status == Constant.EDIT_STATUS_ADD) {
            new Handler(Looper.myLooper()).postDelayed(() -> {
                binding.editEtContent.requestFocus();
                mInputMethodManager = (InputMethodManager) EditActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                mInputMethodManager.showSoftInput(binding.editEtContent, InputMethodManager.SHOW_IMPLICIT);
            }, 380);   //0.1秒
        }

        //实例化软键盘
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //返回键
        binding.editIvBack.setOnClickListener(v -> EditActivity.this.finish());

        //初始化内容框
        //文本发生改变,可以是用户输入或者是EditText.setText触发.(setDefaultText的时候不会回调)
        PerformEdit performEdit = new PerformEdit(binding.editEtContent) {
            @Override
            protected void onTextChanged(Editable s) {
                //文本发生改变,可以是用户输入或者是EditText.setText触发.(setDefaultText的时候不会回调)
                super.onTextChanged(s);
                if (initLength != s.length()) {
                    binding.editIvRedo.setImageResource(R.drawable.redo);
                    binding.editIvUndo.setImageResource(R.drawable.undo);
                }
            }
        };

        performEdit.setDefaultText(binding.editEtContent.getText().toString());

        binding.editEtTitle.setOnFocusChangeListener((v, f) -> {
            isTitle = f;
            if (!f) {
                //收起软键盘
                mInputMethodManager = ((InputMethodManager) EditActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE));
                if (mInputMethodManager != null)
                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                binding.editIvSave.setVisibility(View.VISIBLE);
                binding.editIvMore.setVisibility(View.GONE);
            }
        });

        binding.editEtContent.setOnFocusChangeListener((v, f) -> {
            if (!f) {
                //收起软键盘
                mInputMethodManager = ((InputMethodManager) EditActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE));
                if (mInputMethodManager != null)
                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                binding.editIvUndo.setVisibility(View.GONE);
                binding.editIvRedo.setVisibility(View.GONE);
            } else {
                binding.editIvUndo.setVisibility(View.VISIBLE);
                binding.editIvRedo.setVisibility(View.VISIBLE);
            }
        });

        binding.editIvSave.setOnClickListener(v -> {
            //收起软键盘
            mInputMethodManager = ((InputMethodManager) EditActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE));
            if (mInputMethodManager != null)
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //控件变化
            binding.editIvMore.setVisibility(View.VISIBLE); //隐藏菜单
            //隐藏redo  undo 和 save
            binding.editIvUndo.setVisibility(View.GONE);
            binding.editIvRedo.setVisibility(View.GONE);
            binding.editIvSave.setVisibility(View.GONE);

        });

        binding.editIvUndo.setOnClickListener(v -> performEdit.undo());

        binding.editIvRedo.setOnClickListener(v -> performEdit.redo());

        KeyboardStateObserver.getKeyboardStateObserver(this).
                setKeyboardVisibilityListener(new KeyboardStateObserver.OnKeyboardVisibilityListener() {
                    @Override
                    public void onKeyboardShow() {
//                        Log.d(TAG, "onKeyboardShow: 键盘弹出");
                        if (isTitle) {
                            binding.editIvSave.setVisibility(View.VISIBLE);
                            binding.editIvMore.setVisibility(View.GONE);
                        } else {
                            //当获得焦点时
                            binding.editIvMore.setVisibility(View.GONE); //隐藏菜单
                            //显示redo  undo 和 save
                            binding.editIvUndo.setVisibility(View.VISIBLE);
                            binding.editIvRedo.setVisibility(View.VISIBLE);
                            binding.editIvSave.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onKeyboardHide() {
                        if (isTitle) {
                            binding.editIvUndo.setVisibility(View.GONE);
                            binding.editIvRedo.setVisibility(View.GONE);
                        }
                        binding.editIvSave.setVisibility(View.GONE);
                        binding.editIvMore.setVisibility(View.VISIBLE);
//                        Log.d(TAG, "onKeyboardShow: 键盘收回");
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }

    private void saveNote() {
        String title = binding.editEtTitle.getText().toString();
        String content = binding.editEtContent.getText().toString();
        if (title.isEmpty() && content.isEmpty()) {
            return;
        }
        if (title.equals(mTitle) && content.equals(mContent)) return;
        Note note = new Note();
        note.setContent(content);
        note.setTitle(title);
        note.setUid(SnowflakeIdWorker.getSnowflakeId());
        note.setTime(DateUtils.getTime().getNowTime());
        switch (edit_status) {
            case Constant.EDIT_STATUS_ADD:
                //添加item
                note.setStatus(0);
                DBManager.insertItemToNoteTab(note);
                //成功提示
                MyToastUtil.success("保存成功");
                break;
            case Constant.NOTE_STATUS_NORMAL:
                //修改item
                DBManager.updateItemByUidToNoteTab(note);
                //成功提示
                MyToastUtil.success("修改成功");
                break;
            case Constant.NOTE_STATUS_TOP:
                //置顶内容修改
                DBManager.updateItemByUidToTopTab(note);
                //成功提示
                MyToastUtil.success("修改成功");
                break;
            case Constant.NOTE_STATUS_WASTER:
                //警告提示
                MyToastUtil.warn("废纸篓不能修改内容，请先恢复内容");
                break;
            case Constant.NOTE_STATUS_PRIVATE:
                DBManager.updateItemByUidFromPrivateTab(note);
                //成功提示
                MyToastUtil.success("修改成功");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + edit_status);
        }

    }

}