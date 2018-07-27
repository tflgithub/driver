package fodel.com.fodelscanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fodel.com.fodelscanner.R;

/**
 * Created by Administrator on 2016/4/22.
 */
public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mDatas;

    private Context context;
    public OrderListAdapter(List<String> data, Context context) {
        this.mDatas=data;
        this.context=context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goods_list_item, parent, false);
        return new OrderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String item = mDatas.get(position);
        final OrderListAdapter.ViewHolder viewHolder = (OrderListAdapter.ViewHolder) holder;
        viewHolder.tv_order_no.setText(item);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    private static final class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_order_no;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_order_no = (TextView) itemView.findViewById(R.id.tv_goods);
        }
    }
}
