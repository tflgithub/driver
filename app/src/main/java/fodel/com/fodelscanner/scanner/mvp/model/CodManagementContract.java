package fodel.com.fodelscanner.scanner.mvp.model;

import fodel.com.fodelscanner.scanner.api.entity.response.ResInvoice;
import fodel.com.fodelscanner.scanner.base.IBaseView;
import fodel.com.fodelscanner.scanner.injector.PerActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
public interface CodManagementContract {

    interface View extends IBaseView {

        void showData(ResInvoice invoice);

        void afterDebt();
    }

    interface IPresenter {

        void getData();
    }
}
