package fodel.com.fodelscanner.scanner.injector.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.mvp.model.FinalizeContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.FinalizePresenter;

/**
 * Created by fula on 2017/7/13.
 */
@Module
public class FinalizeModule {

    FinalizeContract.View view;
    Context mContext;

    public FinalizeModule(FinalizeContract.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    @Provides
    FinalizePresenter provideFinalizePresenter() {
        return new FinalizePresenter(view, mContext);
    }
}
