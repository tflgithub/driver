package fodel.com.fodelscanner.scanner.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.cache.bean.Merchant;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;
import fodel.com.fodelscanner.scanner.injector.component.DaggerMerchantComponent;
import fodel.com.fodelscanner.scanner.injector.module.MerchantModule;
import fodel.com.fodelscanner.scanner.mvp.model.MerchantContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.MerchantPresenter;
import fodel.com.fodelscanner.scanner.ui.adapter.MerchantActionsAdapter;
import fodel.com.fodelscanner.utils.ToastUtils;

public class MerchantActionsActivity extends BaseActivity<MerchantPresenter> implements MerchantContract.View {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.listView)
    ListView mListView;
    private MerchantActionsAdapter merchantActionsAdapter;
    private List<Merchant> list = new ArrayList<>();

    @Override
    protected void initView() {
        initToolBar(toolbar, getIntent().getStringExtra("menu_title"), true);
        merchantActionsAdapter = new MerchantActionsAdapter(this, list);
        mListView.setAdapter(merchantActionsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                merchantActionsAdapter.changeSelected(position);
                Intent intent = new Intent(MerchantActionsActivity.this, MenuActivity.class);
                List<ResMenu.SubMenu> menus = new ArrayList<>();
                String[] label = list.get(position).label.split(",");
                String[] type = list.get(position).type.split(",");
                for (int i = 0; i < label.length; i++) {
                    ResMenu.SubMenu menuBean = new ResMenu.SubMenu();
                    String[] nameStr = label[i].split(":");
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(nameStr[0]).append("(").append(nameStr[1]).append(")");
                    menuBean.name = stringBuffer.toString();
                    menuBean.type = type[i];
                    menus.add(menuBean);
                }
                intent.putExtra("menu_title", getIntent().getStringExtra("menu_title"));
                intent.putExtra("name", list.get(position).station_name);
                intent.putExtra("id", list.get(position).station_id);
                intent.putExtra("address", list.get(position).address);
                intent.putParcelableArrayListExtra("sub_menu_list", (ArrayList<? extends Parcelable>) menus);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getMerchant();
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_merchant;
    }

    @Override
    protected void initInjector() {
        DaggerMerchantComponent.builder().appComponent(getApplicationComponent()).merchantModule(new MerchantModule(this, this)).build().inject(this);
    }

    @Override
    public void showMerchants(Object list) {
        this.list = (List<Merchant>) list;
        merchantActionsAdapter.setData(this.list);
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
}
