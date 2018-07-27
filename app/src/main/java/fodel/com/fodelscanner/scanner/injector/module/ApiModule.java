package fodel.com.fodelscanner.scanner.injector.module;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.api.OperatorApi;
import fodel.com.fodelscanner.scanner.api.UploadApi;
import fodel.com.fodelscanner.scanner.api.UserApi;
import fodel.com.fodelscanner.scanner.api.support.HeaderInterceptor;
import fodel.com.fodelscanner.scanner.api.support.HttpRequestInterceptor;
import fodel.com.fodelscanner.scanner.api.support.Logger;
import fodel.com.fodelscanner.scanner.api.support.LoggingInterceptor;
import okhttp3.OkHttpClient;

/**
 * Created by fula on 2017/7/13.
 */
@Module
public class ApiModule {

    @Provides
    public OkHttpClient provideOkHttpClient() {
        LoggingInterceptor logging = new LoggingInterceptor(new Logger());
        logging.setLevel(LoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new HttpRequestInterceptor())
                .addInterceptor(logging);
        return builder.build();
    }

    @Provides
    protected OperatorApi provideOperatorApi(OkHttpClient okHttpClient) {
        return new OperatorApi(okHttpClient);
    }

    @Provides
    protected UserApi provideUserApi(OkHttpClient okHttpClient) {
        return new UserApi(okHttpClient);
    }

    @Provides
    protected UploadApi provideUploadApi(OkHttpClient okHttpClient) {
        return new UploadApi(okHttpClient);
    }
}
