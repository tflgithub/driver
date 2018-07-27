package fodel.com.fodelscanner.scanner.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.util.Calendar;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;
import fodel.com.fodelscanner.scanner.ui.HistoryActivity;

/**
 * Created by fula on 2018/7/25.
 */

public class HistoryFragment extends BackHandledFragment {

    @ViewInject(R.id.edit_start_date)
    private EditText edit_start_date;
    @ViewInject(R.id.edit_end_date)
    private EditText edit_end_date;
    @ViewInject(R.id.rg_type)
    private RadioGroup typeRadioGroup;
    @ViewInject(R.id.rl_pickup)
    private RelativeLayout rl_pickup;
    @ViewInject(R.id.tv_pickup_name)
    private TextView tv_pickup_name;
    @ViewInject(R.id.tv_pickup_amount)
    private TextView tv_pickup_amount;
    @ViewInject(R.id.rl_deliver)
    private RelativeLayout rl_deliver;
    @ViewInject(R.id.tv_deliver_name)
    private TextView tv_deliver_name;
    @ViewInject(R.id.tv_deliver_amount)
    private TextView tv_deliver_amount;
    @ViewInject(R.id.rl_cod)
    private RelativeLayout rl_cod;
    @ViewInject(R.id.tv_cod_name)
    private TextView tv_cod_name;
    @ViewInject(R.id.tv_cod_amount)
    private TextView tv_cod_amount;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private String startDate, endDate, type = "ALL";
    private ResHistory resHistory;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    public void initView() {
        Calendar calendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                edit_start_date.setText(formatDayOrMonth(dayOfMonth) + "/" + formatDayOrMonth(monthOfYear) + "/" + year);
                edit_end_date.setFocusable(true);
                edit_end_date.findFocus();
                edit_end_date.requestFocus();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                edit_end_date.setText(formatDayOrMonth(dayOfMonth) + "/" + formatDayOrMonth(monthOfYear) + "/" + year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        edit_start_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!startDatePickerDialog.isShowing()) {
                    startDatePickerDialog.show();
                }
                return false;
            }
        });
        edit_end_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!endDatePickerDialog.isShowing()) {
                    endDatePickerDialog.show();
                }
                return false;
            }
        });

        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_client:
                        type = "CLIENT";
                        break;
                    case R.id.rb_merchant:
                        type = "MERCHANT";
                        break;
                    case R.id.rb_all:
                        type = "ALL";
                        break;
                }
                getData();
            }
        });
    }

    private void getData() {
        startDate = edit_start_date.getText().toString();
        endDate = edit_end_date.getText().toString();
        ((HistoryActivity) getActivity()).getData(startDate, endDate, type);
    }

    @OnClick({R.id.rl_pickup, R.id.rl_deliver, R.id.rl_cod, R.id.btn_search})
    public void onClick(View view) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.rl_pickup:
                bundle.putParcelable("subItem", resHistory.pickup);
                HistoryDeliveryFragment historyPickupFragment = (HistoryDeliveryFragment) manager.findFragmentByTag("historyDelivery");
                if (historyPickupFragment == null) {
                    historyPickupFragment = new HistoryDeliveryFragment();
                }
                historyPickupFragment.setArguments(bundle);
                ft.replace(R.id.fl_container, historyPickupFragment, "historyDelivery");
                ft.addToBackStack("tag");
                ft.commit();
                break;
            case R.id.rl_deliver:
                bundle.putParcelable("subItem", resHistory.delivery);
                HistoryDeliveryFragment historyDeliveryFragment = (HistoryDeliveryFragment) manager.findFragmentByTag("historyDelivery");
                if (historyDeliveryFragment == null) {
                    historyDeliveryFragment = new HistoryDeliveryFragment();
                }
                historyDeliveryFragment.setArguments(bundle);
                ft.replace(R.id.fl_container, historyDeliveryFragment, "historyDelivery");
                ft.addToBackStack("tag");
                ft.commit();
                break;
            case R.id.rl_cod:
                ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
                HistoryDetailFragment historyDetailFragment = (HistoryDetailFragment) manager.findFragmentByTag("historyDetail");
                if (historyDetailFragment == null) {
                    historyDetailFragment = new HistoryDetailFragment();
                }
                bundle.putParcelable("data", resHistory.cod);
                historyDetailFragment.setArguments(bundle);
                ft.replace(R.id.fl_container, historyDetailFragment, "historyDetail");
                ft.addToBackStack("tag");
                ft.commit();
                break;
            case R.id.btn_search:
                getData();
                break;
        }
    }

    private String formatDayOrMonth(int number) {
        String formatStr = String.valueOf(number);
        if (number < 10) {
            formatStr = "0" + number;
            return formatStr;
        }
        return formatStr;
    }

    @Override
    public void initData() {
        if (resHistory != null) {
            setData(resHistory);
        }
    }

    public void setData(ResHistory history) {
        resHistory = history;
        if (resHistory != null) {
            if (resHistory.pickup != null) {
                rl_pickup.setVisibility(View.VISIBLE);
                tv_pickup_name.setText(resHistory.pickup.name);
                tv_pickup_amount.setText(resHistory.pickup.amount);
            } else {
                rl_pickup.setVisibility(View.GONE);
            }
            if (resHistory.delivery != null) {
                rl_deliver.setVisibility(View.VISIBLE);
                tv_deliver_name.setText(resHistory.delivery.name);
                tv_deliver_amount.setText(resHistory.delivery.amount);
            } else {
                rl_deliver.setVisibility(View.GONE);
            }
            if (resHistory.cod != null) {
                rl_cod.setVisibility(View.VISIBLE);
                tv_cod_name.setText(resHistory.cod.name);
                tv_cod_amount.setText(resHistory.cod.amount);
            } else {
                rl_cod.setVisibility(View.GONE);
            }
        }
    }
}
