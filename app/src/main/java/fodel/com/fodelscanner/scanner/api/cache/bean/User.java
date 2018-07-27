package fodel.com.fodelscanner.scanner.api.cache.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by fula on 2017/7/24.
 */
@DatabaseTable(tableName = "tb_user")
public class User {

    @DatabaseField(columnName = "user_name", id = true)
    public String userName;
    @DatabaseField(columnName = "pass_word")
    public String passWord;
    @DatabaseField(columnName = "portrait_url")
    public String portraitUrl;
    @DatabaseField(columnName = "phone_number")
    public String phoneNumber;
    @DatabaseField(columnName = "first_name")
    public String firstName;
    @DatabaseField(columnName = "last_name")
    public String lastName;
    @DatabaseField(columnName = "is_login")
    public boolean isLoin;
    @DatabaseField(columnName = "create_time")
    public String createTime;
    @DatabaseField(columnName = "update_time")
    public String updateTime;
}
