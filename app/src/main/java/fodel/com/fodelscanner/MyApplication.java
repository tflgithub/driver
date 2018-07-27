package fodel.com.fodelscanner;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.lidroid.xutils.util.LogUtils;
import com.tfl.filedownloader.DownLoadService;

import fodel.com.fodelscanner.scanner.injector.component.AppComponent;
import fodel.com.fodelscanner.scanner.injector.component.DaggerAppComponent;
import fodel.com.fodelscanner.scanner.injector.module.ApiModule;
import fodel.com.fodelscanner.scanner.injector.module.AppModule;
import fodel.com.fodelscanner.scanner.ui.BaseActivity;
import fodel.com.fodelscanner.service.LocationService;
import fodel.com.fodelscanner.service.RemoteService;

/**
 * Created by fula on 2016/4/5.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication application;
    public BaseActivity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initializeInjector();
        startService(new Intent(this, DownLoadService.class));
        if (!BuildConfig.DEBUG) {
            CrashExceptionHandler.getInstance(this).init(this);
        }
        LogUtils.customTagPrefix = "driver";
        startService(new Intent(this, LocationService.class));
        startService(new Intent(this, RemoteService.class));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private AppComponent appComponent;

    private void initializeInjector() {
        this.appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .build();
    }

    public static MyApplication getApplication() {
        return application;
    }

    public void onAfterActivityCreated(BaseActivity activity) {
        this.currentActivity = activity;
    }
}
