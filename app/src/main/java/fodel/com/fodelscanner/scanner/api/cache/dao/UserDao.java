package fodel.com.fodelscanner.scanner.api.cache.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

import fodel.com.fodelscanner.scanner.api.cache.bean.User;
import fodel.com.fodelscanner.scanner.api.cache.database.DatabaseHelper;

/**
 * Created by fula on 2017/7/24.
 */

public class UserDao {

    private Dao<User, String> userDao;

    public UserDao(Context context) {
        this.userDao = DatabaseHelper.getInstance(context).getBaseDao(User.class);
    }

    public User insert(User user) {
        try {
            return userDao.createIfNotExists(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User query() {
        try {
            QueryBuilder<User, String> queryBuilder = userDao.queryBuilder();
            queryBuilder.where().eq("is_login", true);
            return queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao.CreateOrUpdateStatus insertOrUpdate(User user) {
        try {
            return userDao.createOrUpdate(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int updateSate() {
        UpdateBuilder updateBuilder = userDao.updateBuilder();
        try {
            updateBuilder.updateColumnValue("is_login", false).where().eq("is_login", true);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
