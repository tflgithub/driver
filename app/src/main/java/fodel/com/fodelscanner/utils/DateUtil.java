package fodel.com.fodelscanner.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fula on 2017/7/21.
 */

public class DateUtil {

    public static String getCurrentTime() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(now);
    }
}
