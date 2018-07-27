package fodel.com.fodelscanner.scanner.api.exception;

/**
 * Created by Happiness on 2017/2/17.
 */
public class ServerException extends RuntimeException {

    public int code;
    public String message;

    public ServerException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
