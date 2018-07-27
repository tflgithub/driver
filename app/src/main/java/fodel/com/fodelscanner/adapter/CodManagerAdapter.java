package fodel.com.fodelscanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.freedom.yefeng.yfrecyclerview.YfListAdapter;
import com.freedom.yefeng.yfrecyclerview.YfSimpleViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResInvoice;
import fodel.com.fodelscanner.scanner.ui.CodManagementActivity;

/**
 * Created by fula on 2016/4/22.
 */
public class CodManagerAdapter extends YfListAdapter<ResInvoice.Bill> {

    public Map<String, Boolean> selectedMap;
    public double total;
    public static int index = 0;
    private Context mContext;

    public CodManagerAdapter(ArrayList<ResInvoice.Bill> data, Context context) {
        super(data);
        this.mContext = context;
    }

    public double getTotal() {
        return total;
    }

    public void initCheckBox() {
        selectedMap = new HashMap<>();
        for (int i = 0; i < mData.size(); i++) {
            selectedMap.put(mData.get(i).bill_no, false);
        }
        notifyDataSetChanged();
    }

    public void selectedAll() {
        total = 0;
        index = selectedMap.size();
        for (int i = 0; i < mData.size(); i++) {
            total = total + (double) mData.get(i).money.amount/100;
            selectedMap.put(mData.get(i).bill_no, true);
        }
        notifyDataSetChanged();
    }

    public Map<String, Boolean> getSelectedMap() {
        return selectedMap;
    }

    public void add(double amount) {
        total = total + amount;
    }

    public void dec(double amount) {
        total = total - amount;
    }

    public void singleSelected(String billNo, boolean selected, boolean isOnItemClick) {
        selectedMap.put(billNo, selected);
        if (selected) {
            index++;
        } else {
            index--;
        }
        if (isOnItemClick) {
            notifyDataSetChanged();
        }
    }

    public void unSelectedAll() {
        total = 0;
        index = 0;
        for (int i = 0; i < mData.size(); i++) {
            selectedMap.put(mData.get(i).bill_no, false);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cod_manager_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateEmptyViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_empty_material, parent, false);
        return new YfSimpleViewHolder(view);
    }

    @Override
    public void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ResInvoice.Bill item = mData.get(position);
        ((ViewHolder) holder).tv_bill_no.setText(item.bill_no);
        ((ViewHolder) holder).tv_amount.setText(item.money.currency + " " + (float)item.money.amount/100);
        ((ViewHolder) holder).checkBox.setChecked(selectedMap.get(mData.get(position).bill_no));
        ((ViewHolder) holder).checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelect = selectedMap.get(item.bill_no);
                if (!isSelect) {
                    singleSelected(item.bill_no, true, false);
                    add((double) item.money.amount/100);
                    if (index == selectedMap.size()) {
                        ((CodManagementActivity) mContext).isSelectAll = true;
                        ((CodManagementActivity) mContext).btn_select_all.setText("Unselected All");
                    }
                } else {
                    singleSelected(item.bill_no, false, false);
                    dec((double)item.money.amount/100);
                    ((CodManagementActivity) mContext).isSelectAll = false;
                    ((CodManagementActivity) mContext).btn_select_all.setText("Selected All");
                }
                ((CodManagementActivity) mContext).setBtnBackground(index);
            }
        });
        holder.itemView.setTag(item);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_footer, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public void onBindErrorViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindErrorViewHolder(holder, position);
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_bill_no, tv_amount;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_bill_no = (TextView) itemView.findViewById(R.id.tv_bill_no);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb);
        }
    }

    private static final class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView mText;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.tv_footer);
        }
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        String footer = (String) mFooters.get(position);
        ((FooterViewHolder) holder).mText.setText(footer);
        holder.itemView.setTag(footer);
    }

    @Override
    public RecyclerView.ViewHolder onCreateErrorViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_net_error_material, parent, false);
        return new YfSimpleViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateLoadingViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_loading_material, parent, false);
        return new YfSimpleViewHolder(view);
    }
}
