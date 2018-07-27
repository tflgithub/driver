package fodel.com.fodelscanner.scanner.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;
import fodel.com.fodelscanner.scanner.injector.component.DaggerHistoryComponent;
import fodel.com.fodelscanner.scanner.injector.module.HistoryModule;
import fodel.com.fodelscanner.scanner.mvp.model.HistoryContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.HistoryPresenter;
import fodel.com.fodelscanner.scanner.ui.fragment.BackHandledFragment;
import fodel.com.fodelscanner.scanner.ui.fragment.HistoryFragment;
import fodel.com.fodelscanner.utils.ToastUtils;

/**
 * Created by fula on 2016/4/22.
 */
public class HistoryActivity extends BaseActivity<HistoryPresenter> implements HistoryContract.View, BackHandledFragment.BackHandledInterface {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    BackHandledFragment mBackHandedFragment;

    @Override
    protected void initView() {
        ViewUtils.inject(this);
        toolbar.setTitle(getResources().getString(R.string.history));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);  // 设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void initData() {
        mBackHandedFragment = new HistoryFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_container, mBackHandedFragment, "default");
        ft.addToBackStack("tag");
        ft.commit();
    }

    public void getData(String startDate, String endDate, String type) {
        mPresenter.getData(startDate, endDate, type);
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_history;
    }

    @Override
    protected void initInjector() {
        DaggerHistoryComponent.builder().appComponent(getApplicationComponent()).historyModule(new HistoryModule(this)).build().inject(this);
    }

    @Override
    public void showData(ResHistory history) {
        ((HistoryFragment) mBackHandedFragment).setData(history);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        if (mBackHandedFragment != null) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    finish();
                }
                getSupportFragmentManager().popBackStack();
            }
        }
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
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }
}
