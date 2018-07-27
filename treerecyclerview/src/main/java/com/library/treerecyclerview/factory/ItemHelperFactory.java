package com.library.treerecyclerview.factory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.library.treerecyclerview.adapter.TreeRecyclerType;
import com.library.treerecyclerview.annotation.TreeItemClass;
import com.library.treerecyclerview.item.TreeItem;
import com.library.treerecyclerview.item.TreeItemGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fula on 2018/7/20.
 */

public class ItemHelperFactory {

    public static ArrayList<TreeItem> createItems(@Nullable ArrayList list, @Nullable TreeItemGroup treeParentItem) {
        if (null == list) {
            return null;
        }
        ArrayList<TreeItem> treeItemList = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            try {
                Object itemData = list.get(i);
                Class<? extends TreeItem> treeItemClass = getTypeClass(itemData);
                TreeItem treeItem;
                //判断是否是TreeItem的子类
                if (treeItemClass != null) {
                    treeItem = treeItemClass.newInstance();
                    treeItem.setData(itemData);
                    treeItem.setParentItem(treeParentItem);
                    treeItemList.add(treeItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return treeItemList;
    }

    /**
     * 获取ItemClass
     *
     * @param itemData
     * @return
     */
    private static Class<? extends TreeItem> getTypeClass(Object itemData) {
        Class<? extends TreeItem> treeItemClass = null;
        //判断是否使用注解绑定了ItemClass,适用当前模块
        TreeItemClass annotation = itemData.getClass().getAnnotation(TreeItemClass.class);
        if (annotation != null) {
            treeItemClass = annotation.iClass();
        }
        return treeItemClass;
    }

    /**
     * 确定item的class类型,并且添加到了itemConfig,用该方法创建TreeItem
     *
     * @return
     */
    public static <D> TreeItem createTreeItem(D d) {
        TreeItem treeItem = null;
        try {
            Class<? extends TreeItem> itemClass;
            itemClass = getTypeClass(d);
            if (itemClass != null) {
                treeItem = itemClass.newInstance();
                treeItem.setData(d);
            }
        } catch (Exception e) {
        }
        return treeItem;
    }

    /**
     * 根据TreeRecyclerType获取子item集合,不包含TreeItemGroup自身
     *
     * @param itemGroup
     * @param type
     * @return
     */
    @NonNull
    public static ArrayList<TreeItem> getChildItemsWithType(@Nullable TreeItemGroup itemGroup, @Nullable TreeRecyclerType type) {
        return getChildItemsWithType(itemGroup.getChild(), type);
    }

    @NonNull
    public static ArrayList<TreeItem> getChildItemsWithType(@Nullable List<TreeItem> items, @NonNull TreeRecyclerType type) {
        ArrayList<TreeItem> returnItems = new ArrayList<>();
        if (items == null) {
            return returnItems;
        }
        int childCount = items.size();
        for (int i = 0; i < childCount; i++) {
            TreeItem childItem = items.get(i);//获取当前一级
            returnItems.add(childItem);
            if (childItem instanceof TreeItemGroup) {//获取下一级
                List list = null;
                switch (type) {
                    case SHOW_ALL:
                        //调用下级的getAllChild遍历,相当于递归遍历
                        list = getChildItemsWithType((TreeItemGroup) childItem, type);
                        break;
                    case SHOW_EXPAND:
                        //根据isExpand,来决定是否展示
                        if (((TreeItemGroup) childItem).isExpand()) {
                            list = getChildItemsWithType((TreeItemGroup) childItem, type);
                        }
                        break;
                }
                if (list != null && list.size() > 0) {
                    returnItems.addAll(list);
                }
            }
        }
        return returnItems;
    }
}
