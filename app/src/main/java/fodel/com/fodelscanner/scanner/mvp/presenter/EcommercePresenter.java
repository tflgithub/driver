package fodel.com.fodelscanner.scanner.mvp.presenter;
import fodel.com.fodelscanner.scanner.mvp.model.EcommerceContract;

/**
 * Created by fula on 2017/7/21.
 */
public class EcommercePresenter extends BasePresenter implements EcommerceContract.IPresenter {

    public EcommercePresenter(EcommerceContract.View mView) {
        this.mView = mView;
    }

    EcommerceContract.View mView;

    @Override
    public void getEcommerce(String type) {
//        Subscription subscription = MyApplication.getApplication().getAppComponent().getUserApi().getEcommerces(type).subscribe(new Subscriber<List<ResEcommerce>>() {
//            @Override
//            public void onStart() {
//                mView.showLoading();
//            }
//
//            @Override
//            public void onCompleted() {
//                mView.dismiss();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                ApiException exception = ExceptionEngine.handleException(e);
//                mView.showError(exception.message);
//            }
//
//            @Override
//            public void onNext(List<ResEcommerce> resEcommerces) {
//                mView.showEcommerce(resEcommerces);
//            }
//        });
//        addSubscription(subscription);
    }
}
