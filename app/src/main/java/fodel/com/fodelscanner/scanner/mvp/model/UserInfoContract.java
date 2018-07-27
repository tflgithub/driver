package fodel.com.fodelscanner.scanner.mvp.model;

import fodel.com.fodelscanner.scanner.api.cache.bean.User;
import fodel.com.fodelscanner.scanner.base.IBaseView;
import fodel.com.fodelscanner.scanner.injector.PerActivity;

/**
 * Created by fula on 2017/7/21.
 */
@PerActivity
public interface UserInfoContract {

    interface View extends IBaseView {

        void showUser(User user);

        void loadPortrait(String url);
    }

    interface IPresenter {

        void getUser();

        void changePortrait(String path);

        void logOff();
    }
}
