package fodel.com.fodelscanner.scanner.mvp.presenter;

import android.content.Context;

import java.io.File;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.UserApi;
import fodel.com.fodelscanner.scanner.api.cache.bean.User;
import fodel.com.fodelscanner.scanner.api.cache.dao.UserDao;
import fodel.com.fodelscanner.scanner.api.entity.response.ResPortrait;
import fodel.com.fodelscanner.scanner.api.exception.ApiException;
import fodel.com.fodelscanner.scanner.api.exception.ExceptionEngine;
import fodel.com.fodelscanner.scanner.mvp.model.UserInfoContract;
import fodel.com.fodelscanner.service.LocationService;
import fodel.com.fodelscanner.utils.NetUtils;
import fodel.com.fodelscanner.utils.ToastUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by fula on 2017/7/24.
 */

public class ProfilePresenter extends BasePresenter implements UserInfoContract.IPresenter {

    UserDao userDao;

    UserInfoContract.View mView;

    Context mContext;

    UserApi userApi;

    public ProfilePresenter(UserInfoContract.View view) {
        mContext = MyApplication.getApplication().getAppComponent().getContext();
        userApi = MyApplication.getApplication().getAppComponent().getUserApi();
        this.userDao = new UserDao(mContext);
        this.mView = view;
    }

    @Override
    public void getUser() {
        Observable<User> observableFromDB = Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                User user = userDao.query();
                subscriber.onNext(user);
            }
        });
        observableFromDB.subscribe(new Action1<User>() {
            @Override
            public void call(User user) {
                mView.showUser(user);
            }
        });
    }

    @Override
    public void changePortrait(String path) {
        if (!NetUtils.isConnected(mContext)) {
            ToastUtils.showShort(mContext, mContext.getResources().getString(R.string.no_network));
            return;
        }
        File file = new File(path);
        if (file.length() == 0) {
            ToastUtils.showShort(mContext, "Failed to save photo");
            return;
        }
        userApi.editPortrait(file).subscribe(new Subscriber<ResPortrait>() {
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
            public void onNext(ResPortrait resPortrait) {
                User user = userDao.query();
                user.portraitUrl = resPortrait.url;
                userDao.insertOrUpdate(user);
                mView.loadPortrait(user.portraitUrl);
            }
        });
    }

    @Override
    public void logOff() {
        userDao.updateSate();
    }
}
