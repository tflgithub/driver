package fodel.com.fodelscanner.scanner.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.tfl.photocropper.CropFileUtils;
import com.cn.tfl.photocropper.CropHandler;
import com.cn.tfl.photocropper.CropHelper;
import com.cn.tfl.photocropper.CropParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import fodel.com.fodelscanner.MyAcitivityManager;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.cache.bean.User;
import fodel.com.fodelscanner.scanner.injector.component.DaggerProfileComponent;
import fodel.com.fodelscanner.scanner.injector.module.UserModule;
import fodel.com.fodelscanner.scanner.mvp.model.UserInfoContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.ProfilePresenter;
import fodel.com.fodelscanner.utils.GlideUtils;
import fodel.com.fodelscanner.view.SelectPopupWindow;

public class ProfileActivity extends BaseActivity<ProfilePresenter> implements UserInfoContract.View, CropHandler {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.img_head)
    private ImageView img_head;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.tv_telephone)
    private TextView tv_telephone;
    private CropParams mCropParams;
    private SelectPopupWindow menuWindow;

    @Override
    protected void initView() {
        initToolBar(toolbar, getResources().getString(R.string.profile), true);
        mCropParams = new CropParams(this);
    }

    @Override
    protected void initData() {
        mPresenter.getUser();
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initInjector() {
        DaggerProfileComponent.builder().appComponent(getApplicationComponent()).userModule(new UserModule(this)).build().inject(this);
    }

    @Override
    protected void onDestroy() {
        CropHelper.clearCacheDir();
        super.onDestroy();
    }

    @OnClick({R.id.img_head, R.id.btn_logoff, R.id.change_name_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_head:
                changPortrait();
                break;
            case R.id.btn_logoff:
                exit();
                break;
            case R.id.change_name_btn:
//                Intent intent1 = new Intent(DetailedInfoActivity.this,
//                        EditInfoActivity.class);
//                intent1.putExtra(Constants.CHANGE_INFO, tv_name.getText()
//                        .toString());
//                startActivityForResult(intent1, 100);
                break;
        }
    }

    /**
     * 退出
     */
    private void exit() {
        // 实例化SelectPicPopupWindow
        backgroundAlpha(0.5f);
        menuWindow = new SelectPopupWindow(this, exitItemsOnClick,
                R.layout.select_exit_popupwindow);
        menuWindow.setOnDismissListener(new PopupDismissListener());
        // 显示窗口
        menuWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }


    // 为弹出窗口实现监听类
    private View.OnClickListener exitItemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_first:
                    mPresenter.logOff();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    MyAcitivityManager.finishAll();
                    break;
                case R.id.btn_second:
                    MyAcitivityManager.exitApp();
                    break;
            }
        }
    };


    /**
     * 修改头像
     */
    private void changPortrait() {
        // 实例化SelectPicPopupWindow
        backgroundAlpha(0.5f);
        menuWindow = new SelectPopupWindow(this, itemsOnClick,
                R.layout.select_pic_popupwindow);
        menuWindow.setOnDismissListener(new PopupDismissListener());
        // 显示窗口
        menuWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismiss() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showUser(User user) {
        tv_name.setText(user.firstName + " " + user.lastName);
        tv_telephone.setText(user.phoneNumber);
        loadPortrait(user.portraitUrl);
    }

    @Override
    public void loadPortrait(String url) {
        GlideUtils.loadCircleImage(this, url, img_head, 0, 0);
        Intent intent = new Intent();
        intent.putExtra("portraitUrl", url);
        setResult(Activity.RESULT_OK, intent);
    }

    /**
     * 添加弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class PopupDismissListener implements SelectPopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;// 0.0-1.0
        getWindow().setAttributes(lp);
    }


    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            mCropParams.refreshUri();
            menuWindow.dismiss();
            backgroundAlpha(1f);
            switch (v.getId()) {
                case R.id.btn_first:
                    mCropParams.enable = true;
                    mCropParams.compress = false;
                    Intent intent = CropHelper.buildCameraIntent(mCropParams);
                    startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
                    break;
                case R.id.btn_second:
                    mCropParams.enable = true;
                    mCropParams.compress = false;
                    Intent intent2 = CropHelper.buildGalleryIntent(mCropParams);
                    startActivityForResult(intent2, CropHelper.REQUEST_CROP);
                    break;
            }
        }
    };

    private Bitmap mBitmap = null;

    @Override
    public void onPhotoCropped(Intent intent, Uri uri) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            if (mBitmap != null) {
                String path = CropFileUtils.getPath(this, uri);
                mPresenter.changePortrait(path);
            }
        }
    }

    /**
     * 保存头像到服务器
     */

    @Override
    public void onCompressed(Uri uri) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onFailed(String message) {

    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CropHelper.REQUEST_CAMERA:
            case CropHelper.REQUEST_CROP:
                CropHelper.handleResult(this, requestCode, resultCode, data);
                break;
            case 100:
//                if (resultCode == RESULT_OK) {
//                    String name = data.getStringExtra(Constants.CHANGE_INFO);
//                    tv_name.setText(name);
//                    sharedPref.edit().putString(Constants.USERNAME, name).commit();
//                }
                break;
        }

    }


}
