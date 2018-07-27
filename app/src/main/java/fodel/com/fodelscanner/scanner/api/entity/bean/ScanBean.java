package fodel.com.fodelscanner.scanner.api.entity.bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fula on 2017/7/24.
 */

public class ScanBean {

    public String site_id;

    public String shop_name;

    public LinkedList<String> order_list = new LinkedList<>();
}
