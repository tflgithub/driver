package fodel.com.fodelscanner.activity.item.scan;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.library.treerecyclerview.base.ViewHolder;
import com.library.treerecyclerview.factory.ItemHelperFactory;
import com.library.treerecyclerview.item.TreeItem;
import com.library.treerecyclerview.item.TreeItemGroup;
import java.util.ArrayList;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResScanBean;

/**
 * Created by fula on 2018/7/26.
 */

public class ScanItemParent extends TreeItemGroup<ResScanBean.Bill> {

    @Override
    public int getLayoutId() {
        return R.layout.scan_item_header;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        viewHolder.getTextView(R.id.tv_shop_name).setText(data.collection_point_name);
        viewHolder.getTextView(R.id.tv_site_id).setText(data.site_id);
        if (data.awbs.size() > 1) {
            viewHolder.getTextView(R.id.tv_count).setText(data.awbs.size() + " Parcels");
        } else {
            viewHolder.getTextView(R.id.tv_count).setText(data.awbs.size() + " Parcel");
        }
    }

    @Nullable
    @Override
    protected ArrayList<TreeItem> initChildList(ResScanBean.Bill data) {
        return ItemHelperFactory.createItems(data.awbs, this);
    }
}
