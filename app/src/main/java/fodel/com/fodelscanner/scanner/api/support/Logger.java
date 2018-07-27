package fodel.com.fodelscanner.scanner.api.support;

import fodel.com.fodelscanner.utils.LogUtils;


/**
 * Created by fula on 2017/7/13.
 */

public class Logger implements LoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        LogUtils.i("http" , message);
    }
}
