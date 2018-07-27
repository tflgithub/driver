package fodel.com.fodelscanner.scanner.ui.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.xtouch.tfl.alert.AlertView;
import com.cn.xtouch.tfl.alert.OnItemClickListener;

import java.util.List;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMerchant;
import fodel.com.fodelscanner.utils.MapUtil;
import fodel.com.fodelscanner.utils.ShareUtils;
import fodel.com.fodelscanner.utils.StringUtils;

/**
 * Created by Happiness on 2017/3/7.
 */
public class MoreMerchantAdapter extends BaseAdapter {

    private Context mContext;

    private List<ResMerchant> mData;


    public MoreMerchantAdapter(Context context, List<ResMerchant> list) {
        this.mContext = context;
        this.mData = list;
    }

    public void setData(List<ResMerchant> list) {
        this.mData = list;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.more_merchant_item, null);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_site_id = (TextView) convertView.findViewById(R.id.tv_site_id);
            viewHolder.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            viewHolder.call_btn = (ImageButton) convertView.findViewById(R.id.ib_call);
            viewHolder.share_btn = (ImageButton) convertView.findViewById(R.id.ib_share);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.work_status_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ResMerchant item = (ResMerchant) getItem(position);
        viewHolder.tv_site_id.setText(item.site_id);
        viewHolder.tv_name.setText(item.station_name);
        viewHolder.tv_area.setText(item.area);
        viewHolder.tv_address.setText(item.address);
        viewHolder.tv_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MapUtil.showLocation(mContext, item.lt, item.lg, item.address);
                return false;
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
        viewHolder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("Share", null, null, "Cancel", null, new String[]{"Message", "WhatsApp"}, mContext, AlertView.Style.ListAlert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            ShareUtils.telegramShare(mContext, item.sms_text);
                        } else if (position == 1) {
                            ShareUtils.whatsAppShare(mContext, item.sms_text);
                        }
                    }
                }).show();
            }
        });
        viewHolder.linearLayout.removeAllViews();
        if (item.work_status_mark != null) {
            for (String s : item.work_status_mark) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.simple_item_01, null);
                ((TextView) view).setText(s);
                viewHolder.linearLayout.addView(view);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_name, tv_site_id, tv_area, tv_address;
        ImageButton call_btn, share_btn;
        LinearLayout linearLayout;
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
