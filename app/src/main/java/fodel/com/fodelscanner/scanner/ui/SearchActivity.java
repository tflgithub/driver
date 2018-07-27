package fodel.com.fodelscanner.scanner.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.cn.xtouch.tfl.alert.AlertView;
import com.lidroid.xutils.view.annotation.ViewInject;

import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResSearchResult;
import fodel.com.fodelscanner.scanner.injector.component.DaggerSearchComponent;
import fodel.com.fodelscanner.scanner.injector.module.SearchModule;
import fodel.com.fodelscanner.scanner.mvp.model.SearchContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.SearchPresenter;
import fodel.com.fodelscanner.scanner.ui.fragment.SearchFragment;
import fodel.com.fodelscanner.scanner.ui.fragment.SearchResultFragment;
import fodel.com.fodelscanner.service.PrinterService;
import fodel.com.fodelscanner.utils.ToastUtils;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void initView() {
        initToolBar(toolbar, getResources().getString(R.string.menu_search), true);
    }

    @Override
    protected void initData() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_container, SearchFragment.newInstance());
        transaction.commit();
        //bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            new AlertView("Your device does not support bluetooth,cannot connect to printer.", null, null, null, new String[]{"Ok"}, null, this, AlertView.Style.Alert, null).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, Constants.ENABLE_BLUETOOTH_REQUEST);
        } else {
            startService(new Intent(this, PrinterService.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.ENABLE_BLUETOOTH_REQUEST:
                    startService(new Intent(this, PrinterService.class));
                    break;
            }
        }
    }

    public void search(String awb) {
        mPresenter.search(awb);
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_search_activity;
    }

    @Override
    protected void initInjector() {
        DaggerSearchComponent.builder().appComponent(getApplicationComponent()).searchModule(new SearchModule(this)).build().inject(this);
    }

    @Override
    public void showLoading() {
        showProgressDialog(this, " Loading");
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
    public void showData(ResSearchResult resSearchResult) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_container, SearchResultFragment.newInstance(resSearchResult));
        transaction.commit();
    }
}
