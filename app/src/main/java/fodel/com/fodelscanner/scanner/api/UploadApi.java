package fodel.com.fodelscanner.scanner.api;

import android.util.Log;

import java.io.File;

import fodel.com.fodelscanner.BuildConfig;
import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.base.BaseApi;
import fodel.com.fodelscanner.utils.ToastUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by fula on 2017/7/13.
 */

public class UploadApi extends BaseApi {

    UploadService uploadService;

    public UploadApi(OkHttpClient okHttpClient) {
        super(okHttpClient, BuildConfig.API_URL);
        uploadService = retrofit.create(UploadService.class);
    }

    //upload image file
    public void uploadSignatureImage(String url, final File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        doHttp(uploadService.signatureImage(url, body)).subscribe(new Action1<Object>() {
            @Override
            public void call(Object object) {
                file.delete();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ApiException apiException = ExceptionEngine.handleException(throwable);
                ToastUtils.showShort(MyApplication.getApplication().getAppComponent().getContext(), apiException.message);
            }
        });
    }

    //upload crash log file
    public void uploadCashLog(final File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        doHttp(uploadService.crashLog(body)).subscribe(new Action1<Object>() {
            @Override
            public void call(Object object) {
                file.delete();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ApiException apiException = ExceptionEngine.handleException(throwable);
                ToastUtils.showShort(MyApplication.getApplication().getAppComponent().getContext(), apiException.message);
            }
        });
    }

    //upload current position
    public void uploadPosition(double latitude, double longitude) {
        Observable observable = uploadService.uploadPosition(parseJsonBody("lat", String.format("%.8f", latitude), "lon", String.format("%.8f", longitude)));
        doHttp(observable).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                Log.d("uploadPosition", "upload position success");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d("uploadPosition", "upload position failure" + throwable.getMessage());
            }
        });
    }
}
