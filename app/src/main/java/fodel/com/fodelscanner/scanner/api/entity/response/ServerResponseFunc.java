package fodel.com.fodelscanner.scanner.api.entity.response;

import fodel.com.fodelscanner.scanner.api.exception.ServerException;
import rx.functions.Func1;

/**
 * Created by fula on 2017/7/17.
 */

public class ServerResponseFunc<T> implements Func1<BaseEntity<T>, T> {

    @Override
    public T call(BaseEntity<T> tBaseEntity) {
        if (tBaseEntity.code != 1001) {
            throw new ServerException(tBaseEntity.code, tBaseEntity.msg);
        }
        return tBaseEntity.data;
    }
}
