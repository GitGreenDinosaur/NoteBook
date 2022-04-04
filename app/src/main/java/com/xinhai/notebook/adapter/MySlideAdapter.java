package com.xinhai.notebook.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xinhai.notebook.R;
import com.xinhai.notebook.data.db.bean.Note;
import com.xinhai.notebook.utils.MyToastUtil;

import java.util.List;

public class MySlideAdapter extends RecyclerView.Adapter<MySlideAdapter.SlideViewHolder> {

    private final List<Note> mNoteList;
    private final Context mContext;
    private final String TAG = "MySlideAdapter";

    public MySlideAdapter(Context context, List<Note> notes) {
        mNoteList = notes;
        mContext = context;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_layout_main_rv, parent, false);
        return new SlideViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        MyToastUtil toastUtil = new MyToastUtil(mContext, 1500);
        holder.mTitleTv.setText(mNoteList.get(position).getTitle());
        holder.mTimeTv.setText(mNoteList.get(position).getTime());
        holder.mContentTv.setText(mNoteList.get(position).getContent());
        if (!holder.mDeleteTv.hasOnClickListeners()) {
            holder.mDeleteTv.setOnClickListener(v -> {
                //1.删除列表内容
                mNoteList.remove(holder.getAdapterPosition());
                //2.刷新页面
                notifyItemRemoved(holder.getAdapterPosition());
                //3.弹出提示或者弹窗
                toastUtil.success("删除成功");
            });
        }


//        holder.mDetailLl.setOnClickListener(v -> toastUtil.msg("第"+(position+1)+"条消息"));

        if (!holder.mTopTv.hasOnClickListeners()) {
            holder.mTopTv.setOnClickListener(v -> toastUtil.success("第" + (position + 1) + "条消息置顶成功"));
        }



        holder.mGlobalLl.setOnTouchListener((view, motionEvent) -> {
            float x = 0, y = 0;
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = motionEvent.getX();
                    y = motionEvent.getY();
                    Log.d(TAG, "onBindViewHolder: x:y " + x + " : " + y);
                    holder.mGlobalLl.setScaleX((float) 0.85);
                    holder.mGlobalLl.setScaleY((float) 0.85);
                    Log.d(TAG, "onBindViewHolder: ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_UP:
                    holder.mGlobalLl.setScaleX(1);
                    holder.mGlobalLl.setScaleY(1);
                    toastUtil.msg("第" + (position + 1) + "条消息");
                    Log.d(TAG, "onBindViewHolder: ACTION_UP");
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (((motionEvent.getX() > x) && ((motionEvent.getX() - 100) > x)) || ((motionEvent.getX() < x) && ((x - 100) > motionEvent.getX()))) { //x - 100:表示下移    x + 100:表示上移
                        Log.d(TAG, "onBindViewHolder: x = y " + motionEvent.getX() + " :  " + motionEvent.getY());
                        holder.mGlobalLl.setScaleX(1);
                        holder.mGlobalLl.setScaleY(1);
                    }
                    break;

                default:
            }
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    public static class SlideViewHolder extends RecyclerView.ViewHolder {

        private final TextView mDeleteTv;
        private final TextView mTitleTv;
        private final TextView mContentTv;
        private final TextView mTimeTv;
        private final TextView mTopTv;
        private final LinearLayout mGlobalLl;

        private SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.item_main_tv_title);
            mContentTv = itemView.findViewById(R.id.item_main_tv_content);
            mTimeTv = itemView.findViewById(R.id.item_main_tv_time);
            mDeleteTv = itemView.findViewById(R.id.item_main_tv_delete);
            mTopTv = itemView.findViewById(R.id.item_main_tv_top);
            mGlobalLl = itemView.findViewById(R.id.item_main_ll_global);
        }

    }


}
