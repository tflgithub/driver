package com.library.treerecyclerview.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.library.treerecyclerview.manager.ItemManageImpl;
import com.library.treerecyclerview.manager.ItemManager;

import java.util.ArrayList;

/**
 * Created by fula on 2018/7/20.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected ItemManager<T> mItemManager;
    private ArrayList<T> mDatas;
    protected OnItemClickListener mOnItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.createViewHolder(parent, viewType);
        onBindViewHolderClick(holder, holder.itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBindViewHolder(holder, getDatas().get(position), position);
    }


    public interface OnItemClickListener {
        void onItemClick(ViewHolder viewHolder, int position);
    }

    /**
     * 操作adapter
     *
     * @return
     */
    public ItemManager<T> getItemManager() {
        if (mItemManager == null) {
            mItemManager = new ItemManageImpl<>(this);
        }
        return mItemManager;
    }

    /**
     * 实现item的点击事件
     */
    public void onBindViewHolderClick(final ViewHolder viewHolder, View view) {
        //判断当前holder是否已经设置了点击事件
        if (!view.hasOnClickListeners()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = viewHolder.getLayoutPosition();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(viewHolder, layoutPosition);
                    }
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return getLayoutId(position);
    }

    @Override
    public int getItemCount() {
        return getDatas().size();
    }


    public ArrayList<T> getDatas() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        return mDatas;
    }

    public void setDatas(ArrayList<T> datas) {
        if (datas != null && !datas.isEmpty()) {
            getDatas().clear();
            getDatas().addAll(datas);
        }
    }

    public T getData(int position) {
        if (position >= 0) {
            return getDatas().get(position);
        }
        return null;
    }


    /**
     * 获取该position的item的layout
     *
     * @param position 角标
     * @return item的layout id
     */
    public abstract int getLayoutId(int position);

    /**
     * view与数据绑定
     *
     * @param holder
     * @param t
     * @param position
     */
    public abstract void onBindViewHolder(ViewHolder holder, T t, int position);
}
