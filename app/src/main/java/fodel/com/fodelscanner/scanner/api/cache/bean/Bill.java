package fodel.com.fodelscanner.scanner.api.cache.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by fula on 2017/7/13.
 */
@DatabaseTable(tableName = "tb_bill")
public class Bill {
    @DatabaseField(columnName = "awb", id = true)
    public String awb;
    @DatabaseField(columnName = "shop_name")
    public String shop_name;
    @DatabaseField(columnName = "e_name")
    public String e_name;
    @DatabaseField(columnName = "site_id")
    public String site_id;
    @DatabaseField(columnName = "state")
    public int state;
    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "create_time")
    public String create_time;

    public Bill() {

    }
}
