package fodel.com.fodelscanner.scanner.ui;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResEcommerce;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;
import fodel.com.fodelscanner.scanner.injector.component.DaggerEcommerceComponent;
import fodel.com.fodelscanner.scanner.injector.module.EcommerceModule;
import fodel.com.fodelscanner.scanner.mvp.model.EcommerceContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.EcommercePresenter;
import fodel.com.fodelscanner.utils.StringUtils;
import fodel.com.fodelscanner.utils.ToastUtils;

public class EcommerceActivity extends BaseActivity<EcommercePresenter> implements EcommerceContract.View {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    private List<String> dataList;
    private List<String> idList;
    private String type;
    @ViewInject(R.id.listView)
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void initView() {
        type = getIntent().getStringExtra("type");
        String title = null;
        switch (type) {
            case Constants.PICKUP_AT_WAREHOUSE:
                title = getString(R.string.ecommerce_pickup);
                break;
            case Constants.DROP_RETURN_AT_WAREHOUSE:
                title = getString(R.string.ecommerce_checkin);
                break;
        }
        initToolBar(toolbar, title, true);
    }

    @Override
    protected void initData() {
        List<ResMenu.Ecommerce> ecommerces = getIntent().getParcelableArrayListExtra("ecommerceList");
        if (!ecommerces.isEmpty()) {
            dataList = new ArrayList<>();
            idList = new ArrayList<>();
            for (ResMenu.Ecommerce bean : ecommerces) {
                idList.add(bean.id);
                dataList.add(bean.name + "(" + bean.amount + ")");
            }
            arrayAdapter = new ArrayAdapter<>(this, R.layout.text_item, dataList);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String ecommerceId = idList.get(position);
                    String ecommerceName = dataList.get(position);
                    String number = StringUtils.interceptParenthesis(ecommerceName);
                    //截取电商名称
                    ecommerceName = ecommerceName.replaceAll("\\(.*\\)", "");
                    Intent intent = new Intent(EcommerceActivity.this, ScanActivity.class);
                    intent.putExtra("name", ecommerceName);
                    intent.putExtra("id", ecommerceId);
                    intent.putExtra("type", type);
                    intent.putExtra("number", number);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_ecommerce;
    }

    @Override
    protected void initInjector() {
        DaggerEcommerceComponent.builder().appComponent(getApplicationComponent()).ecommerceModule(new EcommerceModule(this)).build().inject(this);
    }

    @Override
    public void showLoading() {
        showProgressDialog(this, getString(R.string.loading));
    }

    @Override
    public void dismiss() {
        dismissProgressDialog();
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void showEcommerce(List<ResEcommerce> list) {

    }
}
