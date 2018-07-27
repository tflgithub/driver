package com.library.treerecyclerview.item;

import android.support.annotation.Nullable;

import com.library.treerecyclerview.adapter.TreeRecyclerType;
import com.library.treerecyclerview.factory.ItemHelperFactory;
import com.library.treerecyclerview.manager.ItemManager;

import java.util.ArrayList;

/**
 * Created by fula on 2018/7/20.
 */

public abstract class TreeItemGroup<D> extends TreeItem<D> {

    /**
     * 持有的子item
     */
    private ArrayList<TreeItem> child;

    /**
     * 是否展开
     */
    private boolean isExpand = false;


    public boolean isExpand() {
        return isExpand;
    }


    /**
     * 设置为传入
     *
     * @param expand 传入true则展开,传入false则折叠
     */
    public final void setExpand(boolean expand) {
        if (!isCanExpand()) {
            return;
        }
        if (expand) {
            onExpand();
        } else {
            onCollapse();
        }
    }

    /**
     * 展开
     */
    protected void onExpand() {
        isExpand = true;
        ItemManager itemManager = getItemManager();
        if (itemManager != null) {
            int itemPosition = itemManager.getItemPosition(this);
            ArrayList datas = itemManager.getAdapter().getDatas();
            datas.addAll(itemPosition + 1, getExpandChild());
            itemManager.notifyDataChanged();
        }
    }

    /**
     * 折叠
     */
    protected void onCollapse() {
        isExpand = false;
        ItemManager itemManager = getItemManager();
        if (itemManager != null) {
            ArrayList datas = itemManager.getAdapter().getDatas();
            datas.removeAll(getExpandChild());
            itemManager.notifyDataChanged();
        }
    }

    /**
     * 能否展开折叠
     *
     * @return
     */
    public boolean isCanExpand() {
        return true;
    }


    /**
     * 获得所有展开的childs,包括子item的childs
     *
     * @return
     */
    @Nullable
    private ArrayList<TreeItem> getExpandChild() {
        if (getChild() == null) {
            return null;
        }
        return ItemHelperFactory.getChildItemsWithType(this, TreeRecyclerType.SHOW_DEFAULT);
    }


    public void setData(D data) {
        super.setData(data);
        child = initChildList(data);
    }

    public void setChild(ArrayList<TreeItem> child) {
        this.child = child;
    }

    /**
     * 获得所有childs,包括下下....级item的childs
     *
     * @return
     */
    @Nullable
    public ArrayList<TreeItem> getAllChilds() {
        if (getChild() == null) {
            return null;
        }
        return ItemHelperFactory.getChildItemsWithType(this, TreeRecyclerType.SHOW_ALL);
    }

    /**
     * 获得自己的childs.
     *
     * @return
     */
    @Nullable
    public ArrayList<TreeItem> getChild() {
        return child;
    }


    public int getChildCount() {
        return child == null ? 0 : child.size();
    }

    /**
     * 初始化子集
     *
     * @param data
     * @return
     */
    @Nullable
    protected abstract ArrayList<TreeItem> initChildList(D data);

    /**
     * 是否消费child的click事件
     *
     * @param child 具体click的item
     * @return 返回true代表消费此次事件，child不会走onclick()，返回false说明不消费此次事件，child依然会走onclick()
     */
    public boolean onInterceptClick(TreeItem child) {
        return false;
    }


}
