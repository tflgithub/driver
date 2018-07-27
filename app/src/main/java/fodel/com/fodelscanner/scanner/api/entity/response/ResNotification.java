package fodel.com.fodelscanner.scanner.api.entity.response;

import java.util.ArrayList;

/**
 * Created by fula on 2017/7/24.
 */

public class ResNotification {

    public int total;

    public ArrayList<NotificationItem> list;

    public static class NotificationItem {

        public String title;

        public String content;

        public String create_time;
    }
}
