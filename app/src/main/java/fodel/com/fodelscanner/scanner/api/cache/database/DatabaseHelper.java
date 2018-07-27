package fodel.com.fodelscanner.scanner.api.cache.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.sql.SQLException;

import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.scanner.api.cache.bean.Bill;
import fodel.com.fodelscanner.scanner.api.cache.bean.Merchant;
import fodel.com.fodelscanner.scanner.api.cache.bean.User;
import fodel.com.fodelscanner.utils.SDCardUtils;
import fodel.com.fodelscanner.utils.SPUtils;


/**
 * Created by fula on 2017/3/29.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "operator.db";

    private static final int DATABASE_VERSION = 2;//更新时候只需要修改这里就可以了

    private static volatile DatabaseHelper instance;

    public static final String DATABASE_PATH = SDCardUtils.getExternalCacheDir(MyApplication.getApplication().getApplicationContext()) + File.separator +
            SPUtils.get(MyApplication.getApplication().getApplicationContext(), Constants.USER_NAME, "")
            + File.separator + DATABASE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_PATH, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Bill.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Merchant.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            //所有的表都必须得删除的， 否则在onCreate方法中会出现建表异常
            TableUtils.dropTable(connectionSource, Bill.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Merchant.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单例获取OpenHelper实例
     *
     * @param context application context
     * @return instance
     */
    public static DatabaseHelper getInstance(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void close() {
        super.close();
        DaoManager.clearCache();
    }

    public <D extends Dao<T, ?>, T> D getBaseDao(Class<T> clazz) {
        try {
            return getDao(clazz);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
