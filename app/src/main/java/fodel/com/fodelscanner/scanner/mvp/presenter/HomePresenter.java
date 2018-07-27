package fodel.com.fodelscanner.scanner.mvp.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.cache.bean.Merchant;
import fodel.com.fodelscanner.scanner.api.cache.bean.adapter.MerchantAdapter;
import fodel.com.fodelscanner.scanner.api.cache.dao.MerchantDao;
import fodel.com.fodelscanner.scanner.api.entity.bean.HomeBean;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMerchant;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.mvp.model.HomeContract;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by fula on 2017/7/21.
 */

public class HomePresenter extends BasePresenter implements HomeContract.IPresenter {

    HomeContract.View mView;

    Context mContext;

    MerchantDao merchantDao;

    public HomePresenter(HomeContract.View mView, Context context) {
        this.mView = mView;
        this.mContext = context;
        merchantDao = new MerchantDao(mContext);
    }

    @Override
    public void getData() {

        // get menu
        Observable<List<ResMenu>> observableMenu = MyApplication.getApplication().getAppComponent().getUserApi().getMenu();

        //get merchants
        Observable<List<ResMerchant>> observableMerchant = MyApplication.getApplication().getAppComponent().getUserApi().getMerchants();

        Observable.zip(observableMenu, observableMerchant, new Func2<List<ResMenu>, List<ResMerchant>, HomeBean>() {
            @Override
            public HomeBean call(List<ResMenu> resMenus, List<ResMerchant> resMerchants) {
                mView.showMenu(resMenus);
                return new HomeBean(resMerchants, resMenus);
            }
        }).map(new Func1<HomeBean, List<Merchant>>() {
            @Override
            public List<Merchant> call(HomeBean homeBean) {
                List<Merchant> list = new ArrayList<>();
                for (ResMerchant resMerchant : homeBean.merchantList) {
                    list.add(new MerchantAdapter(resMerchant).get());
                }
                return list;
            }
        }).subscribe(new Action1<List<Merchant>>() {
            @Override
            public void call(List<Merchant> merchants) {
                merchantDao.deleteAll();
                for (Merchant merchant : merchants) {
                    merchantDao.insert(merchant);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ApiException apiException = ExceptionEngine.handleException(throwable);
                mView.showError(apiException.message);
            }
        });
    }
}
