package fodel.com.fodelscanner.scanner.ui;

import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.cn.xtouch.tfl.alert.AlertView;
import com.cn.xtouch.tfl.alert.OnItemClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResFinalize;
import fodel.com.fodelscanner.scanner.injector.component.DaggerFinalizeComponent;
import fodel.com.fodelscanner.scanner.injector.module.FinalizeModule;
import fodel.com.fodelscanner.scanner.mvp.model.FinalizeContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.FinalizePresenter;
import fodel.com.fodelscanner.utils.SDCardUtils;
import fodel.com.fodelscanner.utils.ToastUtils;
import fodel.com.fodelscanner.view.LinePathView;

public class HandWriteActivity extends BaseActivity<FinalizePresenter> implements FinalizeContract.View {

    @ViewInject(R.id.view)
    LinePathView mPathView;
    private String type, name, id;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.ly_buttons)
    private LinearLayout buttons;
    private ArrayList<String> param;

    @Override
    protected void initView() {
        mPathView.setPaintWidth(10);
        mPathView.setButtonsListener(new MyButtonListener());
        type = getIntent().getStringExtra("type");
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        String title = null;
        switch (type) {
            case Constants.PICKUP_AT_WAREHOUSE:
            case Constants.PICKUP_AT_INTERNAL_WAREHOUSE:
                title = getString(R.string.pick_up_finalize_for) + " " + name;
                break;
            case Constants.DROP_AT_SHOP:
                title = getString(R.string.check_in_finalize_for) + " " + name;
                break;
            case Constants.DROP_RETURN_AT_WAREHOUSE:
            case Constants.PICKUP_RETURN_AT_SHOP:
                title = "Return";
                break;
        }
        initToolBar(toolbar, title, true);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        param = getIntent().getStringArrayListExtra("param");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_hand_write;
    }

    @Override
    protected void initInjector() {
        DaggerFinalizeComponent.builder().appComponent(getApplicationComponent()).finalizeModule(new FinalizeModule(this, this)).build().inject(this);
    }


    @Override
    public void showLoading() {
        showProgressDialog(this, getString(R.string.loading));
    }

    @Override
    public void dismiss() {
        dismissProgressDialog();
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(this, msg);
    }


    @OnClick({R.id.btn_submit, R.id.btn_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                mPresenter.submit(param, type, saveImage(), id);
                break;
            case R.id.btn_clear:
                mPathView.clear();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message message) {
        new AlertView(message.obj.toString(), null, null, null, new String[]{"Ok"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                ((AlertView) o).dismiss();
            }
        }).show();
    }

    private File saveImage() {
        File file = SDCardUtils.getDiskCacheDir(this,
                "images");
        if (!file.exists()) {
            file.mkdir();
        }
        String signatureImagePath = new Date().getTime() + ".png";
        File pf = new File(file.getAbsoluteFile() + File.separator
                + signatureImagePath);
        signatureImagePath = pf.getAbsolutePath();
        try {
            mPathView.save(signatureImagePath, true, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(signatureImagePath);
    }

    boolean isShow = false;

    @Override
    public void target() {
        EventBus.getDefault().post("");
    }

    @Override
    public void showAlert(List<ResFinalize.Awb> awbList) {
        String tip;
        if (awbList.size() == 1) {
            tip = awbList.size() + " parcel is not verified";
        } else {
            tip = awbList.size() + " parcels are not verified";
        }
        new AlertView("Alert", tip, null, null, new String[]{"Ok"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                ((AlertView) o).dismiss();
            }
        }).show();
    }

    private class MyButtonListener implements LinePathView.ButtonsListener {

        @Override
        public void showButton() {
            if (isShow) {
                return;
            }
            AnimationSet animationSet = new AnimationSet(true);
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    //X轴初始位置
                    Animation.RELATIVE_TO_SELF, 1.5f,
                    //X轴移动的结束位置
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    //y轴开始位置
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    //y轴移动后的结束位置
                    Animation.RELATIVE_TO_SELF, 0.0f);

            //3秒完成动画
            translateAnimation.setDuration(1000);
            //如果fillAfter的值为真的话，动画结束后，控件停留在执行后的状态
            animationSet.setFillAfter(true);
            //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
            animationSet.addAnimation(translateAnimation);
            buttons.startAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    buttons.setVisibility(View.VISIBLE);
                    isShow = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        @Override
        public void hideButton() {
            AnimationSet animationSet = new AnimationSet(true);
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    //X轴初始位置
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    //X轴移动的结束位置
                    Animation.RELATIVE_TO_SELF, 1.5f,
                    //y轴开始位置
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    //y轴移动后的结束位置
                    Animation.RELATIVE_TO_SELF, 0.0f);

            //3秒完成动画
            translateAnimation.setDuration(1000);
            //如果fillAfter的值为真的话，动画结束后，控件停留在执行后的状态
            animationSet.setFillAfter(true);
            //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
            animationSet.addAnimation(translateAnimation);
            buttons.startAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    buttons.setVisibility(View.INVISIBLE);
                    isShow = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

}
