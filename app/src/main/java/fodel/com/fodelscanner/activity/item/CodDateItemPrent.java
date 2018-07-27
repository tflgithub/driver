package fodel.com.fodelscanner.activity.item;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.library.treerecyclerview.base.ViewHolder;
import com.library.treerecyclerview.factory.ItemHelperFactory;
import com.library.treerecyclerview.item.TreeItem;
import com.library.treerecyclerview.item.TreeItemGroup;

import java.text.DecimalFormat;
import java.util.ArrayList;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;

/**
 * Created by fula on 2018/7/20.
 */

public class CodDateItemPrent extends TreeItemGroup<ResHistory.Cod.CodDateItem> {
    @Override
    public int getLayoutId() {
        return R.layout.hitory_subitem_dateitem_group_item;
    }

    private DecimalFormat decimalFormat = new DecimalFormat(".00");

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        viewHolder.getTextView(R.id.tv_amount).setText("AED " + getAmount(data.codMerchantItems));
        viewHolder.getTextView(R.id.tv_date).setText(data.date);
    }

    private String getAmount(ArrayList<ResHistory.Cod.CodDateItem.CodMerchantItems> data) {
        int amount = 0;
        for (ResHistory.Cod.CodDateItem.CodMerchantItems codMerchantItem : data) {
            for (ResHistory.Cod.CodDateItem.CodMerchantItems.InvoiceItems invoiceItem : codMerchantItem.invoiceItems) {
                amount = amount + invoiceItem.intAmount;
            }
        }
        return amount % 100 == 0 ? Integer.toString(amount / 100) : decimalFormat.format((float) amount / 100);
    }

    @Nullable
    @Override
    protected ArrayList<TreeItem> initChildList(ResHistory.Cod.CodDateItem data) {
        return ItemHelperFactory.createItems(data.codMerchantItems, this);
    }
}
