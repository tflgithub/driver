package fodel.com.fodelscanner.scanner.mvp.presenter;

import android.content.Context;

import java.util.List;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.cache.bean.Merchant;
import fodel.com.fodelscanner.scanner.api.cache.dao.MerchantDao;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMerchant;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.api.repository.DataListener;
import fodel.com.fodelscanner.scanner.api.repository.MerchantDataRepository;
import fodel.com.fodelscanner.scanner.mvp.model.MerchantContract;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by fula on 2017/7/21.
 */

public class MerchantPresenter extends BasePresenter implements MerchantContract.IPresenter {

    MerchantContract.View mView;

    Context mContext;

    MerchantDao merchantDao;

    MerchantDataRepository merchantDataRepository;

    public MerchantPresenter(MerchantContract.View mView, Context context) {
        this.mView = mView;
        this.mContext = context;
        merchantDao = new MerchantDao(mContext);
        merchantDataRepository = new MerchantDataRepository(merchantDao, mContext);
    }

    @Override
    public void getMerchant() {
        merchantDataRepository.getMerchants(new DataListener<List<Merchant>>() {
            @Override
            public void onSuccess(List<Merchant> data) {
                mView.showMerchants(data);
            }

            @Override
            public void onFailure(String msg) {
                mView.showError(msg);
            }
        }, true);
    }

    @Override
    public void getMoreMerchants(String name, String page) {
        Subscription subscription = MyApplication.getApplication().getAppComponent().getUserApi().getMoreMerchant(name, page).subscribe(new Subscriber<List<ResMerchant>>() {
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
            public void onNext(List<ResMerchant> resMerchant) {
                mView.showMerchants(resMerchant);
            }
        });
        addSubscription(subscription);
    }
}
