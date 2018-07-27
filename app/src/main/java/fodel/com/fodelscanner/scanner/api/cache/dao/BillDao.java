package fodel.com.fodelscanner.scanner.api.cache.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.scanner.api.cache.bean.Bill;
import fodel.com.fodelscanner.scanner.api.cache.database.DatabaseHelper;

/**
 * Created by fula on 2017/7/13.
 */

public class BillDao {

    private Dao<Bill, String> billDao;

    public BillDao(Context context) {
        this.billDao = DatabaseHelper.getInstance(context).getBaseDao(Bill.class);
    }

    public Bill insert(Bill collectionPoint) {
        try {
            return billDao.createIfNotExists(collectionPoint);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void delete(int state, String type) {
        try {
            DeleteBuilder deleteBuilder = billDao.deleteBuilder();
            deleteBuilder.where().eq("state", state).and().eq("type", type);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteAll()
    {
        try {
            billDao.delete(billDao.queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int update(String type, String name) {
        try {
            UpdateBuilder updateBuilder = (UpdateBuilder) billDao.updateBuilder().updateColumnValue("state", Constants.IS_VAILED);
            updateBuilder.where().eq("type", type).and().eq("e_name", name);
            return updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public List<Bill> queryAll(String type, String name) {
        QueryBuilder<Bill, String> queryBuilder = billDao.queryBuilder();
        try {
            queryBuilder.where().eq("type", type).and().eq("e_name", name);
            queryBuilder.orderBy("create_time", false);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bill query(String awb, String type, String name) {
        try {
            QueryBuilder<Bill, String> queryBuilder = billDao.queryBuilder();
            queryBuilder.where().eq("awb", awb).and().eq("type", type).and().eq("e_name", name);
            return queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
