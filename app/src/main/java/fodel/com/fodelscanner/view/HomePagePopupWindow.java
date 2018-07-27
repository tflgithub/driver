package fodel.com.fodelscanner.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;
import fodel.com.fodelscanner.scanner.ui.MenuActivity;
import fodel.com.fodelscanner.utils.MapUtil;

/**
 * Created by fula on 2017/7/31.
 */

public class HomePagePopupWindow extends PopupWindow {

    private Context mContext;

    private View mView;

    public HomePagePopupWindow(Context context, int resId) {
        super(context);
        this.mContext = context;
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(resId, null);
        setContentView(view);
        view.setPadding(25, 25, 25, 25);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(0x0000000);
        setBackgroundDrawable(dw);
        mView = view;
    }

    public void render(final Marker marker) {
        String snippet = marker.getSnippet();
        if (snippet == null) {
            return;
        }
        String[] str = snippet.split("&");
        final String site_id = str[0];
        final String station_id = str[1];
        final String station_name = str[2];
        String area = str[3];
        String lables = str[4];
        String works = str[5];
        String types = str[6];
        final String location = str[7];
        String distance = str[8];
        final String[] label = lables.split(",");
        final String[] type = types.split(",");
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MenuActivity.class);
                List<ResMenu> resMenus = new ArrayList<>();
                for (int i = 0; i < label.length; i++) {
                    ResMenu menuBean = new ResMenu();
                    String[] nameStr = label[i].split(":");
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(nameStr[0]).append("(").append(nameStr[1]).append(")");
                    menuBean.name = stringBuffer.toString();
                    menuBean.type = type[i];
                    resMenus.add(menuBean);
                }
                intent.putExtra("name", station_name);
                intent.putExtra("id", station_id);
                intent.putParcelableArrayListExtra("menu_list", (ArrayList<? extends Parcelable>) resMenus);
                mContext.startActivity(intent);
            }
        });
        TextView tv_site = (TextView) mView.findViewById(R.id.tv_site_id);
        TextView tv_name = (TextView) mView.findViewById(R.id.tv_name);
        TextView tv_area = (TextView) mView.findViewById(R.id.tv_area);
        TextView tv_distance = (TextView) mView.findViewById(R.id.tv_distance_mark);
        tv_distance.setText(distance);
        mView.findViewById(R.id.img_route_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] str = location.split(",");
                double lat = Double.valueOf(str[0]);
                double lng = Double.valueOf(str[1]);
                MapUtil.startNavigation(mContext, new LatLng(lat, lng), marker.getPosition());
            }
        });
        LinearLayout ll_work_mark = (LinearLayout) mView.findViewById(R.id.work_status_layout);
        ll_work_mark.removeAllViews();
        LinearLayout ll_label_mark = (LinearLayout) mView.findViewById(R.id.type_mark_layout);
        ll_label_mark.removeAllViews();
        tv_site.setText(site_id);
        tv_name.setText(station_name);
        tv_area.setText(area);
        for (String s : label) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.simple_item_02, null);
            ((TextView) view).setText(s);
            if (s.contains("CHECK-IN")) {
                view.setBackgroundColor(mContext.getResources().getColor(R.color.red_01));
            } else if (s.contains("RETURN")) {
                view.setBackgroundColor(mContext.getResources().getColor(R.color.green_01));
            } else {
                view.setBackgroundColor(mContext.getResources().getColor(R.color.yellow_01));
            }
            ll_label_mark.addView(view);
        }
        String work[] = works.split(",");
        for (String s : work) {
            View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.simple_item_01, null);
            ((TextView) view).setText(s);
            ll_work_mark.addView(view);
        }
    }
}
