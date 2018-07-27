package fodel.com.fodelscanner.scanner.api.repository;

import android.content.Context;

import java.util.List;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.OperatorApi;
import fodel.com.fodelscanner.scanner.api.cache.bean.Bill;
import fodel.com.fodelscanner.scanner.api.cache.bean.adapter.BillAdapter;
import fodel.com.fodelscanner.scanner.api.cache.dao.BillDao;
import fodel.com.fodelscanner.scanner.api.entity.response.ResBill;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by fula on 2017/7/14.
 */

public class ScanDataRepository {

    public static final String TAG = ScanDataRepository.class.getSimpleName();
    private Context mContext;
    private OperatorApi operatorApi;
    private BillDao dao;

    public ScanDataRepository(Context context, OperatorApi operatorApi, BillDao dao) {
        this.mContext = context;
        this.operatorApi = operatorApi;
        this.dao = dao;
    }

    public interface CallBack {
        void onSuccess(Bill bill);

        void onFailure(String msg);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private CallBack callBack;

    public static ScanDataRepository singleton;

    public static ScanDataRepository with(Context context) {
        if (singleton == null) {
            synchronized (ScanDataRepository.class) {
                if (singleton == null) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }

    public static class Builder {

        private OperatorApi operatorApi;

        private BillDao dao;

        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public ScanDataRepository build() {
            if (dao == null) {
                dao = new BillDao(context);
            }
            if (operatorApi == null) {
                operatorApi = MyApplication.getApplication().getAppComponent().getOperatorApi();
            }
            return new ScanDataRepository(context, operatorApi, dao);
        }
    }

    public Observable<Bill> getBill(final String awb, final String type, final String eName, final String id) {
        Bill alreadyScanned = dao.query(awb, type, eName);
        if (alreadyScanned != null) {
            callBack.onFailure("Already Scanned");
            return Observable.just(alreadyScanned);
        } else {
            Observable<Bill> observable = operatorApi.scan(awb, type, id)
                    .map(new Func1<ResBill, Bill>() {
                        @Override
                        public Bill call(ResBill resHandover) {
                            return new BillAdapter(resHandover).get(type, eName);
                        }
                    });

            observable.subscribe(new Subscriber<Bill>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ApiException apiException = ExceptionEngine.handleException(e);
                    callBack.onFailure(apiException.message);
                }

                @Override
                public void onNext(Bill bill) {
                    if (bill != null) {
                        callBack.onSuccess(dao.insert(bill));
                    }
                }
            });
            return observable;
        }
    }

    public List<Bill> getBillList(String type, String name) {
        return dao.queryAll(type, name);
    }

    public void delete(int state, String type) {
        dao.delete(state, type);
    }

    public void deleteAll()
    {
        dao.deleteAll();
    }

    public int updateBill(String type, String name) {
        return dao.update(type, name);
    }
}
