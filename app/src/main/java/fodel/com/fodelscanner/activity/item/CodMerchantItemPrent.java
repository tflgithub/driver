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

public class CodMerchantItemPrent extends TreeItemGroup<ResHistory.Cod.CodDateItem.CodMerchantItems> {
    @Override
    public int getLayoutId() {
        return R.layout.hitory_merchant_item;
    }

    private DecimalFormat decimalFormat = new DecimalFormat(".00");

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        viewHolder.getTextView(R.id.tv_name).setText(data.name);
        viewHolder.getTextView(R.id.tv_amount).setText("AED " + getAmount(data));
    }

    private String getAmount(ResHistory.Cod.CodDateItem.CodMerchantItems data) {
        int amount = 0;
        for (ResHistory.Cod.CodDateItem.CodMerchantItems.InvoiceItems invoiceItems : data.invoiceItems) {
            amount = amount + invoiceItems.intAmount;
        }
        return amount % 100 == 0 ? Integer.toString(amount / 100) : decimalFormat.format((float) amount / 100);
    }

    @Nullable
    @Override
    protected ArrayList<TreeItem> initChildList(ResHistory.Cod.CodDateItem.CodMerchantItems data) {
        return ItemHelperFactory.createItems(data.invoiceItems, this);
    }
}
