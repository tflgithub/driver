package fodel.com.fodelscanner.scanner.mvp.model;

import java.util.List;

import fodel.com.fodelscanner.scanner.api.entity.response.ResFinalize;
import fodel.com.fodelscanner.scanner.api.entity.response.ResScanBean;
import fodel.com.fodelscanner.scanner.base.IBaseView;
import fodel.com.fodelscanner.scanner.injector.PerActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
public interface ScanContract {

    interface View extends IBaseView {

        void target(List<String> list);

        void afterVerify(ResFinalize resFinalize);

        void afterFinalize(ResFinalize resFinalize);

        void showFail(String msg, String[] content);

        void showScan(ResScanBean resScanBean);
    }

    interface IPresenter {

        void verify(List<String> awb, String type,String id);

        void finalize(List<String> awb, String type,String id);

        void getScan(String type,String id);

        void scan(String awb, String type,String id);

        void getPrinterSurfaceSingle(String id,String awb);
    }
}
