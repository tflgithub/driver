package fodel.com.fodelscanner.activity.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.library.treerecyclerview.base.ViewHolder;
import com.library.treerecyclerview.factory.ItemHelperFactory;
import com.library.treerecyclerview.item.TreeItem;
import com.library.treerecyclerview.item.TreeItemGroup;

import java.util.ArrayList;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;

/**
 * Created by fula on 2018/7/20.
 */

public class DeliveryDateItemPrent extends TreeItemGroup<ResHistory.Delivery.SubItem.DateItem> {
    @Override
    public int getLayoutId() {
        return R.layout.hitory_subitem_dateitem_group_item;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        String amount = data.parcelItems.size() > 1 ? data.parcelItems.size() + " Parcels" : data.parcelItems.size() + " Parcel";
        viewHolder.getTextView(R.id.tv_amount).setText(amount);
        viewHolder.getTextView(R.id.tv_date).setText(data.date);
    }

    @Nullable
    @Override
    protected ArrayList<TreeItem> initChildList(ResHistory.Delivery.SubItem.DateItem data) {
        return ItemHelperFactory.createItems(data.parcelItems, this);
    }
}
