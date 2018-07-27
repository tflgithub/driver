package fodel.com.fodelscanner;

/**
 * Created by Administrator on 2016/4/18.
 */
public class Constants {

    /**
     * 登录token
     */
    public static final String TOKEN = "token";
    /**
     * 登录用户名
     */
    public static final String USER_NAME = "username";

    /**
     * 密码
     */
    public static final String PASS_WORD = "password";

    /**
     * 用户头像图片地址
     */
    public static final String PORTRAIT = "portrait";

    public static final String FIRST_NAME = "first_name";

    public static final String LAST_NAME = "last_name";

    public static final int SCAN_BAR_OR_QR_CODE_REQUEST = 101;//扫描条形码/二维码

    public static final int SCAN_ORC_REQUEST = 102;//文字识别扫描

    public static final int ENABLE_BLUETOOTH_REQUEST = 103;//打开蓝牙

    public static final String NEW_TASK = "new_task";

    public static final String COMMOM_MESSAGE = "common_message";

    public static final String POP = "pop";

    public static final String PICKUP_AT_WAREHOUSE = "1";//在仓库收取派件
    public static final String DROP_AT_SHOP = "2";//在商店交接派件
    public static final String PICKUP_RETURN_AT_SHOP = "3";//在商店收取退货
    public static final String DROP_RETURN_AT_WAREHOUSE = "4";//在仓库交接退货
    public static final String COD_COLLECTION = "5";//商店收款
    public static final String PICKUP_AT_INTERNAL_WAREHOUSE = "6";//collect parcel from internal warehouse
    public static final String DROP_AT_INTERNAL_WAREHOUSE = "7";//drop parcel to internal warehouse
    public static final String REST_SERVER_DATA = "100";
    public static final String CREATE_RTO_DATA = "200";

    public static int IS_NOT_VAIL = 0;
    public static int IS_VAILED = 1;

    public static int WITHIN_10_KM = 10;
}
