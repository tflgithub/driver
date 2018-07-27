package fodel.com.fodelscanner.scanner.mvp.presenter;

import com.google.firebase.iid.FirebaseInstanceId;

import cn.jpush.android.api.JPushInterface;
import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.UserApi;
import fodel.com.fodelscanner.scanner.api.cache.bean.User;
import fodel.com.fodelscanner.scanner.api.cache.bean.adapter.UserAdapter;
import fodel.com.fodelscanner.scanner.api.cache.dao.UserDao;
import fodel.com.fodelscanner.scanner.api.entity.response.ResAuth;
import fodel.com.fodelscanner.scanner.api.entity.response.ResUser;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.mvp.model.UserLoginContract;
import fodel.com.fodelscanner.utils.LogUtils;
import fodel.com.fodelscanner.utils.SPUtils;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by fula on 2017/7/21.
 */

public class LoginPresenter extends BasePresenter implements UserLoginContract.IPresenter {

    UserApi userApi;

    UserDao userDao;

    public LoginPresenter(UserLoginContract.View mView) {
        this.mView = mView;
        userApi = MyApplication.getApplication().getAppComponent().getUserApi();
        userDao = new UserDao(MyApplication.getApplication().getAppComponent().getContext());
    }

    UserLoginContract.View mView;

    public void login(final String userName, final String passWord) {
        String regId = FirebaseInstanceId.getInstance().getToken();
        userApi.login(userName, passWord, regId).subscribe(new Subscriber<ResAuth>() {
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
            public void onNext(ResAuth resAuth) {
                SPUtils.put(MyApplication.getApplication().getApplicationContext(), Constants.TOKEN, resAuth.token);
                userApi.getUserInfo().subscribe(new Action1<ResUser>() {
                    @Override
                    public void call(ResUser resUser) {
                        User user = new UserAdapter(resUser).get();
                        user.userName = userName;
                        user.passWord = passWord;
                        user.isLoin = true;
                        userDao.insertOrUpdate(user);
                        mView.target();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ApiException apiException = ExceptionEngine.handleException(throwable);
                        LogUtils.d(apiException.message);
                    }
                });
            }
        });
    }
}
