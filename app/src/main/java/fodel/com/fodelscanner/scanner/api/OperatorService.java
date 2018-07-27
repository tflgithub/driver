package fodel.com.fodelscanner.scanner.api;

import fodel.com.fodelscanner.scanner.api.entity.response.BaseEntity;
import fodel.com.fodelscanner.scanner.api.entity.response.ResBill;
import fodel.com.fodelscanner.scanner.api.entity.response.ResFinalize;
import fodel.com.fodelscanner.scanner.api.entity.response.ResInvoice;
import fodel.com.fodelscanner.scanner.api.entity.response.ResScanBean;
import fodel.com.fodelscanner.scanner.api.entity.response.ResSearchResult;
import fodel.com.fodelscanner.scanner.api.entity.response.ResSignature;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by fula on 2017/7/13.
 */

public interface OperatorService {

    @GET("/api/v0.2/driver/collectionpoint/handover")
    Observable<BaseEntity<ResScanBean>> getCollection(@Query("id") String id);

    @POST("/api/v0.2/driver/collectionpoint/handover")
    Observable<BaseEntity<ResBill>> getCollection(@Body RequestBody param);

    @POST("/api/v0.2/driver/collectionpoint/handover/verify")
    Observable<BaseEntity<ResFinalize>> verifyCollection(@Body RequestBody param);

    @POST("/api/v0.2/driver/collectionpoint/handover/finalize")
    Observable<BaseEntity<ResFinalize>> finalizeCollection(@Body RequestBody param);

    @POST("/api/v0.2/driver/signature/collectionpoint/handover")
    Observable<BaseEntity<ResSignature>> signatureCollection(@Body RequestBody param);

    @GET("/api/v0.2/driver/collectionpoint/rto")
    Observable<BaseEntity<ResScanBean>> getRto(@Query("id") String id);

    @POST("/api/v0.2/driver/collectionpoint/rto")
    Observable<BaseEntity<ResBill>> getRto(@Body RequestBody param);

    @POST("/api/v0.2/driver/collectionpoint/rto/verify")
    Observable<BaseEntity<ResFinalize>> verifyRto(@Body RequestBody param);

    @POST("/api/v0.2/driver/collectionpoint/rto/finalize")
    Observable<BaseEntity<ResFinalize>> finalizeRto(@Body RequestBody param);

    @POST("/api/v0.2/driver/signature/collectionpoint/rto")
    Observable<BaseEntity<ResSignature>> signatureRto(@Body RequestBody param);

    @GET("/api/v0.2/driver/client/rto")
    Observable<BaseEntity<ResScanBean>> getWarehouse(@Query("id") String id);

    @POST("/api/v0.2/driver/client/rto")
    Observable<BaseEntity<ResBill>> getWarehouse(@Body RequestBody param);

    @POST("/api/v0.2/driver/client/rto/verify")
    Observable<BaseEntity<ResFinalize>> verifyWarehouse(@Body RequestBody param);

    @POST("/api/v0.2/driver/client/rto/finalize")
    Observable<BaseEntity<ResFinalize>> finalizeWarehouse(@Body RequestBody param);

    @POST("/api/v0.2/driver/signature/client/rto")
    Observable<BaseEntity<ResSignature>> signatureWarehouse(@Body RequestBody param);

    @GET("/api/v0.2/driver/client/pickup")
    Observable<BaseEntity<ResScanBean>> getPickup(@Query("id") String id);

    @POST("/api/v0.2/driver/client/pickup")
    Observable<BaseEntity<ResBill>> getPickup(@Body RequestBody param);

    @POST("/api/v0.2/driver/client/pickup/verify")
    Observable<BaseEntity<ResFinalize>> verifyPickup(@Body RequestBody param);

    @POST("/api/v0.2/driver/client/pickup/finalize")
    Observable<BaseEntity<ResFinalize>> finalizePickup(@Body RequestBody param);

    @POST("/api/v0.2/driver/signature/client/pickup")
    Observable<BaseEntity<ResSignature>> signaturePickup(@Body RequestBody param);

    @POST("/training/resetAll")
    Observable<BaseEntity<Object>> resetAllDataForTraining();

    @POST("/training/createRto")
    Observable<BaseEntity<Object>> createRtoDataFroTraning();

    @POST("/api/v0.2/driver/internalwarehouse/handover")
    Observable<BaseEntity<ResBill>> internalWarehouseCheckIn(@Body RequestBody param);

    @GET("/api/v0.2/driver/internalwarehouse/handover")
    Observable<BaseEntity<ResScanBean>> internalWarehouse();

    @GET("/api/v0.2/driver/internalwarehouse/pickup")
    Observable<BaseEntity<ResScanBean>> internalWarehousePickup();

    @POST("/api/v0.2/driver/internalwarehouse/pickup")
    Observable<BaseEntity<ResBill>> internalWarehousePickup(@Body RequestBody param);

    @POST("/api/v0.2/driver/internalwarehouse/handover/verify")
    Observable<BaseEntity<ResFinalize>> internalWarehouseCheckInVerify(@Body RequestBody param);

    @POST("/api/v0.2/driver/internalwarehouse/pickup/verify")
    Observable<BaseEntity<ResFinalize>> internalWarehousePickupVerify(@Body RequestBody param);

    @POST("/api/v0.2/driver/internalwarehouse/handover/finalize")
    Observable<BaseEntity<ResFinalize>> internalWarehouseCheckInFinalize(@Body RequestBody param);

    @POST("/api/v0.2/driver/internalwarehouse/pickup/finalize")
    Observable<BaseEntity<ResFinalize>> internalWarehousePickupFinalize(@Body RequestBody param);

    @POST("/api/v0.2/driver/internalwarehouse/pickup/signature")
    Observable<BaseEntity<ResSignature>> internalWarehousePickupSignature(@Body RequestBody param);

    @POST("/api/v0.2/driver/internalwarehouse/handover/signature")
    Observable<BaseEntity<ResSignature>> internalWarehouseCheckInSignature(@Body RequestBody param);

    @GET("/api/v0.2/driver/invoices")
    Observable<BaseEntity<ResInvoice>> getInvoices();

    @POST("/api/v0.2/driver/invoices/putToBankAccount")
    Observable<BaseEntity<Object>> putToBankAccount(@Body RequestBody param);

    @GET("/api/v0.2/driver/shipments")
    Observable<BaseEntity<ResSearchResult>> search(@Query("awb") String awb);
}
