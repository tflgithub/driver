package fodel.com.fodelscanner.scanner.mvp.presenter;
import java.util.ArrayList;
import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.OperatorApi;
import fodel.com.fodelscanner.scanner.api.entity.response.ResInvoice;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.mvp.model.CodManagementContract;
import fodel.com.fodelscanner.utils.ToastUtils;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by fula on 2017/7/24.
 */

public class CodManagementPresenter extends BasePresenter implements CodManagementContract.IPresenter {

    CodManagementContract.View mView;

    OperatorApi operatorApi;

    public CodManagementPresenter(CodManagementContract.View view) {
        operatorApi = MyApplication.getApplication().getAppComponent().getOperatorApi();
        this.mView = view;
    }

    public void doDebt(ArrayList<String> billNos, int amount) {
        Subscription subscription = operatorApi.putToBankAccount(billNos, amount).subscribe(new Subscriber<Object>() {

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
                ToastUtils.showShort(MyApplication.getApplication().getAppComponent().getContext(), apiException.message);
            }

            @Override
            public void onNext(Object object) {
                mView.afterDebt();
            }
        });
        addSubscription(subscription);
    }


    @Override
    public void getData() {
        Subscription subscription = operatorApi.getInvoices().subscribe(new Subscriber<ResInvoice>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ApiException apiException = ExceptionEngine.handleException(e);
                ToastUtils.showShort(MyApplication.getApplication().getAppComponent().getContext(), apiException.message);
            }

            @Override
            public void onNext(ResInvoice resInvoice) {
                mView.showData(resInvoice);
            }
        });
        addSubscription(subscription);
    }
}
