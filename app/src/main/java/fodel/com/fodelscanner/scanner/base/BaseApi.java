package fodel.com.fodelscanner.scanner.base;

import android.os.SystemClock;
import android.util.ArrayMap;

import com.alibaba.fastjson.JSONObject;
import com.dou361.retrofit2.converter.fastjson.FastJsonConverterFactory;

import java.util.Map;

import fodel.com.fodelscanner.scanner.api.entity.response.ServerResponseFunc;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionFunc;
import fodel.com.fodelscanner.scanner.api.exception.RetryWhenNetworkException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fula on 2017/7/20.
 */

public class BaseApi {

    protected Map<String, Object> paramMap = new ArrayMap<>();

    public Map<String, Object> getParamMap() {
        paramMap.put("driver_id", "");
        paramMap.put("ts", SystemClock.currentThreadTimeMillis());
        return paramMap;
    }
    protected Retrofit retrofit;

    public BaseApi(OkHttpClient okHttpClient, String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(FastJsonConverterFactory.create()) // 添加fastJson转换器
                .client(okHttpClient)
                .build();
    }

    protected Observable doHttp(Observable observable) {
        observable = observable.retryWhen(new RetryWhenNetworkException())
                /*异常处理*/
                .onErrorResumeNext(new ExceptionFunc())
                .map(new ServerResponseFunc())
               /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
               /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    protected RequestBody parseJsonBody(String key, Object object) {
        paramMap.put(key, object);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new JSONObject(getParamMap()).toString());
    }

    protected RequestBody parseJsonBody(String k1, Object v1, String k2, Object v2) {
        paramMap.put(k1, v1);
        paramMap.put(k2, v2);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new JSONObject(getParamMap()).toString());
    }
}
