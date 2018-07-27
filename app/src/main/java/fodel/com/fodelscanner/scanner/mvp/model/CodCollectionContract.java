package fodel.com.fodelscanner.scanner.mvp.model;

import java.util.List;

import fodel.com.fodelscanner.scanner.api.entity.response.ResCodCollection;
import fodel.com.fodelscanner.scanner.base.IBaseView;

/**
 * Created by fula on 2017/7/24.
 */

public interface CodCollectionContract {

    interface View extends IBaseView {

        void showData(ResCodCollection resCodCollection);

        void updateUi();

        void target(String message);

        void afterVerify();
    }

    interface IPresenter {

        void getCodCollection(String shopId);

        void addExtraInvoice(String number);

        void generate(String shopId);

        void shopReceived(List<String> awbs);

        void shopVerify(List<String> awbs);
    }
}
