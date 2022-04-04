package com.xinhai.notebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xinhai.notebook.R;
import com.xinhai.notebook.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    private List<String> mList = new ArrayList<>();
    private Context mContext;

    public SlideAdapter(Context context) {
        for (int i = 1; i <= 20; i++) {
            mList.add("消息" + i);
        }
        mContext = context;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycle_item_slide_menu, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.mMsgTv.setText(mList.get(position));
        if (!holder.mDeleteTv.hasOnClickListeners()) {
            holder.mDeleteTv.setOnClickListener(v -> {
                mList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            });
        }
        holder.mLl_content.setOnClickListener(view -> holder.setAnim1());

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SlideViewHolder extends RecyclerView.ViewHolder {

        private TextView mDeleteTv;
        private TextView mMsgTv;
        private TextView mCollectTv;
        private LinearLayout mLl_content;

        private SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            mDeleteTv = itemView.findViewById(R.id.tv_delete);
            mMsgTv = itemView.findViewById(R.id.tv_msg);
            mCollectTv = itemView.findViewById(R.id.tv_collect);
            mLl_content = itemView.findViewById(R.id.ll_content);

//            mMsgTv.setOnClickListener(v -> Toast.makeText(mContext, "消息", Toast.LENGTH_SHORT).show());
            mMsgTv.setOnClickListener(v -> ToastUtil.show("消息", mContext));
            mCollectTv.setOnClickListener(v -> ToastUtil.show("收藏成功", mContext));

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
            mLl_content.startAnimation(as);
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
            mLl_content.startAnimation(as);
        }

    }


}
