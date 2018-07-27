package fodel.com.fodelscanner.scanner.ui;

import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.cn.xtouch.tfl.alert.AlertView;
import com.cn.xtouch.tfl.alert.OnDismissListener;
import com.cn.xtouch.tfl.alert.OnItemClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResCodCollection;
import fodel.com.fodelscanner.scanner.injector.component.DaggerCodCollectionComponent;
import fodel.com.fodelscanner.scanner.injector.module.CodCollectionModule;
import fodel.com.fodelscanner.scanner.mvp.model.CodCollectionContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.CodCollectionPresenter;
import fodel.com.fodelscanner.scanner.ui.adapter.CodCollectionAdapter;
import fodel.com.fodelscanner.utils.ToastUtils;

public class CodCollectionActivity extends BaseActivity<CodCollectionPresenter> implements CodCollectionContract.View {

    @ViewInject(R.id.expanded_listView)
    ExpandableListView listView;
    private String shopId;
    @ViewInject(R.id.btn)
    Button verificationButton;
    @ViewInject(R.id.btn_generate)
    Button buttonGenerate;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    private View footView;
    private EditText editText;
    private CodCollectionAdapter adapter;

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        shopId = getIntent().getStringExtra("id");
        initToolBar(toolbar, getString(R.string.cod_collection_for) + " " + getIntent().getStringExtra("name"), true);
        footView = LayoutInflater.from(this).inflate(R.layout.edit_listview_footer, null);
        listView.addFooterView(footView);
        verificationButton.setTag(1);
    }

    @Override
    protected void initData() {
        mPresenter.getCodCollection(shopId);
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_cod_collection;
    }

    @Override
    protected void initInjector() {
        DaggerCodCollectionComponent.builder().appComponent(getApplicationComponent()).codCollectionModule(new CodCollectionModule(this)).build().inject(this);
    }

    int totalAmount;

    @Override
    public void showData(final ResCodCollection resCodCollection) {
        adapter = new CodCollectionAdapter(this, resCodCollection.invoices, resCodCollection.currency);
        adapter.setChangeButtonTextListener(new CodCollectionAdapter.ChangeButtonTextListener() {
            @Override
            public void onAdd(int money) {
                totalAmount = totalAmount + money;

                String buttonTxt = "";
                if ((Integer) verificationButton.getTag() == 1) {
                    buttonTxt = getString(R.string.verify) + "(" + resCodCollection.currency + " " + (float)totalAmount/100 + ")";
                } else {
                    buttonTxt = getString(R.string.receive) + "(" + resCodCollection.currency + " " + (float)totalAmount/100 + ")";
                }
                verificationButton.setText(buttonTxt);
                verificationButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                verificationButton.setEnabled(true);
                verificationButton.setBackgroundResource(R.drawable.scan_btn_selector);
            }

            @Override
            public void onDec(int money) {
                totalAmount = totalAmount - money;
                String buttonTxt = "";
                if ((Integer) verificationButton.getTag() == 1) {
                    buttonTxt = getString(R.string.verify) + "(" + resCodCollection.currency + " " + (float)totalAmount/100 + ")";
                } else {
                    buttonTxt = getString(R.string.receive) + "(" + resCodCollection.currency + " " + (float)totalAmount/100 + ")";
                }
                verificationButton.setText(buttonTxt);
                if (totalAmount <=0) {
                    verificationButton.setEnabled(false);
                    verificationButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    verificationButton.setBackgroundResource(R.drawable.btn_selector);
                }
            }
        });
        listView.setAdapter(adapter);
        for (int i = 0; i < resCodCollection.invoices.size(); i++) {
            listView.expandGroup(i);
        }
        editText = (EditText) footView.findViewById(R.id.et_order_no_input);
        Button button = (Button) footView.findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()) {
                    return;
                }
                mPresenter.addExtraInvoice(editText.getText().toString());
            }
        });
        if (!resCodCollection.generate_invoice_text.equals("")) {
            buttonGenerate.setVisibility(View.VISIBLE);
            buttonGenerate.setText(resCodCollection.generate_invoice_text);
        } else {
            buttonGenerate.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateUi() {
        mPresenter.getCodCollection(shopId);
    }

    @Override
    public void showError(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void target(String message) {
        new AlertView(message, null, null, null, new String[]{"Ok"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                ((AlertView) o).dismiss();
            }
        }).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                EventBus.getDefault().post("");
            }
        }).show();
    }

    @Override
    public void afterVerify() {
        verificationButton.setTag(2);
        String text = verificationButton.getText().toString().replace(getString(R.string.verify),getString(R.string.receive));
        verificationButton.setText(text);
    }

    @Override
    public void showLoading() {
        showProgressDialog(this, getString(R.string.loading));
    }

    @Override
    public void dismiss() {
        dismissProgressDialog();
    }


    @OnClick({R.id.btn, R.id.btn_generate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                List<String> bill_nos = new ArrayList<>();
                if (adapter != null) {
                    for (Map.Entry<String, Boolean> entry : adapter.groupCheckBoxMap.entrySet()) {
                        if (entry.getValue()) {
                            bill_nos.add(entry.getKey());
                        }
                    }
                }
                if (bill_nos.isEmpty()) {
                    return;
                }
                if ((Integer) verificationButton.getTag() == 1) {
                    mPresenter.shopVerify(bill_nos);
                } else if ((Integer) verificationButton.getTag() == 2) {
                    mPresenter.shopReceived(bill_nos);
                }
//                new AlertView("Cod Collect Confirmation", " You already receive the cod money? ", null, "No", new String[]{"Yes"},
//                        null, CodCollectionActivity.this, AlertView.Style.Alert, CodCollectionActivity.this).show();
                break;
            case R.id.btn_generate:
                mPresenter.generate(shopId);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
}
