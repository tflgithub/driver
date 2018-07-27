package fodel.com.fodelscanner.scanner.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.injector.component.DaggerLoginComponent;
import fodel.com.fodelscanner.scanner.injector.module.UserModule;
import fodel.com.fodelscanner.scanner.mvp.model.UserLoginContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.LoginPresenter;
import fodel.com.fodelscanner.utils.ToastUtils;
public class LoginActivity extends BaseActivity<LoginPresenter> implements UserLoginContract.View {

    @ViewInject(R.id.et_username)
    private EditText et_username;
    @ViewInject(R.id.et_password)
    private EditText et_password;
    private boolean userisNull = true;
    private boolean pwdisNull = true;
    @ViewInject(R.id.btn_login)
    private Button btn_login;

    @Override
    protected void initView() {
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    userisNull = false;
                    if (!pwdisNull) {
                        btn_login.setEnabled(true);
                    }
                } else {
                    userisNull = true;
                    btn_login.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    pwdisNull = false;
                    if (!userisNull) {
                        btn_login.setEnabled(true);
                    }
                } else {
                    pwdisNull = true;
                    btn_login.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {
        DaggerLoginComponent.builder().appComponent(getApplicationComponent()).userModule(new UserModule(this)).build().inject(this);
    }

    @OnClick({R.id.btn_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mPresenter.login(et_username.getText().toString(), et_password.getText().toString());
                break;
        }
    }

    @Override
    public void showLoading() {
        showProgressDialog(this, getResources().getString(R.string.loading));
    }

    @Override
    public void dismiss() {
        dismissProgressDialog();
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void target() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
