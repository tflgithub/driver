package fodel.com.fodelscanner.scanner.mvp.model;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;
import fodel.com.fodelscanner.scanner.base.IBaseView;
import fodel.com.fodelscanner.scanner.injector.PerActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
public interface HistoryContract {

    interface View extends IBaseView {

        void showData(ResHistory history);
    }

    interface IPresenter {

        void getData(String startDate, String endDate, String type);
    }
}
