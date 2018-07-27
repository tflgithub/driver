package fodel.com.fodelscanner.scanner.injector.module;

import android.content.Context;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.MyApplication;

/**
 * Created by fula on 2017/7/13.
 */
@Module
public class AppModule {

    private final MyApplication application;

    public AppModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }
}
