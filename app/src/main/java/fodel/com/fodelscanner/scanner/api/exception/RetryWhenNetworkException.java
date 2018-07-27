package fodel.com.fodelscanner.scanner.api.exception;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Happiness on 2017/4/19.
 */

public class RetryWhenNetworkException implements Func1<Observable<? extends Throwable>, Observable<?>> {
    /* retry次数*/
    private int count = 1;
    /*延迟*/
    private long delay = 100;
    /*叠加延迟*/
    private long increaseDelay = 100;

    public RetryWhenNetworkException() {

    }

    public RetryWhenNetworkException(int count, long delay) {
        this(count, delay, 0);
    }

    public RetryWhenNetworkException(int count, long delay, long increaseDelay) {
        this.count = count;
        this.delay = delay;
        this.increaseDelay = increaseDelay;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable.zipWith(observable.range(1, count + 1), new Func2<Throwable, Integer, Wrapper>() {
            @Override
            public Wrapper call(Throwable throwable, Integer integer) {
                return new Wrapper(throwable, integer);
            }
        }).flatMap(new Func1<Wrapper, Observable<?>>() {
            @Override
            public Observable<?> call(Wrapper wrapper) {
                if ((wrapper.throwable instanceof ConnectException)
                        || (wrapper.throwable instanceof SocketTimeoutException)
                        || (wrapper.throwable instanceof TimeoutException) && wrapper.index < count + 1) {
                    return Observable.timer(delay + (wrapper.index - 1) * increaseDelay, TimeUnit.MILLISECONDS);
                }
                return Observable.error(wrapper.throwable);
            }
        });
    }

    private class Wrapper {
        private int index;
        private Throwable throwable;

        public Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }
}
