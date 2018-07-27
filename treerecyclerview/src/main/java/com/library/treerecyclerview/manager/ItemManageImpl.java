package com.library.treerecyclerview.manager;

import com.library.treerecyclerview.base.BaseRecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by fula on 2018/7/20.
 */

public class ItemManageImpl<T> extends ItemManager<T> {

    public ItemManageImpl(BaseRecyclerAdapter<T> adapter) {
        super(adapter);
    }

    @Override
    public void addItem(T item) {
        if (item == null) return;
        getDatas().add(item);
        notifyDataChanged();
    }

    @Override
    public void addItem(int position, T item) {
        getDatas().add(position, item);
        notifyDataChanged();
    }

    @Override
    public void addItems(ArrayList<T> items) {
        getDatas().addAll(items);
        notifyDataChanged();
    }

    @Override
    public void addItems(int position, ArrayList<T> items) {
        getDatas().addAll(position, items);
        notifyDataChanged();
    }

    @Override
    public void removeItem(T item) {
        getDatas().remove(item);
        notifyDataChanged();
    }

    @Override
    public void removeItem(int position) {
        getDatas().remove(position);
        notifyDataChanged();
    }

    @Override
    public void removeItems(ArrayList<T> items) {
        getDatas().removeAll(items);
        notifyDataChanged();
    }

    @Override
    public void replaceItem(int position, T item) {
        getDatas().set(position, item);
        notifyDataChanged();
    }

    @Override
    public void replaceAllItem(ArrayList<T> items) {
        if (items != null) {
            setDatas(items);
            notifyDataChanged();
        }
    }

    @Override
    public T getItem(int position) {
        return getDatas().get(position);
    }

    @Override
    public int getItemPosition(T item) {
        return getDatas().indexOf(item);
    }

    @Override
    public void clean() {
        getDatas().clear();
        notifyDataChanged();
    }
}
