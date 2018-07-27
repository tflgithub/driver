package fodel.com.fodelscanner.scanner.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fodel.com.fodelscanner.scanner.api.entity.response.BaseEntity;
import fodel.com.fodelscanner.scanner.api.entity.response.ResAuth;
import fodel.com.fodelscanner.scanner.api.entity.response.ResCod;
import fodel.com.fodelscanner.scanner.api.entity.response.ResCodCollection;
import fodel.com.fodelscanner.scanner.api.entity.response.ResCodInfo;
import fodel.com.fodelscanner.scanner.api.entity.response.ResEcommerce;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMerchant;
import fodel.com.fodelscanner.scanner.api.entity.response.ResNotification;
import fodel.com.fodelscanner.scanner.api.entity.response.ResPortrait;
import fodel.com.fodelscanner.scanner.api.entity.response.ResReceived;
import fodel.com.fodelscanner.scanner.api.entity.response.ResUser;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by fula on 2017/7/20.
 */

public interface UserService {

    @POST("/api/v0.2/auth")
    Observable<BaseEntity<ResAuth>> auth(@Body RequestBody param);

    @GET("/api/v0.2/user")
    Observable<BaseEntity<ResUser>> getUserInfo();

    @GET("/api/v0.2/driver/summary")
    Observable<BaseEntity<List<ResMenu>>> getMenu();

    @GET("/api/v0.2/driver/merchant/action")
    Observable<BaseEntity<List<ResMerchant>>> getMerchants();

    @GET("/api/v0.2/driver/invoices/uncollected_list")
    Observable<BaseEntity<ResCodCollection>> getCollected(@QueryMap Map<String, Object> map);

    @POST("/api/v0.2/driver/invoices/generate")
    Observable<BaseEntity<ResCodCollection>> generate(@Body RequestBody param);

    @POST("/api/v0.2/driver/invoices/verify")
    Observable<BaseEntity<Object>> codVerify(@Body RequestBody param);

    @POST("/api/v0.2/driver/invoices/received")
    Observable<BaseEntity<ResReceived>> codReceived(@Body RequestBody param);

    @GET("/api/v0.2/driver/codMenu")
    Observable<BaseEntity<List<ResMerchant>>> getCodMenu();

    @GET("/api/v0.2/driver/codMenu/getInvoiceByStation")
    Observable<BaseEntity<List<ResCod>>> getInvoiceByStation(@QueryMap Map<String, Object> map);

    @GET("/api/v0.2/driver/codMenu/getOrderList")
    Observable<BaseEntity<List<ResCodInfo>>> getOrderList(@QueryMap Map<String, Object> map);

    @GET("/api/v0.2/driver/ecommerce/listWithBooking")
    Observable<BaseEntity<List<ResEcommerce>>> getEcommerce(@QueryMap Map<String, Object> map);

    @GET("/api/v0.2/driver/invoices/addExtraInvoice")
    Observable<BaseEntity<Object>> addExtraInvoice(@QueryMap Map<String, Object> map);

    @GET("/api/v0.2/driver/notification/list")
    Observable<BaseEntity<ResNotification>> getNotification(@QueryMap Map<String, Object> map);

    @POST("/user/editPortrait")
    @Multipart
    Observable<BaseEntity<ResPortrait>> editPortrait(@Part MultipartBody.Part file);

    @GET("/api/v0.2/driver/collectionpoint/list")
    Observable<BaseEntity<List<ResMerchant>>> getMoreMerchant(@QueryMap Map<String, Object> map);

    @GET("/api/v0.2/driver/history")
    Observable<BaseEntity<ResHistory>> getHistory(@QueryMap Map<String, Object> map);

}
