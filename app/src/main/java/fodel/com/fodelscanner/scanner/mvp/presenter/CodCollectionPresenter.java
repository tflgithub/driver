package fodel.com.fodelscanner.scanner.mvp.presenter;

import java.util.List;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.entity.response.ResCodCollection;
import fodel.com.fodelscanner.scanner.api.entity.response.ResReceived;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.mvp.model.CodCollectionContract;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by fula on 2017/7/24.
 */

public class CodCollectionPresenter extends BasePresenter implements CodCollectionContract.IPresenter {

    CodCollectionContract.View mView;

    public CodCollectionPresenter(CodCollectionContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getCodCollection(String shopId) {
        Subscription subscription = MyApplication.getApplication().getAppComponent().getUserApi().getCollected(shopId).subscribe(new Subscriber<ResCodCollection>() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onCompleted() {
                mView.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismiss();
                ApiException apiException = ExceptionEngine.handleException(e);
                mView.showError(apiException.message);
            }

            @Override
            public void onNext(ResCodCollection resCodCollections) {
                mView.showData(resCodCollections);
            }
        });
        addSubscription(subscription);
    }

    @Override
    public void generate(String shopId) {
        Subscription subscription = MyApplication.getApplication().getAppComponent().getUserApi().generate(shopId).subscribe(new Subscriber<ResCodCollection>() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onCompleted() {
                mView.dismiss();
                mView.updateUi();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismiss();
                ApiException apiException = ExceptionEngine.handleException(e);
                mView.showError(apiException.message);
            }

            @Override
            public void onNext(ResCodCollection resCodCollections) {
            }
        });
        addSubscription(subscription);
    }

    @Override
    public void addExtraInvoice(final String number) {
        Subscription subscription = MyApplication.getApplication().getAppComponent().getUserApi().addExtraInvoice(number).subscribe(new Subscriber<Object>() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onCompleted() {
                mView.dismiss();
                mView.updateUi();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismiss();
                ApiException apiException = ExceptionEngine.handleException(e);
                mView.showError(apiException.message);
            }

            @Override
            public void onNext(Object o) {
            }
        });
        addSubscription(subscription);
    }


    @Override
    public void shopVerify(List<String> awbs) {
        Subscription subscription = MyApplication.getApplication().getAppComponent().getUserApi().verify(awbs).subscribe(new Subscriber<Object>() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onCompleted() {
                mView.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismiss();
                ApiException apiException = ExceptionEngine.handleException(e);
                mView.showError(apiException.message);
            }

            @Override
            public void onNext(Object o) {
                mView.afterVerify();
            }
        });
        addSubscription(subscription);
    }


    @Override
    public void shopReceived(List<String> awbs) {
        Subscription subscription = MyApplication.getApplication().getAppComponent().getUserApi().received(awbs).subscribe(new Subscriber<ResReceived>() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onCompleted() {
                mView.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismiss();
                ApiException apiException = ExceptionEngine.handleException(e);
                mView.showError(apiException.message);
            }

            @Override
            public void onNext(ResReceived resReceived) {
                mView.target(resReceived.message);
            }
        });
        addSubscription(subscription);
    }
}
