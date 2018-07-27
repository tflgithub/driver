package fodel.com.fodelscanner.scanner.mvp.presenter;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.OperatorApi;
import fodel.com.fodelscanner.scanner.api.entity.response.ResSearchResult;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.mvp.model.SearchContract;
import fodel.com.fodelscanner.utils.LogUtils;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by fula on 2017/7/24.
 */

public class SearchPresenter extends BasePresenter implements SearchContract.IPresenter {

    SearchContract.View mView;
    OperatorApi operatorApi;

    public SearchPresenter(SearchContract.View view) {
        this.mView = view;
        operatorApi = MyApplication.getApplication().getAppComponent().getOperatorApi();
    }

    @Override
    public void search(String awb) {
        Subscription subscription = operatorApi.search(awb).subscribe(new Subscriber<ResSearchResult>() {
            @Override
            public void onCompleted() {
                mView.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismiss();
                LogUtils.saveInfoToSD(e);
                ApiException apiException = ExceptionEngine.handleException(e);
                mView.showError(apiException.message);
            }

            @Override
            public void onNext(ResSearchResult resSearchResult) {
                mView.showData(resSearchResult);
            }

            @Override
            public void onStart() {
                mView.showLoading();
            }
        });
        addSubscription(subscription);
    }
}
