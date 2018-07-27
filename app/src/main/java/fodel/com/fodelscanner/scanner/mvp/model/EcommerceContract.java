package fodel.com.fodelscanner.scanner.mvp.model;

import java.util.List;

import fodel.com.fodelscanner.scanner.base.IBaseView;
import fodel.com.fodelscanner.scanner.api.entity.response.ResEcommerce;
import fodel.com.fodelscanner.scanner.injector.PerActivity;

/**
 * Created by fula on 2017/7/21.
 */
@PerActivity
public interface EcommerceContract {

    interface View extends IBaseView {
        void showEcommerce(List<ResEcommerce> list);
    }

    interface IPresenter {

        void getEcommerce(String type);
    }
}
