package fodel.com.fodelscanner.scanner.api.cache.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import fodel.com.fodelscanner.scanner.api.cache.bean.Merchant;
import fodel.com.fodelscanner.scanner.api.cache.database.DatabaseHelper;

/**
 * Created by fula on 2017/7/13.
 */

public class MerchantDao {

    private Dao<Merchant, String> merchantDao;

    public MerchantDao(Context context) {
        this.merchantDao = DatabaseHelper.getInstance(context).getBaseDao(Merchant.class);
    }

    public Merchant insert(Merchant merchant) {
        try {
            return merchantDao.createIfNotExists(merchant);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Merchant> selectAll() {
        try {
            QueryBuilder queryBuilder = merchantDao.queryBuilder().orderBy("distance", true);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int deleteAll() {
        try {
            return merchantDao.delete(selectAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
