package fodel.com.fodelscanner.scanner.api.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.cache.bean.Merchant;
import fodel.com.fodelscanner.scanner.api.cache.bean.adapter.MerchantAdapter;
import fodel.com.fodelscanner.scanner.api.cache.dao.MerchantDao;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMerchant;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.utils.NetUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by fula on 2017/7/28.
 */

public class MerchantDataRepository {


    MerchantDao merchantDao;

    Context mContext;

    public MerchantDataRepository(MerchantDao merchantDao, Context context) {
        this.merchantDao = merchantDao;
        this.mContext = context;
    }

    public void getMerchants(final DataListener<List<Merchant>> dataListener, boolean isLocal) {
        Observable<List<Merchant>> observableFromDB = Observable.create(new Observable.OnSubscribe<List<Merchant>>() {
            @Override
            public void call(Subscriber<? super List<Merchant>> subscriber) {
                List<Merchant> list = merchantDao.selectAll();
                subscriber.onNext(list);
            }
        });
        if (!NetUtils.isConnected(mContext) || isLocal) {
            observableFromDB.subscribe(new Action1<List<Merchant>>() {
                @Override
                public void call(List<Merchant> merchants) {
                    dataListener.onSuccess(merchants);
                }
            });
            return;
        }
        Observable<List<Merchant>> observableFromNet = MyApplication.getApplication().getAppComponent().getUserApi().getMerchants().map(new Func1<List<ResMerchant>, List<Merchant>>() {
            @Override
            public List<Merchant> call(List<ResMerchant> resMerchants) {
                List<Merchant> list = new ArrayList<>();
                for (ResMerchant resMerchant : resMerchants) {
                    list.add(new MerchantAdapter(resMerchant).get());
                }
                return list;
            }
        });
        observableFromNet.subscribe(new Action1<List<Merchant>>() {
            @Override
            public void call(List<Merchant> merchants) {
                merchantDao.deleteAll();
                for (Merchant merchant : merchants) {
                    merchantDao.insert(merchant);
                }
                dataListener.onSuccess(merchants);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ApiException apiException = ExceptionEngine.handleException(throwable);
                dataListener.onFailure(apiException.message);
            }
        });
    }
}
