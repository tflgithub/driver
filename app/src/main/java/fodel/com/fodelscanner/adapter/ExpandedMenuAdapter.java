package fodel.com.fodelscanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.helper.ResourceHelper;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;

/**
 * Created by Happiness on 2017/3/2.
 */
public class ExpandedMenuAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private List<ResMenu> mDatas;

    public ExpandedMenuAdapter(Context context, List<ResMenu> list) {
        this.mContext = context;
        this.mDatas = list;
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mDatas.get(groupPosition).list == null) {
            return 0;
        } else {
            return mDatas.get(groupPosition).list.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).list.get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expanded_menu, null);
            viewHolder.iv_menu_icon = (ImageView) convertView.findViewById(R.id.iv_menu_icon);
            viewHolder.tv_menu_name = (TextView) convertView.findViewById(R.id.tv_menu_name);
            viewHolder.tv_menu_record = (TextView) convertView.findViewById(R.id.tv_menu_record);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iv_menu_icon.setImageResource(ResourceHelper.getInstance(mContext).getDrawableId(mDatas.get(groupPosition).draw_id));
        viewHolder.tv_menu_name.setText(mDatas.get(groupPosition).name);
        if (mDatas.get(groupPosition).amount != null && !mDatas.get(groupPosition).amount.equals("")) {
            viewHolder.tv_menu_record.setVisibility(View.VISIBLE);
            viewHolder.tv_menu_record.setText(mDatas.get(groupPosition).amount);
        } else {
            viewHolder.tv_menu_record.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
  //      ViewHolder viewHolder;
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.expanded_menu, null);
//            viewHolder.iv_menu_icon = (ImageView) convertView.findViewById(R.id.iv_menu_icon);
//            viewHolder.tv_menu_name = (TextView) convertView.findViewById(R.id.tv_menu_name);
//            viewHolder.tv_menu_record = (TextView) convertView.findViewById(R.id.tv_menu_record);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        viewHolder.iv_menu_icon.setImageResource(ResourceHelper.getInstance(mContext).getDrawableId(mDatas.get(groupPosition).list.get(childPosition).draw_id));
//        viewHolder.tv_menu_name.setText(mDatas.get(groupPosition).list.get(childPosition).name);
//        if (!mDatas.get(groupPosition).list.get(childPosition).amount.equals("0")) {
//            viewHolder.tv_menu_record.setVisibility(View.VISIBLE);
//            viewHolder.tv_menu_record.setText(mDatas.get(groupPosition).list.get(childPosition).amount);
//        } else {
//            viewHolder.tv_menu_record.setVisibility(View.GONE);
//        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {
        ImageView iv_menu_icon;
        TextView tv_menu_name, tv_menu_record;
    }
}
