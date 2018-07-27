package fodel.com.fodelscanner.scanner.injector.module;

import dagger.Module;
import dagger.Provides;
import fodel.com.fodelscanner.scanner.mvp.model.UserInfoContract;
import fodel.com.fodelscanner.scanner.mvp.model.UserLoginContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.LoginPresenter;
import fodel.com.fodelscanner.scanner.mvp.presenter.ProfilePresenter;

/**
 * Created by fula on 2017/7/13.
 */
@Module
public class UserModule {

    UserLoginContract.View view;

    public UserModule(UserLoginContract.View view) {
        this.view = view;
    }

    @Provides
    LoginPresenter provideUserPresenter() {
        return new LoginPresenter(view);
    }

    UserInfoContract.View mView;

    public UserModule(UserInfoContract.View view) {
        this.mView = view;
    }

    @Provides
    ProfilePresenter provideProfilePresenter() {
        return new ProfilePresenter(mView);
    }
}
