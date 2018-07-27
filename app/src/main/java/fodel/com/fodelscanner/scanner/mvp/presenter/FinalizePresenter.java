package fodel.com.fodelscanner.scanner.mvp.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.OperatorApi;
import fodel.com.fodelscanner.scanner.api.UploadApi;
import fodel.com.fodelscanner.scanner.api.entity.response.BaseEntity;
import fodel.com.fodelscanner.scanner.api.entity.response.ResFinalize;
import fodel.com.fodelscanner.scanner.api.entity.response.ResSignature;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.mvp.model.FinalizeContract;
import fodel.com.fodelscanner.utils.JsonUtils;
import fodel.com.fodelscanner.utils.ToastUtils;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by fula on 2017/7/19.
 */

public class FinalizePresenter extends BasePresenter implements FinalizeContract.IPresenter {

    public static final String TAG = FinalizePresenter.class.getSimpleName();

    FinalizeContract.View mView;

    Context mContext;

    OperatorApi operatorApi;

    UploadApi uploadApi;

    File file;

    public FinalizePresenter(FinalizeContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
        operatorApi = MyApplication.getApplication().getAppComponent().getOperatorApi();
        uploadApi = MyApplication.getApplication().getAppComponent().getUploadApi();
    }

    private void finalize(final List<String> awb, final String type, final String id) {
        Subscription subscription = operatorApi.finalize(awb, type, id).subscribe(new Subscriber<ResFinalize>() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onCompleted() {
                mView.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismiss();
                ApiException apiException = ExceptionEngine.handleException(e);
                mView.showError(apiException.message);
            }

            @Override
            public void onNext(ResFinalize resFinalize) {
                if (!resFinalize.remaining.isEmpty()) {
                    mView.showAlert(resFinalize.remaining);
                } else if (!resFinalize.failure.isEmpty()) {
                    mView.showAlert(resFinalize.failure);
                } else {
                    signature(awb, type, id);
                }
            }
        });
        addSubscription(subscription);
    }

    private void signature(List<String> list, String type, String id) {
        Subscription subscription = operatorApi.signature(list, type, id).subscribe(new Subscriber<ResSignature>() {
            @Override
            public void onStart() {
                mView.showLoading();
            }

            @Override
            public void onCompleted() {
                mView.dismiss();
                mView.target();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismiss();
                ApiException apiException = ExceptionEngine.handleException(e);
                if (apiException.response != null) {
                    try {
                        Type type = new TypeToken<BaseEntity<ResSignature>>() {
                        }.getType();
                        BaseEntity<?> res = JsonUtils.jsonToBaseEntity(apiException.response, type);
                        ToastUtils.showShort(mContext, res.msg);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    ToastUtils.showShort(mContext, apiException.message);
                }
            }

            @Override
            public void onNext(ResSignature resSignature) {
                mView.dismiss();
                uploadImage(resSignature.image_url);
            }
        });
        addSubscription(subscription);
    }

    private void uploadImage(String url) {
        uploadApi.uploadSignatureImage(url, file);
    }

    @Override
    public void submit(ArrayList<String> list, String type, File file, String id) {
        this.file = file;
        finalize(list, type, id);
    }
}
