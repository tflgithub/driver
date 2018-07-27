package fodel.com.fodelscanner.scanner.mvp.model;

import fodel.com.fodelscanner.scanner.api.entity.response.ResSearchResult;
import fodel.com.fodelscanner.scanner.base.IBaseView;
import fodel.com.fodelscanner.scanner.injector.PerActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
public interface SearchContract {

    interface View extends IBaseView {

        void showData(ResSearchResult resSearchResult);
    }

    interface IPresenter {

        void search(String awb);
    }
}
