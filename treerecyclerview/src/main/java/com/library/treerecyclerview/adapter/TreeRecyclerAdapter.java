package com.library.treerecyclerview.adapter;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.library.treerecyclerview.base.BaseRecyclerAdapter;
import com.library.treerecyclerview.base.ViewHolder;
import com.library.treerecyclerview.factory.ItemHelperFactory;
import com.library.treerecyclerview.item.TreeItem;
import com.library.treerecyclerview.item.TreeItemGroup;
import com.library.treerecyclerview.manager.ItemManager;

import java.util.ArrayList;

/**
 * Created by fula on 2018/7/20.
 */

public class TreeRecyclerAdapter extends BaseRecyclerAdapter<TreeItem>{

    private TreeRecyclerType type;

    private ItemManager<TreeItem> mItemManager;

    public TreeRecyclerAdapter(TreeRecyclerType treeRecyclerType) {
        type = treeRecyclerType == null ? TreeRecyclerType.SHOW_DEFAULT : treeRecyclerType;
    }

    @Override
    public void onBindViewHolderClick(final ViewHolder holder, View view) {
        if (!view.hasOnClickListeners()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    //拿到BaseItem
                    TreeItem item = getDatas().get(layoutPosition);
                    TreeItemGroup itemParentItem = item.getParentItem();
                    //判断上一级是否需要拦截这次事件，只处理当前item的上级，不关心上上级如何处理.
                    if (itemParentItem != null && itemParentItem.onInterceptClick(item)) {
                        return;
                    }
                    //必须是TreeItemGroup才能展开折叠,并且type不能为 TreeRecyclerType.SHOW_ALL
                    if (item instanceof TreeItemGroup && type != TreeRecyclerType.SHOW_ALL) {
                        TreeItemGroup treeItemGroup = (TreeItemGroup) item;
                        treeItemGroup.setExpand(!treeItemGroup.isExpand());
                    }
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(holder, layoutPosition);
                    } else {
                        getDatas().get(layoutPosition).onClick(holder);
                    }
                }
            });
        }
    }


    @Override
    public void setDatas(ArrayList<TreeItem> items) {
        if (null == items || items.isEmpty()) {
            return;
        }
        getDatas().clear();
        assembleItems(items);
    }

    /**
     * 对初始的一级items进行遍历,将每个item的childs拿出来,进行組合。
     *
     * @param items
     */
    private void assembleItems(ArrayList<TreeItem> items) {
        if (type != null) {
            ArrayList<TreeItem> datas = getDatas();
            datas.addAll(ItemHelperFactory.getChildItemsWithType(items, type));
        } else {
            super.setDatas(items);
        }
    }

    public ItemManager<TreeItem> getItemManager() {
        if (mItemManager == null) {
            mItemManager = new TreeItemManageImpl(this);
        }
        return mItemManager;
    }

    @Override
    public int getLayoutId(int position) {
        return getDatas().get(position).getLayoutId();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TreeItem t = getDatas().get(position);
        checkItemManage(t);
        t.onBindViewHolder(holder);
    }

    private void checkItemManage(TreeItem item) {
        if (item.getItemManager() == null) {
            item.setItemManager(getItemManager());
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, TreeItem treeItem, int position) {
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                int viewLayoutPosition = layoutParams.getViewLayoutPosition();
                int i = getItemCount();
                if (getItemCount() == 0) {
                    return;
                }
                if (viewLayoutPosition < 0 || viewLayoutPosition >= i) {
                    return;
                }
                getData(viewLayoutPosition).getItemOffsets(outRect, layoutParams, viewLayoutPosition);
            }
        });
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int i = getItemCount();
                    if (i == 0) {
                        return spanCount;
                    }
                    int checkPosition = position;
                    if (checkPosition < 0 || checkPosition >= i) {
                        return spanCount;
                    }
                    int itemSpanSize = getItemSpanSize(checkPosition);
                    if (itemSpanSize == 0) {
                        return spanCount;
                    }
                    return itemSpanSize;
                }
            });
        }
    }

    public int getItemSpanSize(int position) {
        return getData(position).getSpanSize();
    }


    private class TreeItemManageImpl extends ItemManager<TreeItem> {

        TreeItemManageImpl(BaseRecyclerAdapter<TreeItem> adapter) {
            super(adapter);
        }

        @Override
        public void addItem(TreeItem item) {
            if (null == item) {
                return;
            }
            if (item instanceof TreeItemGroup) {
                ArrayList<TreeItem> childItemsWithType = ItemHelperFactory.getChildItemsWithType((TreeItemGroup) item, type);
                childItemsWithType.add(0, item);
                getDatas().addAll(childItemsWithType);
            } else {
                getDatas().add(item);
            }
            notifyDataChanged();
        }


        @Override
        public void addItem(int position, TreeItem item) {
            getDatas().add(position, item);
            notifyDataChanged();
        }

        @Override
        public void addItems(ArrayList<TreeItem> items) {
            ArrayList<TreeItem> childItemsWithType = ItemHelperFactory.getChildItemsWithType(items, type);
            getDatas().addAll(childItemsWithType);
            notifyDataChanged();
        }

        @Override
        public void addItems(int position, ArrayList<TreeItem> items) {
            ArrayList<TreeItem> childItemsWithType = ItemHelperFactory.getChildItemsWithType(items, type);
            getDatas().addAll(position, childItemsWithType);
            notifyDataChanged();
        }

        @Override
        public void removeItem(TreeItem item) {
            if (null == item) {
                return;
            }
            if (item instanceof TreeItemGroup) {
                ArrayList<TreeItem> childItemsWithType = ItemHelperFactory.getChildItemsWithType((TreeItemGroup) item, type);
                childItemsWithType.add(0, item);
                getDatas().removeAll(childItemsWithType);
            } else {
                getDatas().remove(item);
            }
            notifyDataChanged();
        }

        @Override
        public void removeItem(int position) {
            getDatas().remove(position);
            notifyDataChanged();
        }

        @Override
        public void removeItems(ArrayList<TreeItem> items) {
            ArrayList<TreeItem> childItemsWithType = ItemHelperFactory.getChildItemsWithType(items, type);
            getDatas().removeAll(childItemsWithType);
            notifyDataChanged();
        }

        @Override
        public void replaceItem(int position, TreeItem item) {
            getDatas().set(position, item);
            notifyDataChanged();
        }

        @Override
        public void replaceAllItem(ArrayList<TreeItem> items) {
            if (items != null) {
                setDatas(items);
                notifyDataChanged();
            }
        }

        @Override
        public void clean() {
            getDatas().clear();
            notifyDataChanged();
        }

        @Override
        public TreeItem getItem(int position) {
            return getDatas().get(position);
        }

        @Override
        public int getItemPosition(TreeItem item) {
            return getDatas().indexOf(item);
        }
    }
}
