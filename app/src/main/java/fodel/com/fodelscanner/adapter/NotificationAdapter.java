package fodel.com.fodelscanner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freedom.yefeng.yfrecyclerview.YfListAdapter;
import com.freedom.yefeng.yfrecyclerview.YfSimpleViewHolder;

import java.util.ArrayList;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResNotification;

/**
 * Created by Administrator on 2016/4/22.
 */
public class NotificationAdapter extends YfListAdapter<ResNotification.NotificationItem> {

    public NotificationAdapter(ArrayList<ResNotification.NotificationItem> data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_adapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateEmptyViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_empty_material, parent, false);
        return new YfSimpleViewHolder(view);
    }

    @Override
    public void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position) {
        ResNotification.NotificationItem item = mData.get(position);
        ((ViewHolder) holder).tv_title.setText(item.title);
        ((ViewHolder) holder).tv_title.setTag(item.title);
        ((ViewHolder) holder).tv_content.setText(item.content);
        ((ViewHolder) holder).tv_content.setTag(item.content);
        ((ViewHolder) holder).tv_time.setText(item.create_time);
        ((ViewHolder) holder).tv_time.setTag(item.create_time);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_footer, parent, false);
        return new FooterViewHolder(view);
    }


    private static final class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_content, tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
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
