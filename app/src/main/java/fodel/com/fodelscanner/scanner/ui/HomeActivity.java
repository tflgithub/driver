package fodel.com.fodelscanner.scanner.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.BuildConfig;
import fodel.com.fodelscanner.MyAcitivityManager;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.activity.NotificationActivity;
import fodel.com.fodelscanner.adapter.ExpandedMenuAdapter;
import fodel.com.fodelscanner.scanner.api.cache.bean.User;
import fodel.com.fodelscanner.scanner.api.cache.dao.UserDao;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;
import fodel.com.fodelscanner.scanner.injector.component.DaggerHomeComponent;
import fodel.com.fodelscanner.scanner.injector.module.HomeModule;
import fodel.com.fodelscanner.scanner.mvp.model.HomeContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.HomePresenter;
import fodel.com.fodelscanner.utils.AppUtils;
import fodel.com.fodelscanner.utils.GlideUtils;
import fodel.com.fodelscanner.utils.NetUtils;
import fodel.com.fodelscanner.utils.ToastUtils;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    @ViewInject(R.id.expanded_menu)
    private ExpandableListView expanded_menu;
    @ViewInject(R.id.tv_version_name)
    TextView version_name;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    @ViewInject(R.id.iv_message)
    private ImageView iv_message;
    ImageView head_img;
    View headerView;
    UserDao userDao;
    User user;
    @ViewInject(R.id.iv_refresh)
    ImageView iv_refresh;
    ObjectAnimator refreshAnimator;
    @ViewInject(R.id.ly_buttons)
    LinearLayout buttons;

    @Override
    protected void initView() {
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);
        drawerLayout.setStatusBarBackground(R.color.white);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(false);
        refreshAnimator = ObjectAnimator.ofFloat(iv_refresh, "rotation", 0f, -360f);
        // Duration is smaller and faster
        refreshAnimator.setDuration(100);
        refreshAnimator.setRepeatCount(-1);
        refreshAnimator.setInterpolator(new LinearInterpolator());
        refreshAnimator.setRepeatMode(ValueAnimator.RESTART);
        refreshAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mPresenter.getData();
            }
        });
        version_name.setText(AppUtils.getVersionName(this));
        initMenu();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        user = userDao.query();
        head_img = (ImageView) headerView.findViewById(R.id.profile_image);
        TextView tv_name = (TextView) headerView.findViewById(R.id.user_name);
        tv_name.setText(user.firstName + " " + user.lastName);
        startRefresh();
    }

    @Subscribe
    public void finishAllActivity(String msg) {
        startRefresh();
        MyAcitivityManager.finishAll(this);
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initInjector() {
        DaggerHomeComponent.builder().appComponent(getApplicationComponent()).homeModule(new HomeModule(this, this)).build().inject(this);
    }

    private void initMenu() {
        headerView = LayoutInflater.from(this)
                .inflate(R.layout.drawer_header, null);
        expanded_menu.addHeaderView(headerView);
        int color = Color.parseColor(BuildConfig.MENU_COLOR);
        expanded_menu.setBackgroundColor(color);
        expanded_menu.setGroupIndicator(null);
        userDao = new UserDao(this);
    }

    @OnClick({R.id.iv_refresh, R.id.iv_notification})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_refresh:
                startRefresh();
                mPresenter.getData();
                break;
            case R.id.iv_notification:
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                startActivity(intent);
                iv_message.setVisibility(View.GONE);
                break;
        }
    }

    private void startRefresh() {
        if (!NetUtils.isConnected(this)) {
            ToastUtils.showShort(this, getString(R.string.no_network));
            return;
        }
        if (!refreshAnimator.isStarted()) {
            refreshAnimator.start();
        }
    }

    private void cancelRefresh() {
        if (refreshAnimator.isRunning()) {
            refreshAnimator.cancel();
        }
    }

    @Override
    public void showMenu(final List<ResMenu> list) {
        cancelRefresh();
        addButtons(list);
        drawerToggle.setDrawerIndicatorEnabled(true);
        ExpandedMenuAdapter adapter = new ExpandedMenuAdapter(this, list);
        expanded_menu.setAdapter(adapter);
        expanded_menu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ResMenu resMenu = list.get(groupPosition);
                intent(resMenu);
                return true;
            }
        });
    }

    private void intent(ResMenu resMenu) {
        String className = resMenu.class_name;
        Class toActivity = null;
        try {
            if (!className.isEmpty()) {
                toActivity = Class.forName(className);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (toActivity != null) {
            Intent intent = new Intent(HomeActivity.this, toActivity);
            intent.putParcelableArrayListExtra("sub_menu_list", (ArrayList<? extends Parcelable>) resMenu.list);
            intent.putExtra("menu_title", resMenu.name);
            startActivityForResult(intent, 100);
        }
    }

    private void addButtons(List<ResMenu> list) {
        buttons.removeAllViews();
        for (final ResMenu resMenu : list) {
            if (resMenu.amount != null) {
                Button button = (Button) LayoutInflater.from(this).inflate(R.layout.menu_button, buttons, false);
                button.setText(resMenu.name + "(" + resMenu.amount + ")");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent(resMenu);
                    }
                });
                buttons.addView(button);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            GlideUtils.loadCircleImage(this, data.getStringExtra("portraitUrl"), head_img, 0, 0);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismiss() {

    }

    @Override
    public void showError(String msg) {
        cancelRefresh();
        ToastUtils.showShort(this, msg);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
