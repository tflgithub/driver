package fodel.com.fodelscanner.scanner.api.exception;

import android.util.Log;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by fula on 2017/7/17.
 */

public class ExceptionFunc implements Func1<Throwable, Observable> {
    @Override
    public Observable call(Throwable throwable) {
        Log.e("ExceptionFunc", "-------->" + throwable.getMessage());
        return Observable.error(throwable);
    }
}
