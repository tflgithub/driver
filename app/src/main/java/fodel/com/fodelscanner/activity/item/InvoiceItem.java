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

import java.util.ArrayList;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;
import fodel.com.fodelscanner.utils.ToastUtils;

import static android.R.id.list;

/**
 * Created by fula on 2018/7/20.
 */

public class InvoiceItem extends TreeItem<ResHistory.Cod.CodDateItem.CodMerchantItems.InvoiceItems> {
    @Override
    public int getLayoutId() {
        return R.layout.history_invoice_item;
    }

    private int beginChangePos;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        String text = getItemManager().getText();
        ForegroundColorSpan span = getItemManager().getSpan();
        if (text != null) {
            //获取匹配文字的 position
            beginChangePos = data.billNo.indexOf(text);
            // 文字的builder 用来做变色操作
            SpannableStringBuilder builder = new SpannableStringBuilder(data.billNo);
            //如果没有匹配到关键字的话 list.get(position).indexOf(text)会返回-1
            if (beginChangePos != -1) {
                //设置呈现的文字
                builder.setSpan(span, beginChangePos, beginChangePos + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.getTextView(R.id.tv_bill_no).setText(builder);
            }
        } else {
            viewHolder.getTextView(R.id.tv_bill_no).setText(data.billNo);
        }
        viewHolder.getTextView(R.id.tv_time).setText(data.time);
        viewHolder.getTextView(R.id.tv_amount).setText(data.amount);
        viewHolder.setOnClickListener(R.id.tv_bill_no, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager copy = (ClipboardManager) MyApplication.getApplication().currentActivity
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText(data.billNo);
                ToastUtils.showShort(MyApplication.getApplication().currentActivity, data.billNo + " has been copied to the clipboard");
            }
        });
    }
}
