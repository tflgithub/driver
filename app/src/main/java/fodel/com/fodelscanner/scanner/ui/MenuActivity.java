package fodel.com.fodelscanner.scanner.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.utils.StringUtils;
import fodel.com.fodelscanner.utils.ToastUtils;
import rx.functions.Action1;

public class MenuActivity extends BaseActivity {

    String id;
    String name;

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar(toolbar, getIntent().getStringExtra("menu_title"), true);
        if (getIntent().getStringExtra("address") != null) {
            TextView textView = (TextView) findViewById(R.id.tv_address);
            textView.setVisibility(View.VISIBLE);
            textView.setText(getIntent().getStringExtra("address"));
        }
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        List<ResMenu.SubMenu> menus = getIntent().getParcelableArrayListExtra("sub_menu_list");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ly_buttons);
        if (menus != null) {
            for (ResMenu.SubMenu subMenu : menus) {
                Button button = (Button) LayoutInflater.from(this).inflate(R.layout.menu_button, linearLayout, false);
                if (subMenu.amount == null) {
                    button.setText(subMenu.name);
                    button.setOnClickListener(new MyOnClickListener(subMenu.type, StringUtils.interceptParenthesis(subMenu.name), subMenu.class_name,subMenu.list));
                } else {
                    button.setText(subMenu.name + "(" + subMenu.amount + ")");
                    button.setOnClickListener(new MyOnClickListener(subMenu.type, subMenu.amount, subMenu.class_name,subMenu.list));
                }
                linearLayout.addView(button);
            }
        }
    }


    @Override
    protected int getResourcesId() {
        return R.layout.activity_menu;
    }

    @Override
    protected void initInjector() {

    }

    private class MyOnClickListener implements View.OnClickListener {

        private String intentType;

        private String number;

        private String clazz;

        private Class toActivity = null;

        private List<ResMenu.Ecommerce> ecommerceList;

        public MyOnClickListener(String type, String number, String clazz, List<ResMenu.Ecommerce> ecommerces) {

            this.intentType = type;

            this.number = number;

            this.clazz = clazz;

            this.ecommerceList = ecommerces;
        }

        @Override
        public void onClick(View v) {
            try {
                if (clazz != null)
                    toActivity = Class.forName(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.putExtra("name", name);
            intent.putExtra("id", id);
            intent.putExtra("type", intentType);
            intent.putExtra("number", number);
            if (toActivity != null) {
                intent.setClass(MenuActivity.this, toActivity);
                intent.putParcelableArrayListExtra("ecommerceList", (ArrayList<? extends Parcelable>) ecommerceList);
                startActivity(intent);
            } else {
                switch (intentType) {
                    case Constants.COD_COLLECTION:
                        intent.setClass(MenuActivity.this, CodCollectionActivity.class);
                        startActivity(intent);
                        break;
                    case Constants.REST_SERVER_DATA:
                        MyApplication.getApplication().getAppComponent().getOperatorApi().resetDataForTraining().subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object objectBaseEntity) {
                                ToastUtils.showShort(MenuActivity.this, "Reset successfully");
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                ApiException apiException = ExceptionEngine.handleException(throwable);
                                ToastUtils.showShort(MenuActivity.this, apiException.message);
                            }
                        });
                        break;
                    case Constants.CREATE_RTO_DATA:
                        MyApplication.getApplication().getAppComponent().getOperatorApi().createRtoForTraining().subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object objectBaseEntity) {
                                ToastUtils.showShort(MenuActivity.this, "Create successfully");
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                ApiException apiException = ExceptionEngine.handleException(throwable);
                                ToastUtils.showShort(MenuActivity.this, apiException.message);
                            }
                        });
                        break;
                    default:
                        intent.setClass(MenuActivity.this, ScanActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        }
    }
}
