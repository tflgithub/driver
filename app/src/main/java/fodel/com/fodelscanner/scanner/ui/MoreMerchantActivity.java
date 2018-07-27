package fodel.com.fodelscanner.scanner.ui;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMerchant;
import fodel.com.fodelscanner.scanner.injector.component.DaggerMerchantComponent;
import fodel.com.fodelscanner.scanner.injector.module.MerchantModule;
import fodel.com.fodelscanner.scanner.mvp.model.MerchantContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.MerchantPresenter;
import fodel.com.fodelscanner.scanner.ui.adapter.MoreMerchantAdapter;
import fodel.com.fodelscanner.utils.ToastUtils;
import fodel.com.fodelscanner.view.LoadMoreListView;

public class MoreMerchantActivity extends BaseActivity<MerchantPresenter> implements MerchantContract.View, LoadMoreListView.IloadListener {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.listView)
    LoadMoreListView mListView;
    @ViewInject(R.id.emptyElement)
    private TextView emptyElement;
    private MoreMerchantAdapter moreMerchantAdapter;
    private List<ResMerchant> list = new ArrayList<>();
    private int currentPage = 1;
    private String name = "";

    @Override
    protected void initView() {
        initToolBar(toolbar, getIntent().getStringExtra("menu_title"), true);
        moreMerchantAdapter = new MoreMerchantAdapter(this, list);
        mListView.setAdapter(moreMerchantAdapter);
        mListView.setIloadListener(this);
    }

    @Override
    protected void initData() {
        mPresenter.getMoreMerchants(name, String.valueOf(currentPage));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.ab_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                name = "";
                currentPage = 1;
                initData();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                name = query;
                currentPage = 1;
                initData();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }


    @Override
    protected int getResourcesId() {
        return R.layout.activity_more_merchant;
    }

    @Override
    protected void initInjector() {
        DaggerMerchantComponent.builder().appComponent(getApplicationComponent()).merchantModule(new MerchantModule(this, this)).build().inject(this);
    }

    @Override
    public void showLoading() {
        showProgressDialog(this, getString(R.string.loading));
    }

    @Override
    public void dismiss() {
        dismissProgressDialog();
        mListView.loadComplete();
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void showMerchants(Object list) {
        List<ResMerchant> newList = (List<ResMerchant>) list;
        if (newList.isEmpty()) {
            if (currentPage > 1) {
                mListView.setLast(true);
                showError("No more data");
            } else {
                emptyElement.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            }
            return;
        } else {
            mListView.setVisibility(View.VISIBLE);
            emptyElement.setVisibility(View.GONE);
            mListView.setLast(false);
            if (currentPage > 1) {
                this.list.addAll(newList);
            } else {
                this.list = newList;
            }
            moreMerchantAdapter.setData(this.list);
        }
    }

    @Override
    public void onLoad() {
        currentPage++;
        initData();
    }
}
