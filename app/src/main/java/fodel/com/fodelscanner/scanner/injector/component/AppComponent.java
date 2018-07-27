package fodel.com.fodelscanner.scanner.injector.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import fodel.com.fodelscanner.scanner.api.OperatorApi;
import fodel.com.fodelscanner.scanner.api.UploadApi;
import fodel.com.fodelscanner.scanner.api.UserApi;
import fodel.com.fodelscanner.scanner.injector.module.ApiModule;
import fodel.com.fodelscanner.scanner.injector.module.AppModule;

/**
 * Created by fula on 2017/7/13.
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    Context getContext();

    OperatorApi getOperatorApi();

    UploadApi getUploadApi();

    UserApi getUserApi();
}
