package fodel.com.fodelscanner.scanner.mvp.model;

import fodel.com.fodelscanner.scanner.base.IBaseView;
import fodel.com.fodelscanner.scanner.injector.PerActivity;

/**
 * Created by fula on 2017/7/21.
 */
@PerActivity
public interface MerchantContract {

    interface View extends IBaseView {

        void showMerchants(Object list);
    }

    interface IPresenter {

        void getMerchant();

        void getMoreMerchants(String name,String page);
    }
}
