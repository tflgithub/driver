package fodel.com.fodelscanner.scanner.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResCodCollection;

/**
 * Created by Happiness on 2017/2/28.
 */
public class CodCollectionAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private List<ResCodCollection.Invoice> mDatas;

    public Map<String, Boolean> groupCheckBoxMap;

    private String currency;

    public CodCollectionAdapter(Context context, List<ResCodCollection.Invoice> list, String currency) {
        this.mContext = context;
        this.mDatas = list;
        this.currency = currency;
        initCheckBox();
    }

    public void initCheckBox() {
        groupCheckBoxMap = new HashMap<>();
        for (int i = 0; i < mDatas.size(); i++) {
            groupCheckBoxMap.put(mDatas.get(i).bill_number, false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).order_list.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).order_list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cod_collection_item, null);
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final int mGroupPosition = groupPosition;
        viewHolder.checkBox.setChecked(groupCheckBoxMap.get(mDatas.get(mGroupPosition).bill_number));
        viewHolder.textView1.setText(mDatas.get(groupPosition).bill_number);
        viewHolder.textView2.setText(currency + " " + (float)mDatas.get(groupPosition).amount/100);
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isChecked = viewHolder.checkBox.isChecked();
                groupCheckBoxMap.put(mDatas.get(mGroupPosition).bill_number, isChecked);
                if (changeButtonTextListener != null) {
                    if (isChecked) {
                        changeButtonTextListener.onAdd(mDatas.get(groupPosition).amount);
                    } else {
                        changeButtonTextListener.onDec(mDatas.get(groupPosition).amount);
                    }
                }
            }
        });
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cod_order_item, null);
            holder = new ViewHolder();
            holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView1.setText(mDatas.get(groupPosition).order_list.get(childPosition).awb);
        holder.textView2.setText(currency+" "+(float)mDatas.get(groupPosition).order_list.get(childPosition).amount/100 );
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    class ViewHolder {
        TextView textView1, textView2;
        CheckBox checkBox;
    }

    private ChangeButtonTextListener changeButtonTextListener;

    public void setChangeButtonTextListener(ChangeButtonTextListener changeButtonTextListener) {
        this.changeButtonTextListener = changeButtonTextListener;
    }

    public interface ChangeButtonTextListener {

        void onAdd(int money);

        void onDec(int money);
    }
}