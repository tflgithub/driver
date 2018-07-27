package fodel.com.fodelscanner.scanner.ui.fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import com.freedom.yefeng.yfrecyclerview.YfListInterface;
import com.freedom.yefeng.yfrecyclerview.YfListRecyclerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.adapter.HistorySubItemAdapter;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;

/**
 * Created by fula on 2018/7/25.
 */

public class HistoryDeliveryFragment extends BackHandledFragment {

    @ViewInject(R.id.recyclerView)
    private YfListRecyclerView mList;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    private HistorySubItemAdapter mAdapter;
    private ResHistory.Delivery mData;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_history_subitem;
    }

    @Override
    public void initView() {
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setDivider(R.drawable.divider);
    }

    @Override
    public void initData() {
        mData = getArguments().getParcelable("subItem");
        tv_name.setText(mData.name);
        mAdapter = new HistorySubItemAdapter(mData.subItem);
        mList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new YfListInterface.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object o) {
                ResHistory.Delivery.SubItem subItem = (ResHistory.Delivery.SubItem) o;
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
                HistoryDetailFragment historyDetailFragment = (HistoryDetailFragment) manager.findFragmentByTag("historyDetail");
                if (historyDetailFragment == null) {
                    historyDetailFragment = new HistoryDetailFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", subItem);
                historyDetailFragment.setArguments(bundle);
                ft.replace(R.id.fl_container, historyDetailFragment, "historyDetail");
                ft.addToBackStack("tag");
                ft.commit();
            }
        });
    }
}
