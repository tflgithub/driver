package fodel.com.fodelscanner.scanner.injector.module;

import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.mvp.model.SearchContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.SearchPresenter;

/**
 * Created by fula on 2017/7/13.
 */
@Module
public class SearchModule {

    SearchContract.View view;

    public SearchModule(SearchContract.View view) {
        this.view = view;
    }

    @Provides
    SearchPresenter provideReturnPresenter() {
        return new SearchPresenter(view);
    }
}
