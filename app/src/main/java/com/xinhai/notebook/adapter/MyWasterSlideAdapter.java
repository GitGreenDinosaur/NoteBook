package com.xinhai.notebook.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xinhai.notebook.R;
import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.data.db.DBManager;
import com.xinhai.notebook.data.db.bean.Note;
import com.xinhai.notebook.ui.dialog.DeleteDialog;
import com.xinhai.notebook.ui.dialog.RecoverDialog;
import com.xinhai.notebook.utils.MyToastUtil;
import com.xinhai.notebook.utils.SpfUtil;

import java.util.List;

public class MyWasterSlideAdapter extends RecyclerView.Adapter<MyWasterSlideAdapter.SlideViewHolder> {

    private final List<Note> mNoteList;
    private final String TAG = "MyWasterSlideAdapter";

    public MyWasterSlideAdapter(List<Note> notes) {
        mNoteList = notes;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_layout_waster_rv, parent, false);
        return new SlideViewHolder(view);
    }


    @SuppressLint({"ClickableViewAccessibility", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        //判空
        if (mNoteList.isEmpty()) {
            return;
        }
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

                //弹出提醒弹窗
                DeleteDialog deleteDialog
                        = new DeleteDialog(v.getContext(), R.style.MyDialog);
                deleteDialog.show();
                deleteDialog.setOnDismissListener(d -> {
                    if (SpfUtil.getString(Constant.FLAG_DIALOG_CALLBACK).equals("true")) {
                        //1.移除数据库
                        DBManager.deleteItemByUidFromTab(mNoteList.get(position).getUid(),Constant.TABLE_NAME_WASTER);
                        //2.删除列表内容
                        mNoteList.remove(holder.getAdapterPosition());
                        //3.刷新页面
                        notifyItemRemoved(holder.getAdapterPosition());
                        //4.弹出提示或者弹窗
                        MyToastUtil.success("删除成功");
                        //5.清除标记
                        SpfUtil.saveString(Constant.FLAG_DIALOG_CALLBACK, "");
                    }
                });


            });
        }

        if (!holder.mRecoverTv.hasOnClickListeners()) {
            holder.mRecoverTv.setOnClickListener(v -> {
                DBManager.recoverItemToNoteTab(mNoteList.get(position));
                mNoteList.remove(holder.getAdapterPosition());
                notifyItemRemoved(position);//移除当前位置
                MyToastUtil.success("第" + (position + 1) + "条消息恢复成功");
            });
        }

        holder.mGlobalLl.setOnClickListener(v -> {
            RecoverDialog recoverDialog = new RecoverDialog(v.getContext());
            recoverDialog.show();
            recoverDialog.setDialogSize();
            recoverDialog.setOnDismissListener(d -> {
                if (SpfUtil.getString(Constant.FLAG_DIALOG_CALLBACK).equals("true")) {
                    DBManager.recoverItemToNoteTab(mNoteList.get(position));
                    mNoteList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(position);//移除当前位置
                    SpfUtil.saveString(Constant.FLAG_DIALOG_CALLBACK, "");
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return mNoteList == null ? 0 : mNoteList.size();
    }

    public static class SlideViewHolder extends RecyclerView.ViewHolder {

        private final TextView mDeleteTv;
        private final TextView mTitleTv;
        private final TextView mContentTv;
        private final TextView mTimeTv;
        private final TextView mRecoverTv;
        private final LinearLayout mGlobalLl;

        private SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.item_waster_tv_title);
            mContentTv = itemView.findViewById(R.id.item_waster_tv_content);
            mTimeTv = itemView.findViewById(R.id.item_waster_tv_time);
            mDeleteTv = itemView.findViewById(R.id.item_waster_tv_delete);
            mRecoverTv = itemView.findViewById(R.id.item_waster_tv_recover);
            mGlobalLl = itemView.findViewById(R.id.item_waster_ll_global);

        }


    }


}
