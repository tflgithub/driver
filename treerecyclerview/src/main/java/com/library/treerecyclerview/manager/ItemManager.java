package com.library.treerecyclerview.manager;

import android.text.style.ForegroundColorSpan;

import com.library.treerecyclerview.base.BaseRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by fula on 2018/7/20.
 */

public abstract class ItemManager<T> {


    protected BaseRecyclerAdapter<T> mAdapter;

    protected void setDatas(ArrayList<T> items) {
        getAdapter().setDatas(items);
    }

    protected ArrayList<T> getDatas() {
        return getAdapter().getDatas();
    }

    public ItemManager(BaseRecyclerAdapter<T> adapter) {
        mAdapter = adapter;
    }

    public BaseRecyclerAdapter<T> getAdapter() {
        return mAdapter;
    }

    public void setAdapter(BaseRecyclerAdapter<T> adapter) {
        mAdapter = adapter;
    }

    /**
     * 需要改变颜色的text
     */
    protected String text;
    /**
     * text改变的颜色
     */
    protected ForegroundColorSpan span;

    public void setText(String text, ForegroundColorSpan span) {
        this.text = text;
        this.span = span;
    }

    public String getText() {
        return text;
    }

    public ForegroundColorSpan getSpan() {
        return span;
    }

    //增
    public abstract void addItem(T item);

    public abstract void addItem(int position, T item);

    public abstract void addItems(ArrayList<T> items);

    public abstract void addItems(int position, ArrayList<T> items);

    //删
    public abstract void removeItem(T item);

    public abstract void removeItem(int position);

    public abstract void removeItems(ArrayList<T> items);


    //改
    public abstract void replaceItem(int position, T item);

    public abstract void replaceAllItem(ArrayList<T> items);

    //查
    public abstract T getItem(int position);

    public abstract int getItemPosition(T item);

    public abstract void clean();

    //刷新
    public void notifyDataChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
