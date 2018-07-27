package fodel.com.fodelscanner.activity;

import android.content.Intent;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.jpush.android.api.JPushInterface;
import fodel.com.fodelscanner.BuildConfig;
import fodel.com.fodelscanner.CrashExceptionHandler;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.cache.bean.User;
import fodel.com.fodelscanner.scanner.api.cache.dao.UserDao;
import fodel.com.fodelscanner.scanner.ui.BaseActivity;
import fodel.com.fodelscanner.scanner.ui.HomeActivity;
import fodel.com.fodelscanner.scanner.ui.LoginActivity;
import fodel.com.fodelscanner.upgrade.CheckUpdateTask;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getResourcesId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initView() {
        ViewUtils.inject(this);
    }

    @Override
    protected void initData() {
        if (!BuildConfig.DEBUG) {
            CrashExceptionHandler.getInstance(this).checkCrash();
        }
        Intent intent = new Intent();
        UserDao userDao = new UserDao(this);
        User user = userDao.query();
        if (user != null && user.isLoin) {
            intent.setClass(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        registerWithNotificationHubs();
    }

    private void registerWithNotificationHubs() {
        if (checkPlayServices()) {
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability
                        .getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        new CheckUpdateTask(this).execute(BuildConfig.UPGRADE);
    }


    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @OnClick({R.id.login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                gotoLoginActivity();
                break;
        }
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
