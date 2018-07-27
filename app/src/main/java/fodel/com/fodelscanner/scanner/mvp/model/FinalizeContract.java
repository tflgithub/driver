package fodel.com.fodelscanner.scanner.mvp.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.scanner.api.entity.response.ResFinalize;
import fodel.com.fodelscanner.scanner.base.IBaseView;
import fodel.com.fodelscanner.scanner.injector.PerActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
public interface FinalizeContract {

    interface View extends IBaseView {

        void target();

        void showAlert(List<ResFinalize.Awb> awbList);
    }

    interface IPresenter {

        void submit(ArrayList<String> list, String type, File file,String id);
    }
}
