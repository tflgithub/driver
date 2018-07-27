package fodel.com.fodelscanner.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import com.freedom.yefeng.yfrecyclerview.YfListMode;
import com.freedom.yefeng.yfrecyclerview.YfListRecyclerView;
import com.freedom.yefeng.yfrecyclerview.YfLoadMoreListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.ArrayList;
import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.adapter.NotificationAdapter;
import fodel.com.fodelscanner.scanner.api.entity.response.ResNotification;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.utils.ToastUtils;
import fodel.com.fodelscanner.view.model.SpaceItemDecoration;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/4/23.
 */
public class NotificationActivity extends fodel.com.fodelscanner.scanner.ui.BaseActivity implements YfLoadMoreListener {

    @ViewInject(R.id.recycler)
    private YfListRecyclerView mList;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    private NotificationAdapter mAdapter;
    private int mCurrentPage = 1;
    private boolean mLoadingLock = false;
    private int mTotalDataCount = 0;
    private ArrayList<ResNotification.NotificationItem> mData = new ArrayList<>();

    @Override
    protected void initView() {
        ViewUtils.inject(this);
        toolbar.setTitle(getResources().getString(R.string.notification));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);  // 设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.enableAutoLoadMore(this);
        mList.setDivider(R.drawable.divider);
        mAdapter = new NotificationAdapter(mData);
        mList.setAdapter(mAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        mList.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    @Override
    protected void initData() {

        fillData(true);
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_notification;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void loadMore() {
        fillData(false);
    }


    private void fillData(final boolean isFirstLoad) {
        MyApplication.getApplication().getAppComponent().getUserApi().getNotification(String.valueOf(mCurrentPage)).subscribe(new Subscriber<ResNotification>() {

            @Override
            public void onStart() {
                mAdapter.changeMode(YfListMode.MODE_LOADING);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ApiException apiException = ExceptionEngine.handleException(e);
                ToastUtils.showShort(NotificationActivity.this, apiException.message);
            }

            @Override
            public void onNext(ResNotification notification) {
                mCurrentPage++;
                mTotalDataCount = notification.total;
                if (isFirstLoad) {
                    if (notification.list.isEmpty()) {
                        mAdapter.changeMode(YfListMode.MODE_EMPTY);
                    } else {
                        mData.addAll(notification.list);
                        mAdapter.changeMode(YfListMode.MODE_DATA);
                    }
                    return;
                }
                if (mLoadingLock) {
                    return;
                }
                if (mAdapter.getData().size() < mTotalDataCount && mAdapter.getData().size() > 0) {
                    // has more
                    mLoadingLock = true;
                    if (!mAdapter.getFooters().contains(getResources().getString(R.string.loading))) {
                        mAdapter.addFooter(getResources().getString(R.string.loading));
                    }
                    ArrayList<ResNotification.NotificationItem> moreList = new ArrayList<>();
                    for (ResNotification.NotificationItem item : notification.list) {
                        moreList.add(item);
                    }
                    mAdapter.addData(moreList);
                    mLoadingLock = false;
                } else {
                    // no more
                    if (mAdapter.getFooters().contains(getResources().getString(R.string.loading))) {
                        mAdapter.removeFooter(getResources().getString(R.string.loading));
                    }
                }
            }
        });
    }
}
