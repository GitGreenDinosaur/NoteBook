package com.xinhai.notebook.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xinhai.notebook.R;
import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.data.db.DBManager;
import com.xinhai.notebook.data.db.bean.Note;
import com.xinhai.notebook.ui.activity.EditActivity;
import com.xinhai.notebook.utils.MyToastUtil;
import com.xinhai.notebook.utils.SpfUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MyMainSlideAdapter extends RecyclerView.Adapter<MyMainSlideAdapter.SlideViewHolder> implements ItemTouchHelperAdapter {

    private final List<Note> mNoteList;
    private final List<Note> selected;
    public HashMap<Integer, Boolean> map;
    private final EventBus eventBus;
    private final OnStartDragListener mDragStartListener;
    private final String TAG = "MySlideAdapter";

    public MyMainSlideAdapter(List<Note> notes, OnStartDragListener dragStartListener, EventBus eventBus) {
        mNoteList = notes;
        mDragStartListener = dragStartListener;
        this.eventBus = eventBus;
        map = new HashMap<>();
        selected = new ArrayList<>();
        init();
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_layout_main_rv, parent, false);
        return new SlideViewHolder(view);
    }

    private void init() {
        if (null == mNoteList || mNoteList.size() <= 0) {
            return;
        }
        for (int i = 0, p = mNoteList.size(); i < p; i++) {
            map.put(i, false);
        }
    }

    @SuppressLint({"ClickableViewAccessibility", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        //判空
        if (mNoteList.isEmpty()) {
            return;
        }

        //判断是否开启了私密空间
        if (SpfUtil.getString(Constant.KEY_PRIVATE_PASS).equals(""))
            holder.mPrivateTv.setVisibility(View.GONE);

        //当只有标题时
        if (mNoteList.get(position).getContent().isEmpty()) {
            holder.mTitleTv.setText(mNoteList.get(position).getTitle());
            holder.mContentTv.setVisibility(View.GONE);
        }
        //只有内容时
        if (mNoteList.get(position).getTitle().isEmpty()) {
            String[] splits = mNoteList.get(position).getContent().split("\n\t");
            if (splits.length <= 1) {
                holder.mTitleTv.setText(splits[0]);
                holder.mContentTv.setVisibility(View.GONE);
            } else {
                holder.mTitleTv.setText(splits[0]);
                StringBuilder content = new StringBuilder();
                for (int i = 1; i < splits.length; i++) {
                    content.append(splits[i]);
                }
                if (holder.mContentTv.getVisibility() == View.GONE)
                    holder.mContentTv.setVisibility(View.VISIBLE);
                holder.mContentTv.setText(content.toString());
            }
        }
        //当两者不为空时
        if (!(mNoteList.get(position).getTitle().isEmpty() || mNoteList.get(position).getContent().isEmpty())) {
            holder.mTitleTv.setText(mNoteList.get(position).getTitle());
            holder.mContentTv.setText(mNoteList.get(position).getContent());
        }
        holder.mTimeTv.setText(mNoteList.get(position).getTime());
        if (!holder.mDeleteTv.hasOnClickListeners()) {
            holder.mDeleteTv.setOnClickListener(v -> {
                //1.移除数据库
                DBManager.removeItemToWasterTab(mNoteList.get(position), Constant.TABLE_NAME_NOTE);
                //2.删除列表内容
                mNoteList.remove(holder.getAdapterPosition());
                //3.刷新页面
                notifyItemRemoved(holder.getAdapterPosition());
                //4.弹出提示或者弹窗
                MyToastUtil.success("删除成功");
            });
        }

        if (!holder.mPrivateTv.hasOnClickListeners()) {
            holder.mPrivateTv.setOnClickListener(v -> {
                if (!SpfUtil.getString(Constant.KEY_PRIVATE_PASS).equals("")) {
                    DBManager.removeItemToPrivateTab(mNoteList.get(position),Constant.TABLE_NAME_NOTE);
                    notifyItemRemoved(position);//移除当前位置
                    holder.mIv_top.setVisibility(View.VISIBLE);
                    MyToastUtil.success("设置成功");
                }
            });
        }

        holder.mGlobalLl.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), EditActivity.class);
            intent.putExtra(Constant.EDIT_STATUS,Constant.NOTE_STATUS_NORMAL); //给编辑界面，传一个编辑的状态
            intent.putExtra(Constant.KEY_NOTE_INFO,mNoteList.get(position));
            v.getContext().startActivity(intent);

        });




    }

    private int selected(HashMap<Integer, Boolean> map) {
        int size = 0;
        for (Integer key : map.keySet()) {
            if(map.get(key)){
                size++;
            }
        }
        return size;
    }

    public HashMap<Integer, Boolean> getMap() {
        return map;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMap(HashMap<Integer, Boolean> map) {
        this.map = map;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mNoteList == null ? 0 : mNoteList.size();
    }

    /**
     * 点击事件和长按事件
     */
    public interface ItemClickListener{
        void onItemClick(RecyclerView.ViewHolder holder , int position);
        void onItemLongClick(RecyclerView.ViewHolder holder , int position);
    }

    private ItemClickListener mItemClickListener;

    public void setOnItemClickListener(ItemClickListener listener){
        this.mItemClickListener=listener;
    }

    @Override
    public void onItemDismiss(int position) {
        mNoteList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mNoteList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
//        init();
//        notifyDataSetChanged();
        return true;
    }


    public static class SlideViewHolder extends RecyclerView.ViewHolder {

        private final TextView mDeleteTv;
        private final TextView mTitleTv;
        private final TextView mContentTv;
        private final TextView mTimeTv;
        private final TextView mPrivateTv;
        private final LinearLayout mGlobalLl;
        private final ImageView mIv_top;

        private SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.item_main_tv_title);
            mContentTv = itemView.findViewById(R.id.item_main_tv_content);
            mTimeTv = itemView.findViewById(R.id.item_main_tv_time);
            mDeleteTv = itemView.findViewById(R.id.item_main_tv_delete);
            mPrivateTv = itemView.findViewById(R.id.item_main_tv_private);
            mGlobalLl = itemView.findViewById(R.id.item_main_ll_global);
            mIv_top = itemView.findViewById(R.id.item_main_iv_top);

        }
    }

}

