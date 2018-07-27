package com.tfl.filedownloader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Happiness on 2017/5/9.
 */

public class DownLoadService extends Service {

    private static DownLoadManager downLoadManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static DownLoadManager getDownLoadManager() {
        return downLoadManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downLoadManager = new DownLoadManager(DownLoadService.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放downLoadManager
        downLoadManager.stopAllTask();
        downLoadManager = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (downLoadManager == null) {
            downLoadManager = new DownLoadManager(DownLoadService.this);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
