package fodel.com.fodelscanner.scanner.mvp.presenter;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.mvp.model.HistoryContract;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by fula on 2017/7/24.
 */

public class HistoryPresenter extends BasePresenter implements HistoryContract.IPresenter {

    HistoryContract.View mView;

    public HistoryPresenter(HistoryContract.View view) {
        this.mView = view;
    }

    @Override
    public void getData(String startDate, String endDate, String type) {
        Subscription subscription = MyApplication.getApplication().getAppComponent().getUserApi().getHistory(startDate, endDate, type).subscribe(new Subscriber<ResHistory>() {
            @Override
            public void onCompleted() {
                mView.dismiss();
            }
            @Override
            public void onError(Throwable e) {
                ApiException apiException = ExceptionEngine.handleException(e);
                mView.showError( apiException.message);
                mView.dismiss();
            }
            @Override
            public void onNext(ResHistory resHistories) {
                mView.showData(resHistories);
            }

            @Override
            public void onStart() {
                mView.showLoading();
            }
        });
        addSubscription(subscription);
    }
}
