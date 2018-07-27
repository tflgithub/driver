package fodel.com.fodelscanner.scanner.injector.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.mvp.model.MerchantContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.MerchantPresenter;

/**
 * Created by fula on 2017/7/21.
 */
@Module
public class MerchantModule {

    MerchantContract.View view;
    Context mContext;

    public MerchantModule(MerchantContract.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    @Provides
    MerchantPresenter provideMerchantPresenter() {
        return new MerchantPresenter(view, mContext);
    }
}
