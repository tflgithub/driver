package fodel.com.fodelscanner.scanner.injector.module;

import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.mvp.model.CodCollectionContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.CodCollectionPresenter;

/**
 * Created by fula on 2017/7/21.
 */
@Module
public class CodCollectionModule {

    CodCollectionContract.View view;

    public CodCollectionModule(CodCollectionContract.View view) {
        this.view = view;
    }

    @Provides
    CodCollectionPresenter provideCodCollectionPresenter() {
        return new CodCollectionPresenter(view);
    }
}
