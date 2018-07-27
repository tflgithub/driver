package fodel.com.fodelscanner.scanner.injector.module;

import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.mvp.model.HistoryContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.HistoryPresenter;

/**
 * Created by fula on 2017/7/13.
 */
@Module
public class HistoryModule {

    HistoryContract.View view;

    public HistoryModule(HistoryContract.View view) {
        this.view = view;
    }

    @Provides
    HistoryPresenter provideHistoryPresenter() {
        return new HistoryPresenter(view);
    }
}
