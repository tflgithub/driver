package fodel.com.fodelscanner.scanner.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lidroid.xutils.ViewUtils;

import javax.inject.Inject;

import fodel.com.fodelscanner.MyAcitivityManager;
import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.injector.component.AppComponent;
import fodel.com.fodelscanner.scanner.mvp.presenter.BasePresenter;
import fodel.com.fodelscanner.view.ProcessProgressDialog;

/**
 * Created by fula on 2017/7/13.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourcesId());
        ViewUtils.inject(this);
        MyAcitivityManager.addActivity(this);
        MyApplication.getApplication().onAfterActivityCreated(this);
        initInjector();
        initView();
        initData();
    }

    protected AppComponent getApplicationComponent() {
        return ((MyApplication) getApplication()).getAppComponent();
    }

    @Inject
    protected T mPresenter;

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getResourcesId();

    protected abstract void initInjector();

    protected void initToolBar(Toolbar toolbar, String title, boolean isHomeEnable) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(isHomeEnable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isHomeEnable);
        getSupportActionBar().setDisplayShowHomeEnabled(isHomeEnable);
    }


    private Dialog dialog;

    /**
     * 处理类加载
     *
     * @param context
     * @param msg
     */
    protected synchronized void showProgressDialog(Context context, String msg) {
        if (dialog != null) {
            dialog.cancel();
        }
        dialog = new ProcessProgressDialog(context, msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 关闭dialog
     */
    protected synchronized void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void reLoginQuest() {
        initData();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.unSubscription();
        }
        MyAcitivityManager.removeActivity(this);
        super.onDestroy();
    }
}
