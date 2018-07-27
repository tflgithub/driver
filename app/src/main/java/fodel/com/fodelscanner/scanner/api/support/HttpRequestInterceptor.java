package fodel.com.fodelscanner.scanner.api.support;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.MyAcitivityManager;
import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.cache.bean.User;
import fodel.com.fodelscanner.scanner.api.cache.dao.UserDao;
import fodel.com.fodelscanner.scanner.api.entity.response.ResAuth;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.ui.LoginActivity;
import fodel.com.fodelscanner.utils.LogUtils;
import fodel.com.fodelscanner.utils.SPUtils;
import fodel.com.fodelscanner.utils.ToastUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by fula on 2017/7/19.
 */

public class HttpRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().build();
        Response response = chain.proceed(request);
        final Context context = MyApplication.getApplication().getApplicationContext();
        if (response.code() == ExceptionEngine.UNAUTHORIZED) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort(context, context.getString(R.string.exit_tip));
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    MyAcitivityManager.finishAll();
                }
            });
        } else if (response.code() == ExceptionEngine.TOKEN_EXPIRED) {
            UserDao userDao = new UserDao(context);
            User user = userDao.query();
            String regId = JPushInterface.getRegistrationID(MyApplication.getApplication().getAppComponent().getContext());
            if (regId.isEmpty()) {
                regId = FirebaseInstanceId.getInstance().getToken();
            }
            LogUtils.d(regId);
            MyApplication.getApplication().getAppComponent().getUserApi().login(user.userName, user.passWord, regId).subscribe(new Subscriber<ResAuth>() {
                @Override
                public void onCompleted() {
                    MyAcitivityManager.getCurrentActivity().reLoginQuest();
                }

                @Override
                public void onError(Throwable e) {
                    ApiException apiException = ExceptionEngine.handleException(e);
                    ToastUtils.showShort(context, apiException.message);
                }

                @Override
                public void onNext(ResAuth resAuth) {
                    SPUtils.put(MyApplication.getApplication().getAppComponent().getContext(), Constants.TOKEN, resAuth.token);
                }
            });
        }
        return response;
    }
}
