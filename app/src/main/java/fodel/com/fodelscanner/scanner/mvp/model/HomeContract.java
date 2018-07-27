package fodel.com.fodelscanner.scanner.mvp.model;

import java.util.List;

import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;
import fodel.com.fodelscanner.scanner.base.IBaseView;
import fodel.com.fodelscanner.scanner.injector.PerActivity;

/**
 * Created by fula on 2017/7/21.
 */
@PerActivity
public interface HomeContract {

    interface View extends IBaseView {

        void showMenu(List<ResMenu> list);

    }

    interface IPresenter {

        void getData();
    }
}
