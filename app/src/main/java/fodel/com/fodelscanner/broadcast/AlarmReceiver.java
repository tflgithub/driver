package fodel.com.fodelscanner.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.service.LocationService;

/**
 * Created by fula on 2017/7/27.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (LocationService.currentLocation != null) {
            Log.d("AlarmReceiver", "开始上传当前位置:" + LocationService.currentLocation.latitude + "," + LocationService.currentLocation.longitude);
            MyApplication.getApplication().getAppComponent().getUploadApi().uploadPosition(LocationService.currentLocation.latitude, LocationService.currentLocation.longitude);
        }
    }
}
