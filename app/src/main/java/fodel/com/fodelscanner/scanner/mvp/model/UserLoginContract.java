package fodel.com.fodelscanner.scanner.mvp.model;

import fodel.com.fodelscanner.scanner.base.IBaseView;
import fodel.com.fodelscanner.scanner.injector.PerActivity;

/**
 * Created by fula on 2017/7/21.
 */
@PerActivity
public interface UserLoginContract {

    interface View extends IBaseView {

        void target();
    }

    interface IPresenter {


    }
}
