package fodel.com.fodelscanner.scanner.ui.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.xtouch.tfl.alert.AlertView;
import com.cn.xtouch.tfl.alert.OnItemClickListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.cache.bean.Merchant;
import fodel.com.fodelscanner.service.LocationService;
import fodel.com.fodelscanner.utils.MapUtil;
import fodel.com.fodelscanner.utils.StringUtils;

/**
 * Created by Happiness on 2017/3/7.
 */
public class MerchantActionsAdapter extends BaseAdapter {

    private Context mContext;

    private List<Merchant> mData;

    int mSelect = -1;   //选中项

    public MerchantActionsAdapter(Context context, List<Merchant> list) {
        this.mContext = context;
        this.mData = list;

    }

    public void setData(List<Merchant> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public void changeSelected(int position) { //刷新方法
        if (position != mSelect) {
            mSelect = position;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.merchant_actions_item, null);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_site_id = (TextView) convertView.findViewById(R.id.tv_site_id);
            viewHolder.tv_distance_mark = (TextView) convertView.findViewById(R.id.tv_distance_mark);
            viewHolder.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            viewHolder.call_btn = (ImageButton) convertView.findViewById(R.id.ib_call);
            viewHolder.route_btn = (ImageButton) convertView.findViewById(R.id.img_route_btn);
            viewHolder.linearLayout1 = (LinearLayout) convertView.findViewById(R.id.work_status_layout);
            viewHolder.linearLayout2 = (LinearLayout) convertView.findViewById(R.id.type_mark_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Merchant item = (Merchant) getItem(position);
        viewHolder.tv_name.setText(item.station_name);
        viewHolder.tv_site_id.setText(item.site_id);
        viewHolder.tv_area.setText(item.area);
        if (item.distance == 0.0) {
            viewHolder.tv_distance_mark.setText("Unknown");
            viewHolder.route_btn.setEnabled(false);
        } else {
            viewHolder.route_btn.setEnabled(true);
            viewHolder.tv_distance_mark.setText(MapUtil.DistanceUtil.convertedDistance(item.distance));
        }
        viewHolder.route_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] locationStr = item.location.split(",");
                MapUtil.startNavigation(mContext, LocationService.currentLocation, new LatLng(Double.valueOf(locationStr[0]), Double.valueOf(locationStr[1])));
            }
        });
        if (!StringUtils.isEmpty(item.phone_number)) {
            viewHolder.call_btn.setVisibility(View.VISIBLE);
            viewHolder.call_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] numbers = item.phone_number.split("/");
                    new AlertView("Dial", null, null, "Cancel", null, numbers, mContext, AlertView.Style.ListAlert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            if (position != -1) {
                                call(numbers[position]);
                            }
                        }
                    }).show();

                }
            });
        } else {
            viewHolder.call_btn.setVisibility(View.GONE);
        }
        viewHolder.linearLayout1.removeAllViews();
        String[] workStatus = item.work_mark.split(",");
        for (String s : workStatus) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.simple_item_01, null);
            ((TextView) view).setText(s);
            viewHolder.linearLayout1.addView(view);
        }
        viewHolder.linearLayout2.removeAllViews();
        String[] type = item.type.split(",");
        String[] label = item.label.split(",");
        for (int i = 0; i < type.length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.simple_item_02, null);
            ((TextView) view).setText(label[i]);
            switch (type[i]) {
                case Constants.PICKUP_RETURN_AT_SHOP:
                    view.setBackgroundColor(mContext.getResources().getColor(R.color.green_01));
                    break;
                case Constants.DROP_AT_SHOP:
                    view.setBackgroundColor(mContext.getResources().getColor(R.color.red_01));
                    break;
                case Constants.COD_COLLECTION:
                    view.setBackgroundColor(mContext.getResources().getColor(R.color.yellow_01));
                    break;
            }
            viewHolder.linearLayout2.addView(view);
        }
        if (mSelect == position) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.green_02));  //选中项背景
        } else {
            convertView.setBackgroundResource(R.drawable.btn_selector);  //其他项背景
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_name, tv_site_id, tv_distance_mark, tv_area;
        LinearLayout linearLayout1, linearLayout2;
        ImageButton route_btn, call_btn;
    }


    private void call(String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mContext.startActivity(intent);
    }
}
