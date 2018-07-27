package fodel.com.fodelscanner.scanner.injector.module;
import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.mvp.model.CodManagementContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.CodManagementPresenter;

/**
 * Created by fula on 2017/7/13.
 */
@Module
public class CodManagementModule {

    CodManagementContract.View view;

    public CodManagementModule(CodManagementContract.View view) {
        this.view = view;
    }

    @Provides
    CodManagementPresenter provideCodManagementPresenter() {
        return new CodManagementPresenter(view);
    }
}
