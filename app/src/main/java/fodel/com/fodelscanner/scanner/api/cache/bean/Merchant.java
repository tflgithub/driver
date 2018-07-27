package fodel.com.fodelscanner.scanner.api.cache.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by fula on 2017/7/27.
 */
@DatabaseTable(tableName = "tb_merchant")
public class Merchant {

    @DatabaseField(columnName = "site_id", id = true)
    public String site_id;
    @DatabaseField(columnName = "station_id")
    public String station_id;
    @DatabaseField(columnName = "station_name")
    public String station_name;
    @DatabaseField(columnName = "address")
    public String address;
    @DatabaseField(columnName = "location")
    public String location;
    @DatabaseField(columnName = "distance")
    public double distance;
    @DatabaseField(columnName = "label")
    public String label;
    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "work_mark")
    public String work_mark;
    @DatabaseField(columnName = "area")
    public String area;
    @DatabaseField(columnName = "phone_number")
    public String phone_number;
}
