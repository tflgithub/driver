package fodel.com.fodelscanner.scanner.injector.module;

import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.mvp.model.EcommerceContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.EcommercePresenter;

/**
 * Created by fula on 2017/7/21.
 */
@Module
public class EcommerceModule {

    EcommerceContract.View view;

    public EcommerceModule(EcommerceContract.View view) {
        this.view = view;
    }

    @Provides
    EcommercePresenter provideEcommercePresenter() {
        return new EcommercePresenter(view);
    }
}
