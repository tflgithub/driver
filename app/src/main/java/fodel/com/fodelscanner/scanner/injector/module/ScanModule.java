package fodel.com.fodelscanner.scanner.injector.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.mvp.model.ScanContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.ScanPresenter;

/**
 * Created by fula on 2017/7/13.
 */
@Module
public class ScanModule {

    ScanContract.View view;
    Context mContext;
    public ScanModule(ScanContract.View view,Context context) {
        this.view = view;
        this.mContext=context;
    }

    @Provides
    ScanPresenter provideScanPresenter() {
        return new ScanPresenter(view);
    }
}
