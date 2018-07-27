package fodel.com.fodelscanner.scanner.mvp.presenter;
import java.util.List;
import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.OperatorApi;
import fodel.com.fodelscanner.scanner.api.entity.response.ResBill;
import fodel.com.fodelscanner.scanner.api.entity.response.ResFinalize;
import fodel.com.fodelscanner.scanner.api.entity.response.ResScanBean;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.mvp.model.ScanContract;
import fodel.com.fodelscanner.utils.LogUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by fula on 2017/7/13.
 */

public class ScanPresenter extends BasePresenter implements ScanContract.IPresenter {

    public static final String TAG = ScanPresenter.class.getSimpleName();

    private final ScanContract.View mView;

    OperatorApi operatorApi;

    public ScanPresenter(ScanContract.View mView) {
        this.mView = mView;
        operatorApi = MyApplication.getApplication().getAppComponent().getOperatorApi();
    }

    @Override
    public void getScan(String type,String id) {
        Subscription subscription = operatorApi.getScan(type,id).subscribe(new Subscriber<ResScanBean>() {
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
            public void onNext(ResScanBean resScanBean) {
                mView.showScan(resScanBean);
            }

            @Override
            public void onStart() {
                mView.showLoading();
            }
        });
        addSubscription(subscription);
    }


    @Override
    public void scan(String awb, String type,String id) {
        Subscription subscription = operatorApi.scan(awb, type, id).subscribe(new Action1<ResBill>() {
            @Override
            public void call(ResBill resBill) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LogUtils.saveInfoToSD(throwable);
                ApiException apiException = ExceptionEngine.handleException(throwable);
                mView.showError(apiException.message);
            }
        });
        addSubscription(subscription);
    }

    @Override
    public void getPrinterSurfaceSingle(String id, String awb) {

    }


    @Override
    public void verify(List<String> awbs, String type,String id) {
        Subscription subscription = operatorApi.verify(awbs, type,id).subscribe(new Subscriber<ResFinalize>() {
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
            public void onNext(ResFinalize resFinalize) {
                mView.afterVerify(resFinalize);
            }
        });
        addSubscription(subscription);
    }

    @Override
    public void finalize(List<String> awb, String type,String id) {
        Subscription subscription = operatorApi.finalize(awb, type,id).subscribe(new Subscriber<ResFinalize>() {
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
            public void onNext(ResFinalize resFinalize) {
                mView.afterFinalize(resFinalize);
            }
        });
        addSubscription(subscription);
    }
}
