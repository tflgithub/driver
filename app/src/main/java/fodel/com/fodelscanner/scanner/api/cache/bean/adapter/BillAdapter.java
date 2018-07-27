package fodel.com.fodelscanner.scanner.api.cache.bean.adapter;


import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.scanner.api.cache.bean.Bill;
import fodel.com.fodelscanner.scanner.api.entity.response.ResBill;
import fodel.com.fodelscanner.utils.DateUtil;

/**
 * Created by fula on 2017/7/14.
 */

public class BillAdapter {

    private ResBill resBill;

    private Bill bill;

    public BillAdapter(ResBill resBill) {
        this.resBill = resBill;
        bill = new Bill();
    }

    public Bill get(String type, String eName) {
        bill.awb = resBill.awb;
        bill.shop_name = resBill.collection_point_name;
        bill.site_id = resBill.site_id;
        bill.state = Constants.IS_NOT_VAIL;
        bill.type = type;
        bill.e_name = eName;
        bill.create_time = DateUtil.getCurrentTime();
        return bill;
    }
}
