package fodel.com.fodelscanner.scanner.api.cache.bean.adapter;

import fodel.com.fodelscanner.scanner.api.cache.bean.Merchant;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMerchant;
import fodel.com.fodelscanner.service.LocationService;
import fodel.com.fodelscanner.utils.MapUtil;

/**
 * Created by fula on 2017/7/27.
 */

public class MerchantAdapter {

    private ResMerchant resMerchant;

    private Merchant merchant;

    public MerchantAdapter(ResMerchant resMerchant) {
        this.resMerchant = resMerchant;
        this.merchant = new Merchant();
    }

    public Merchant get() {
        merchant.site_id = resMerchant.site_id;
        merchant.station_id = resMerchant.station_id;
        merchant.station_name = resMerchant.station_name;
        merchant.address = resMerchant.address;
        merchant.area = resMerchant.area;
        merchant.phone_number=resMerchant.phone_number;
        merchant.distance = getDistance(resMerchant.lt, resMerchant.lg);
        StringBuffer labelBuffer = new StringBuffer();
        StringBuffer typeBuffer = new StringBuffer();
        for (ResMerchant.TypeMark mark : resMerchant.type_mark) {
            labelBuffer.append(mark.label_name).append(",");
            typeBuffer.append(mark.type).append(",");
        }
        merchant.label = labelBuffer.toString();
        merchant.type = typeBuffer.toString();
        StringBuffer location = new StringBuffer();
        location.append(resMerchant.lt).append(",").append(resMerchant.lg);
        merchant.location = location.toString();
        StringBuffer workBuffer = new StringBuffer();
        for (String s : resMerchant.work_status_mark) {
            workBuffer.append(s).append(",");
        }
        merchant.work_mark = workBuffer.toString();
        return merchant;
    }

    private Double getDistance(double latitude, double longitude) {
        try {
            double lat1 = LocationService.currentLocation.latitude;
            double lng1 = LocationService.currentLocation.longitude;
            return MapUtil.DistanceUtil.distance(lat1, lng1, latitude, longitude);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
