package fodel.com.fodelscanner.scanner.ui;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.freedom.yefeng.yfrecyclerview.YfListInterface;
import com.freedom.yefeng.yfrecyclerview.YfListMode;
import com.freedom.yefeng.yfrecyclerview.YfListRecyclerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.adapter.CodManagerAdapter;
import fodel.com.fodelscanner.scanner.api.entity.response.ResInvoice;
import fodel.com.fodelscanner.scanner.injector.component.DaggerCodManagementComponent;
import fodel.com.fodelscanner.scanner.injector.module.CodManagementModule;
import fodel.com.fodelscanner.scanner.mvp.model.CodManagementContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.CodManagementPresenter;
import fodel.com.fodelscanner.utils.ToastUtils;

public class CodManagementActivity extends BaseActivity<CodManagementPresenter> implements CodManagementContract.View {

    @ViewInject(R.id.ly_header)
    LinearLayout ly_header;
    @ViewInject(R.id.ly_footer)
    LinearLayout ly_footer;
    @ViewInject(R.id.tv_debt)
    TextView tv_debt;
    @ViewInject(R.id.tv_pending)
    TextView tv_pending;
    @ViewInject(R.id.btn_select_all)
    public Button btn_select_all;
    @ViewInject(R.id.btn_debt)
    Button btn_debt;
    @ViewInject(R.id.tv_select_num)
    TextView tv_select_num;
    @ViewInject(R.id.et_paid_num)
    EditText et_paid_num;
    @ViewInject(R.id.recyclerView)
    private YfListRecyclerView mList;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    private CodManagerAdapter codAdapter;
    private ArrayList<ResInvoice.Bill> mData = new ArrayList<>();
    public boolean isSelectAll = false;
    String currency;
    private List<ResInvoice.Bill> pending;

    @Override
    protected void initView() {
        initToolBar(toolbar, getResources().getString(R.string.menu_cod_management), true);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setDivider(R.drawable.divider);
        codAdapter = new CodManagerAdapter(mData, this);
        mList.setAdapter(codAdapter);
        codAdapter.setOnItemClickListener(new YfListInterface.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object o) {
                ResInvoice.Bill item = (ResInvoice.Bill) o;
                boolean isSelect = codAdapter.getSelectedMap().get(item.bill_no);
                if (!isSelect) {
                    codAdapter.singleSelected(item.bill_no, true, true);
                    codAdapter.add((double) item.money.amount / 100);
                    if (codAdapter.index == codAdapter.getSelectedMap().size()) {
                        isSelectAll = true;
                        btn_select_all.setText("Unselected All");
                    }
                } else {
                    codAdapter.singleSelected(item.bill_no, false, true);
                    codAdapter.dec((double) item.money.amount / 100);
                    isSelectAll = false;
                    btn_select_all.setText("Selected All");
                }
                setBtnBackground(codAdapter.index);
            }
        });
    }


    @Override
    protected void initData() {
        codAdapter.changeMode(YfListMode.MODE_LOADING);
        mPresenter.getData();
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_cod_management;
    }

    @Override
    protected void initInjector() {
        DaggerCodManagementComponent.builder().appComponent(getApplicationComponent()).codManagementModule(new CodManagementModule(this)).build().inject(this);
    }

    @Override
    public void showData(ResInvoice invoice) {
        pending = invoice.pending;
        ly_header.setVisibility(View.VISIBLE);
        currency = invoice.debt_money.currency == null ? "AED" : invoice.debt_money.currency;
        tv_debt.setText(currency+" "+ (float) invoice.debt_money.amount / 100 + " in the pocket");
        tv_pending.setText(invoice.pending.size() + " Pending invoices");
        if (invoice.with_driver.isEmpty()) {
            ly_footer.setVisibility(View.GONE);
        } else {
            ly_footer.setVisibility(View.VISIBLE);
        }
        codAdapter.setData(invoice.with_driver);
        codAdapter.initCheckBox();
    }

    @Override
    public void afterDebt() {
        initData();
        codAdapter.index = 0;
        codAdapter.total = 0;
        setBtnBackground(codAdapter.index);
    }

    @OnClick({R.id.btn_select_all, R.id.btn_debt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_all:
                selectAll();
                break;
            case R.id.btn_debt:
                if (codAdapter == null) {
                    return;
                }
                ArrayList<String> bill_nos = new ArrayList<>();
                for (Map.Entry<String, Boolean> entry : codAdapter.getSelectedMap().entrySet()) {
                    if (entry.getValue()) {
                        bill_nos.add(entry.getKey());
                    }
                }
                mPresenter.doDebt(bill_nos, Integer.valueOf(et_paid_num.getText().toString()) * 100);
                break;
        }
    }

    private void selectAll() {
        if (codAdapter == null) return;
        if (!isSelectAll) {
            codAdapter.selectedAll();
            btn_select_all.setText("Unselected All");
            isSelectAll = true;
        } else {
            codAdapter.unSelectedAll();
            btn_select_all.setText("Selected All");
            isSelectAll = false;
        }
        setBtnBackground(codAdapter.index);
    }


    private int dealNumber(double num) {
        int number = (new Double(num)).intValue();
        if (number > 10) {
            if ((number % 10 != 0)) {
                number = number / 10 * 10;
            }
        } else {
            return 0;
        }
        return number;
    }

    public void setBtnBackground(int size) {
        if (size != 0) {
            btn_debt.setEnabled(true);
            btn_debt.setTextColor(Color.WHITE);
        } else {
            btn_debt.setEnabled(false);
            btn_debt.setTextColor(Color.GRAY);
        }
        BigDecimal b = new BigDecimal(codAdapter.getTotal());
        double amount = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        tv_select_num.setText(currency + " " + amount);
        et_paid_num.setText(String.valueOf(dealNumber(codAdapter.getTotal())));
        et_paid_num.setSelection(et_paid_num.getText().length());
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
        codAdapter.changeMode(YfListMode.MODE_DATA);
    }
}
