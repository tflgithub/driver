package fodel.com.fodelscanner.scanner.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import com.library.treerecyclerview.adapter.TreeRecyclerAdapter;
import com.library.treerecyclerview.adapter.TreeRecyclerType;
import com.library.treerecyclerview.factory.ItemHelperFactory;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;

/**
 * Created by fula on 2018/7/26.
 */

public class HistoryDetailFragment extends BackHandledFragment {

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    @ViewInject(R.id.et_filter)
    private EditText et_filter;
    Object data;
    TreeRecyclerAdapter treeRecyclerAdapter = new TreeRecyclerAdapter(TreeRecyclerType.SHOW_DEFAULT);
    private MyHandler myHandler = new MyHandler();

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    treeRecyclerAdapter.getItemManager().clean();
                    break;
                case 1:
                    treeRecyclerAdapter.getItemManager().replaceAllItem(ItemHelperFactory.createItems((ArrayList) msg.obj, null));
                    break;
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_histroy_detail;
    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        recyclerView.setAdapter(treeRecyclerAdapter);
        et_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                treeRecyclerAdapter.getItemManager().setText(s.toString(), new ForegroundColorSpan(getResources().getColor(R.color.red)));
                doFilter(s.toString());
            }
        });
    }

    private void doFilter(final String s) {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                refresh(filter(s));
            }
        });
    }

    private ArrayList<Object> filter(String s) {
        ArrayList<Object> results = new ArrayList<>();
        if (data instanceof ResHistory.Delivery.SubItem) {
            for (ResHistory.Delivery.SubItem.DateItem dateItem : ((ResHistory.Delivery.SubItem) data).dateItems) {
                dateItem.parcelItems = filterItem(dateItem, s);
                if (!dateItem.parcelItems.isEmpty()) {
                    results.add(dateItem);
                }
            }
        } else if (data instanceof ResHistory.Cod) {
            for (ResHistory.Cod.CodDateItem codDateItem : ((ResHistory.Cod) data).codDateItem) {
                codDateItem.codMerchantItems = filterMerchantItems(codDateItem, s);
                if (!codDateItem.codMerchantItems.isEmpty()) {
                    results.add(codDateItem);
                }
            }
        }
        return results;
    }

    private ArrayList<ResHistory.Delivery.SubItem.DateItem.ParcelItem> filterItem(ResHistory.Delivery.SubItem.DateItem dateItem, String charString) {
        ArrayList<ResHistory.Delivery.SubItem.DateItem.ParcelItem> sourceList = (ArrayList<ResHistory.Delivery.SubItem.DateItem.ParcelItem>) sourceMap.get(dateItem);
        ArrayList<ResHistory.Delivery.SubItem.DateItem.ParcelItem> filteredList = new ArrayList<>();
        if (charString.isEmpty()) {
            filteredList = sourceList;
        } else {
            for (ResHistory.Delivery.SubItem.DateItem.ParcelItem parcelItem : sourceList) {
                if (parcelItem.awb.contains(charString) || parcelItem.name.contains(charString)) {
                    filteredList.add(parcelItem);
                }
            }
        }
        return filteredList;
    }

    private ArrayList<ResHistory.Cod.CodDateItem.CodMerchantItems> filterMerchantItems(ResHistory.Cod.CodDateItem codDateItem, String s) {
        ArrayList<ResHistory.Cod.CodDateItem.CodMerchantItems> filteredList = new ArrayList<>();
        ArrayList<ResHistory.Cod.CodDateItem.CodMerchantItems> resourceList = (ArrayList<ResHistory.Cod.CodDateItem.CodMerchantItems>) sourceMap.get(codDateItem);
        for (ResHistory.Cod.CodDateItem.CodMerchantItems codMerchantItem : resourceList) {
            codMerchantItem.invoiceItems = filterInvoiceItems(codMerchantItem, s);
            if (!codMerchantItem.invoiceItems.isEmpty()) {
                filteredList.add(codMerchantItem);
            }
        }
        return filteredList;
    }

    private ArrayList<ResHistory.Cod.CodDateItem.CodMerchantItems.InvoiceItems> filterInvoiceItems(ResHistory.Cod.CodDateItem.CodMerchantItems merchantItems, String charString) {
        ArrayList<ResHistory.Cod.CodDateItem.CodMerchantItems.InvoiceItems> filteredList = new ArrayList<>();
        ArrayList<ResHistory.Cod.CodDateItem.CodMerchantItems.InvoiceItems> resourceList = (ArrayList<ResHistory.Cod.CodDateItem.CodMerchantItems.InvoiceItems>) sourceMap.get(merchantItems);
        if (charString.isEmpty()) {
            filteredList = resourceList;
        } else {
            for (ResHistory.Cod.CodDateItem.CodMerchantItems.InvoiceItems invoiceItems : resourceList) {
                if (invoiceItems.billNo.contains(charString)) {
                    filteredList.add(invoiceItems);
                }
            }
        }
        return filteredList;
    }

    private void refresh(ArrayList<Object> data) {
        Message message = new Message();
        if (data.isEmpty()) {
            message.what = 0;
            myHandler.sendMessage(message);
        } else {
            message.what = 1;
            message.obj = data;
            myHandler.sendMessage(message);
        }
    }

    private Map<Object, Object> sourceMap = new HashMap<>();

    @Override
    public void initData() {
        data = getArguments().getParcelable("data");
        if (data instanceof ResHistory.Delivery.SubItem) {
            treeRecyclerAdapter.getItemManager().addItems(ItemHelperFactory.createItems(((ResHistory.Delivery.SubItem) data).dateItems, null));
            for (ResHistory.Delivery.SubItem.DateItem dateItem : ((ResHistory.Delivery.SubItem) data).dateItems) {
                sourceMap.put(dateItem, dateItem.parcelItems);
            }
        } else if (data instanceof ResHistory.Cod) {
            treeRecyclerAdapter.getItemManager().addItems(ItemHelperFactory.createItems(((ResHistory.Cod) data).codDateItem, null));
            for (ResHistory.Cod.CodDateItem codDateItem : ((ResHistory.Cod) data).codDateItem) {
                sourceMap.put(codDateItem, codDateItem.codMerchantItems);
                for (ResHistory.Cod.CodDateItem.CodMerchantItems merchantItems : codDateItem.codMerchantItems) {
                    sourceMap.put(merchantItems, merchantItems.invoiceItems);
                }
            }
        }
    }
}
