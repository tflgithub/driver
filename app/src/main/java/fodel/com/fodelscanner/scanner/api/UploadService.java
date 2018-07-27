package fodel.com.fodelscanner.scanner.api;

import fodel.com.fodelscanner.scanner.api.entity.response.BaseEntity;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/9/13.
 */

public interface UploadService {

    @POST
    @Multipart
    Observable<BaseEntity<Object>> signatureImage(@Url String url, @Part MultipartBody.Part file);

    @POST("/log/driver")
    @Multipart
    Observable<BaseEntity<Object>> crashLog(@Part MultipartBody.Part file);

    @POST("/api/v0.2/driver/coordinates")
    Observable<BaseEntity<Object>> uploadPosition(@Body RequestBody param);
}
