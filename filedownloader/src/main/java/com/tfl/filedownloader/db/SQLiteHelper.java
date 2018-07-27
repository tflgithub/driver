package com.tfl.filedownloader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Happiness on 2017/5/9.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME = "file_down_loader";
    private static SQLiteDatabase.CursorFactory mFactory = null;
    private static final int mVersion = 1;
    public static final String TABLE_NAME = "down_load_info"; //文件下载信息数据表名称


    public SQLiteHelper(Context context) {
        super(context, DATA_BASE_NAME, mFactory, mVersion);
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建文件下载信息数据表
        String downLoadSql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
                + "userID VARCHAR, "
                + "taskID VARCHAR, "
                + "url VARCHAR, "
                + "filePath VARCHAR, "
                + "fileName VARCHAR, "
                + "fileSize VARCHAR, "
                + "downLoadSize VARCHAR "
                + ")";
        db.execSQL(downLoadSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
