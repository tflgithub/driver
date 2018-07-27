package fodel.com.fodelscanner.scanner.api;

import java.util.List;

import fodel.com.fodelscanner.BuildConfig;
import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.scanner.api.entity.response.BaseEntity;
import fodel.com.fodelscanner.scanner.api.entity.response.ResBill;
import fodel.com.fodelscanner.scanner.api.entity.response.ResFinalize;
import fodel.com.fodelscanner.scanner.api.entity.response.ResInvoice;
import fodel.com.fodelscanner.scanner.api.entity.response.ResScanBean;
import fodel.com.fodelscanner.scanner.api.entity.response.ResSearchResult;
import fodel.com.fodelscanner.scanner.api.entity.response.ResSignature;
import fodel.com.fodelscanner.scanner.base.BaseApi;
import okhttp3.OkHttpClient;
import rx.Observable;

/**
 * Created by fula on 2017/7/13.
 */

public class OperatorApi extends BaseApi {

    private OperatorService operatorApiService;

    public OperatorApi(OkHttpClient okHttpClient) {
        super(okHttpClient, BuildConfig.API_URL);
        operatorApiService = retrofit.create(OperatorService.class);
    }

    public Observable<ResScanBean> getScan(String type, String id) {
        Observable observable = null;
        switch (type) {
            case Constants.DROP_AT_SHOP:
                observable = operatorApiService.getCollection(id);
                break;
            case Constants.PICKUP_RETURN_AT_SHOP:
                observable = operatorApiService.getRto(id);
                break;
            case Constants.DROP_RETURN_AT_WAREHOUSE:
                observable = operatorApiService.getWarehouse(id);
                break;
            case Constants.PICKUP_AT_WAREHOUSE:
                observable = operatorApiService.getPickup(id);
                break;
            case Constants.DROP_AT_INTERNAL_WAREHOUSE:
                observable = operatorApiService.internalWarehouse();
                break;
            case Constants.PICKUP_AT_INTERNAL_WAREHOUSE:
                observable = operatorApiService.internalWarehousePickup();
                break;
        }
        return doHttp(observable);
    }


    public Observable<ResBill> scan(String awb, String type, String id) {
        Observable observable = null;
        if (type.equals(Constants.DROP_AT_SHOP)) {
            observable = operatorApiService.getCollection(parseJsonBody("awb", awb, "id", id));
        } else if (type.equals(Constants.PICKUP_RETURN_AT_SHOP)) {
            observable = operatorApiService.getRto(parseJsonBody("awb", awb, "id", id));
        } else if (type.equals(Constants.DROP_RETURN_AT_WAREHOUSE)) {
            observable = operatorApiService.getWarehouse(parseJsonBody("awb", awb, "id", id));
        } else if (type.equals(Constants.PICKUP_AT_WAREHOUSE)) {
            observable = operatorApiService.getPickup(parseJsonBody("awb", awb, "id", id));
        } else if (type.equals(Constants.PICKUP_AT_INTERNAL_WAREHOUSE)) {
            observable = operatorApiService.internalWarehousePickup(parseJsonBody("awb", awb));
        } else if (type.equals(Constants.DROP_AT_INTERNAL_WAREHOUSE)) {
            observable = operatorApiService.internalWarehouseCheckIn(parseJsonBody("awb", awb));
        }
        return doHttp(observable);
    }


    public Observable<ResFinalize> verify(List<String> awbs, String type, String id) {
        Observable observable = null;
        if (type.equals(Constants.DROP_AT_SHOP)) {
            observable = operatorApiService.verifyCollection(parseJsonBody("awbs", awbs, "id", id));
        } else if (type.equals(Constants.PICKUP_RETURN_AT_SHOP)) {
            observable = operatorApiService.verifyRto(parseJsonBody("awbs", awbs, "id", id));
        } else if (type.equals(Constants.DROP_RETURN_AT_WAREHOUSE)) {
            observable = operatorApiService.verifyWarehouse(parseJsonBody("awbs", awbs, "id", id));
        } else if (type.equals(Constants.PICKUP_AT_WAREHOUSE)) {
            observable = operatorApiService.verifyPickup(parseJsonBody("awbs", awbs, "id", id));
        } else if (type.equals(Constants.PICKUP_AT_INTERNAL_WAREHOUSE)) {
            observable = operatorApiService.internalWarehousePickupVerify(parseJsonBody("awbs", awbs, "id", id));
        } else if (type.equals(Constants.DROP_AT_INTERNAL_WAREHOUSE)) {
            observable = operatorApiService.internalWarehouseCheckInVerify(parseJsonBody("awbs", awbs, "id", id));
        }
        return doHttp(observable);
    }

    public Observable<ResFinalize> finalize(List<String> awbs, String type, String id) {
        Observable observable = null;
        switch (type) {
            case Constants.DROP_AT_SHOP:
                observable = operatorApiService.finalizeCollection(parseJsonBody("awbs", awbs, "id", id));
                break;
            case Constants.PICKUP_RETURN_AT_SHOP:
                observable = operatorApiService.finalizeRto(parseJsonBody("awbs", awbs, "id", id));
                break;
            case Constants.DROP_RETURN_AT_WAREHOUSE:
                observable = operatorApiService.finalizeWarehouse(parseJsonBody("awbs", awbs, "id", id));
                break;
            case Constants.PICKUP_AT_WAREHOUSE:
                observable = operatorApiService.finalizePickup(parseJsonBody("awbs", awbs, "id", id));
                break;
            case Constants.DROP_AT_INTERNAL_WAREHOUSE:
                observable = operatorApiService.internalWarehouseCheckInFinalize(parseJsonBody("awbs", awbs, "id", id));
                break;
            case Constants.PICKUP_AT_INTERNAL_WAREHOUSE:
                observable = operatorApiService.internalWarehousePickupFinalize(parseJsonBody("awbs", awbs, "id", id));
                break;
        }
        return doHttp(observable);
    }

    public Observable<ResSignature> signature(List<String> awbs, String type, String id) {
        Observable observable = null;
        switch (type) {
            case Constants.DROP_AT_SHOP:
                observable = operatorApiService.signatureCollection(parseJsonBody("awbs", awbs, "id", id));
                break;
            case Constants.PICKUP_RETURN_AT_SHOP:
                observable = operatorApiService.signatureRto(parseJsonBody("awbs", awbs, "id", id));
                break;
            case Constants.DROP_RETURN_AT_WAREHOUSE:
                observable = operatorApiService.signatureWarehouse(parseJsonBody("awbs", awbs, "id", id));
                break;
            case Constants.PICKUP_AT_WAREHOUSE:
                observable = operatorApiService.signaturePickup(parseJsonBody("awbs", awbs, "id", id));
                break;
        }
        return doHttp(observable);
    }

    //training clear all cache data
    public Observable<BaseEntity> resetDataForTraining() {
        return doHttp(operatorApiService.resetAllDataForTraining());
    }

    //training create rto data
    public Observable<BaseEntity> createRtoForTraining() {
        return doHttp(operatorApiService.createRtoDataFroTraning());
    }

    public Observable<ResInvoice> getInvoices() {
        return doHttp(operatorApiService.getInvoices());
    }

    public Observable putToBankAccount(List<String> bill_nos, int amount) {
        return doHttp(operatorApiService.putToBankAccount(parseJsonBody("bill_nos", bill_nos, "amount", amount)));
    }

    //search
    public Observable<ResSearchResult> search(String awb) {
        return doHttp(operatorApiService.search(awb));
    }
}
