package fodel.com.fodelscanner.scanner.injector.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.mvp.model.HomeContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.HomePresenter;

/**
 * Created by fula on 2017/7/21.
 */
@Module
public class HomeModule {

    HomeContract.View view;
    Context mContext;


    public HomeModule(HomeContract.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    @Provides
    HomePresenter provideHomePresenter() {
        return new HomePresenter(view, mContext);
    }
}
