package fodel.com.fodelscanner.scanner.api.exception;

/**
 * Created by Happiness on 2017/2/17.
 */
public class ApiException extends Exception {

    public int code;
    public String message;
    public String response;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ApiException(String response) {
        this.response = response;
    }
}
