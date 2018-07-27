package fodel.com.fodelscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freedom.yefeng.yfrecyclerview.YfListAdapter;
import com.freedom.yefeng.yfrecyclerview.YfListInterface;
import com.freedom.yefeng.yfrecyclerview.YfSimpleViewHolder;

import java.util.ArrayList;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;

public class HistorySubItemAdapter extends YfListAdapter<ResHistory.Delivery.SubItem> {

    public HistorySubItemAdapter(ArrayList<ResHistory.Delivery.SubItem> data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_subitem_item, parent, false);
        return new HistorySubItemAdapter.ViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateEmptyViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_empty_material, parent, false);
        return new YfSimpleViewHolder(view);
    }

    @Override
    public void setOnItemClickListener(YfListInterface.OnItemClickListener onItemClickListener) {
        super.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position) {
        ResHistory.Delivery.SubItem item = mData.get(position);
        ((ViewHolder) holder).tv_amount.setText(String.valueOf(item.amount));
        ((ViewHolder) holder).tv_name.setText(item.name);
        holder.itemView.setTag(item);
    }

    private static final class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_amount;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
        }
    }
}
