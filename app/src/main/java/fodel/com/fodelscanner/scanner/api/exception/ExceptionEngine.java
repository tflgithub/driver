package fodel.com.fodelscanner.scanner.api.exception;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by fula on 2017/2/17.
 */
public class ExceptionEngine {

    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int TOKEN_EXPIRED = 406;
    public static final int HTTP_STATUS_412 = 412;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case HTTP_STATUS_412:
                    ResponseBody body = ((HttpException) e).response().errorBody();
                    try {
                        ex = new ApiException(body.string());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case UNAUTHORIZED:
                    break;
                case TOKEN_EXPIRED:
                    ex.message="The request is invalidated and logged in again";
                    break;
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "System error";  //服务器错误
                    break;
            }
            return ex;

        } else if (e instanceof SocketTimeoutException) {
            SocketTimeoutException socketTimeoutException = (SocketTimeoutException) e;
            ex = new ApiException(socketTimeoutException, ERROR.REQUEST_TIME_OUT);
            ex.message = "Network slow, please try again later";
            return ex;
        } else if (e instanceof ServerException) {    //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if ((e instanceof com.alibaba.fastjson.JSONException)
                || (e instanceof ClassCastException)) {
            ex = new ApiException(e, ERROR.PARSE_ERROR);
            ex.message = "Parse error";            //解析错误
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ERROR.NETWORD_ERROR);
            ex.message = "Connection failed";  //网络错误
            return ex;
        } else {
            ex = new ApiException(e, ERROR.UNKNOWN);
           // ex.message=e.getMessage()+"  info:" +e.toString();
            ex.message = "Unknown  error";          //未知错误
            return ex;
        }
    }

    /**
     * Agreed exceptions
     */

    public class ERROR {

        public static final int UNKNOWN = 1000;

        public static final int PARSE_ERROR = 1001;

        public static final int NETWORD_ERROR = 1002;

        public static final int HTTP_ERROR = 1003;

        public static final int REQUEST_TIME_OUT = 1004;
    }
}
