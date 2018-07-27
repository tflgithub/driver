package fodel.com.fodelscanner.scanner.api.repository;

/**
 * Created by fula on 2017/7/28.
 */

public interface DataListener<T> {

    void onSuccess(T data);

    void onFailure(String msg);
}
