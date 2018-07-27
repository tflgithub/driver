package fodel.com.fodelscanner.scanner.api;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.BuildConfig;
import fodel.com.fodelscanner.scanner.api.entity.response.ResAuth;
import fodel.com.fodelscanner.scanner.api.entity.response.ResCodCollection;
import fodel.com.fodelscanner.scanner.api.entity.response.ResHistory;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMenu;
import fodel.com.fodelscanner.scanner.api.entity.response.ResMerchant;
import fodel.com.fodelscanner.scanner.api.entity.response.ResNotification;
import fodel.com.fodelscanner.scanner.api.entity.response.ResPortrait;
import fodel.com.fodelscanner.scanner.api.entity.response.ResReceived;
import fodel.com.fodelscanner.scanner.api.entity.response.ResUser;
import fodel.com.fodelscanner.scanner.base.BaseApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by fula on 2017/7/13.
 */

public class UserApi extends BaseApi {

    private UserService userService;

    public UserApi(OkHttpClient okHttpClient) {
        super(okHttpClient, BuildConfig.API_URL);
        userService = retrofit.create(UserService.class);
    }

    public Observable<ResAuth> login(String userName, String password, String cid) {
        paramMap.put("username", userName);
        paramMap.put("password", password);
        paramMap.put("cid", cid);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new JSONObject(getParamMap()).toString());
        return doHttp(userService.auth(requestBody));
    }

    public Observable<ResUser> getUserInfo() {
        return doHttp(userService.getUserInfo());
    }

    //show menu
    public Observable<List<ResMenu>> getMenu() {
        return doHttp(userService.getMenu());
    }

    public Observable<List<ResMerchant>> getMerchants() {
        return doHttp(userService.getMerchants());
    }

    //cod list
    public Observable<ResCodCollection> getCollected(String stationId) {
        paramMap.put("station_id", stationId);
        return doHttp(userService.getCollected(paramMap));
    }

    public Observable<ResCodCollection> generate(String stationId) {
        return doHttp(userService.generate(parseJsonBody("station_id", stationId)));
    }

    public Observable verify(List<String> bill_nos) {
        return doHttp(userService.codVerify(parseJsonBody("bill_nos", bill_nos)));
    }

    public Observable<ResReceived> received(List<String> bill_nos) {
        return doHttp(userService.codReceived(parseJsonBody("bill_nos", bill_nos)));
    }

    // get E-commerce
//    public Observable<List<ResEcommerce>> getEcommerces(String type) {
//        paramMap.put("type", type);
//        return doHttp(userService.getEcommerce(paramMap));
//    }

    public Observable addExtraInvoice(String number) {
        paramMap.put("number", number);
        return doHttp(userService.addExtraInvoice(paramMap));
    }

    public Observable<ResNotification> getNotification(String page) {
        paramMap.put("page", page);
        return doHttp(userService.getNotification(paramMap));
    }

    public Observable<ResPortrait> editPortrait(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        return doHttp(userService.editPortrait(body));
    }

    public Observable<List<ResMerchant>> getMoreMerchant(String name, String page) {
        paramMap.put("station_name", name);
        paramMap.put("page", page);
        return doHttp(userService.getMoreMerchant(paramMap));
    }

    public Observable<ResHistory> getHistory(String startDate, String endDate, String type) {
        paramMap.put("start_date", startDate);
        paramMap.put("end_date", endDate);
        paramMap.put("type", type);
        return doHttp(userService.getHistory(paramMap));
    }

}
