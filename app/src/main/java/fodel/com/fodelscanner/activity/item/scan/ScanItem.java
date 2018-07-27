package fodel.com.fodelscanner.activity.item.scan;

import android.support.annotation.NonNull;

import com.library.treerecyclerview.base.ViewHolder;
import com.library.treerecyclerview.item.TreeItem;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResScanBean;

/**
 * Created by fula on 2018/7/26.
 */

public class ScanItem extends TreeItem<ResScanBean.Bill.Awb> {

    @Override
    public int getLayoutId() {
        return R.layout.scan_item_content;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        if (getItemManager().getText() != null) {
            if (data.verified) {
                viewHolder.setTextColor(R.id.tv_awb, MyApplication.getApplication().getResources().getColor(R.color.orange));
            } else {
                viewHolder.setTextColor(R.id.tv_awb, MyApplication.getApplication().getResources().getColor(R.color.deep_green));
            }
        }
        viewHolder.getTextView(R.id.tv_awb).setText(data.awb);
        if (data.pieces > 1 && getItemManager().getText() == null) {
            viewHolder.setVisible(R.id.tv_piece, true);
            viewHolder.getTextView(R.id.tv_piece).setText(data.scanNumber + " of " + data.pieces);
        } else {
            viewHolder.setVisible(R.id.tv_piece, false);
        }
    }
}
