package fodel.com.fodelscanner.scanner.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.library.treerecyclerview.adapter.TreeRecyclerAdapter;
import com.library.treerecyclerview.adapter.TreeRecyclerType;
import com.library.treerecyclerview.factory.ItemHelperFactory;
import java.util.ArrayList;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResScanBean;

/**
 * Created by fula on 2018/7/26.
 */

public class ScannedFragment extends Fragment {

    private TreeRecyclerAdapter treeRecyclerAdapter = new TreeRecyclerAdapter(TreeRecyclerType.SHOW_ALL);

    public static ScannedFragment newInstance() {
        ScannedFragment fragment = new ScannedFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(treeRecyclerAdapter);
        }
        return view;
    }

    public void setData(ArrayList<ResScanBean.Bill> data, boolean isScanned) {
        if (isScanned) {
            treeRecyclerAdapter.getItemManager().setText("scanned", null);
        }
        if (data.isEmpty()) {
            treeRecyclerAdapter.getItemManager().clean();
        } else {
            treeRecyclerAdapter.getItemManager().replaceAllItem(ItemHelperFactory.createItems(data, null));
        }
    }
}
