package fodel.com.fodelscanner.activity.item;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.library.treerecyclerview.base.ViewHolder;
import com.library.treerecyclerview.item.TreeItem;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;
import fodel.com.fodelscanner.utils.ToastUtils;

/**
 * Created by fula on 2018/7/20.
 */

public class DeliveryParcelItem extends TreeItem<ResHistory.Delivery.SubItem.DateItem.ParcelItem> {
    @Override
    public int getLayoutId() {
        return R.layout.history_subitem_dateitem_parcel_item;
    }

    private int beginChangePosName, beginChangePosAwb;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        String text = getItemManager().getText();
        ForegroundColorSpan span = getItemManager().getSpan();
        if (text != null) {
            //获取匹配文字的 position
            beginChangePosAwb = data.awb.indexOf(text);
            // 文字的builder 用来做变色操作
            SpannableStringBuilder builder = new SpannableStringBuilder(data.awb);
            //如果没有匹配到关键字的话 list.get(position).indexOf(text)会返回-1
            if (beginChangePosAwb != -1) {
                //设置呈现的文字
                builder.setSpan(span, beginChangePosAwb, beginChangePosAwb + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.getTextView(R.id.tv_awb).setText(builder);
            } else {
                viewHolder.getTextView(R.id.tv_awb).setText(data.awb);
            }
        } else {
            viewHolder.getTextView(R.id.tv_awb).setText(data.awb);
        }

        if (text != null) {
            //获取匹配文字的 position
            beginChangePosName = data.name.indexOf(text);
            // 文字的builder 用来做变色操作
            SpannableStringBuilder builder = new SpannableStringBuilder(data.name);
            //如果没有匹配到关键字的话 list.get(position).indexOf(text)会返回-1
            if (beginChangePosName != -1) {
                //设置呈现的文字
                builder.setSpan(span, beginChangePosName, beginChangePosName + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.getTextView(R.id.tv_name).setText(builder);
            } else {
                viewHolder.getTextView(R.id.tv_name).setText(data.name);
            }
        } else {
            viewHolder.getTextView(R.id.tv_name).setText(data.name);
        }
        viewHolder.getTextView(R.id.tv_time).setText(data.time);
        viewHolder.setOnClickListener(R.id.tv_awb, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager copy = (ClipboardManager) MyApplication.getApplication().currentActivity
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText(data.awb);
                ToastUtils.showShort(MyApplication.getApplication().currentActivity, data.awb + " has been copied to the clipboard");
            }
        });
    }
}
