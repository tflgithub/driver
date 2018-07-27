package fodel.com.fodelscanner.scanner.api.support;

import java.io.IOException;

import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.utils.SPUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fula on 2017/7/13.
 */

public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .addHeader("Authorization", "Bearer " + SPUtils.get(MyApplication.getApplication().getApplicationContext(), Constants.TOKEN, ""))
                .build();
        return chain.proceed(request);
    }
}
