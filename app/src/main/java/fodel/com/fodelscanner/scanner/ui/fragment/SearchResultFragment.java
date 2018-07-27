package fodel.com.fodelscanner.scanner.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qs.helper.printer.PrinterClass;

import fodel.com.fodelscanner.BuildConfig;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResSearchResult;
import fodel.com.fodelscanner.service.PrinterService;

public class SearchResultFragment extends Fragment {
    private static final String ARG_PARAM = "param";
    private ResSearchResult mParam;

    public SearchResultFragment() {

    }

    public static SearchResultFragment newInstance(ResSearchResult param) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        ((TextView) view.findViewById(R.id.tv_status)).setText(mParam.status);
        ((TextView) view.findViewById(R.id.tv_driver)).setText(mParam.assignedDriverEntity.firstName + mParam.assignedDriverEntity.lastName);
        ((TextView) view.findViewById(R.id.tv_awb)).setText(mParam.awb);
        ((TextView) view.findViewById(R.id.tv_order_no)).setText(mParam.orderNo);
        ((TextView) view.findViewById(R.id.tv_recipient_phone_number)).setText(mParam.recipientPhoneNumber);
        ((TextView) view.findViewById(R.id.tv_recipient_name)).setText(mParam.recipientName);
        ((TextView) view.findViewById(R.id.tv_address)).setText(mParam.address);
        ((TextView) view.findViewById(R.id.tv_weight)).setText(mParam.weight);
        ((TextView) view.findViewById(R.id.tv_price)).setText(mParam.price.currency + " " + mParam.price.amount);
        ((TextView) view.findViewById(R.id.tv_site_id)).setText(mParam.collectionPointEntity.siteId);
        ((TextView) view.findViewById(R.id.tv_site_name)).setText(mParam.collectionPointEntity.name);
        ((TextView) view.findViewById(R.id.tv_site_address)).setText(mParam.collectionPointEntity.address);
        ((TextView) view.findViewById(R.id.tv_site_area)).setText(mParam.collectionPointEntity.areaEntity.name);
        ((TextView) view.findViewById(R.id.tv_company_name)).setText(mParam.collectionPointEntity.managerUserEntity.companyName);
        ((TextView) view.findViewById(R.id.tv_manager_name)).setText(mParam.collectionPointEntity.managerUserEntity.firstName);
        ((TextView) view.findViewById(R.id.tv_manager_email)).setText(mParam.collectionPointEntity.managerUserEntity.email);
        ((TextView) view.findViewById(R.id.tv_manager_address)).setText(mParam.collectionPointEntity.managerUserEntity.address);
        ((TextView) view.findViewById(R.id.tv_manager_phone)).setText(mParam.collectionPointEntity.managerUserEntity.phone);
        ((TextView) view.findViewById(R.id.tv_e_name)).setText(mParam.ecommerceEntity.name);
        ((TextView) view.findViewById(R.id.tv_e_phone)).setText(mParam.ecommerceEntity.phone);
        ((TextView) view.findViewById(R.id.tv_e_address)).setText(mParam.ecommerceEntity.address);
        view.findViewById(R.id.btn_print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = BuildConfig.API_URL + "/api/v0.2/shipments/label/image/small/" + mParam.warehouseDetailDto.id + "/" + mParam.awb;
                //String url = "http://api2.test.fo-del.com/api/v0.2/shipments/label/image/small/2/w7";
                if (PrinterService.pl.getState() != PrinterClass.STATE_CONNECTED) {
                    getActivity().startService(new Intent(getActivity(), PrinterService.class));
                }
                PrinterService.downloadAndPrint(url);
            }
        });
        return view;
    }
}
