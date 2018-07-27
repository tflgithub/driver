package fodel.com.fodelscanner.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;
import java.util.Map;

import fodel.com.fodelscanner.IProcessService;
import fodel.com.fodelscanner.broadcast.AlarmReceiver;

/**
 * Created by tfl on 2016/3/15.
 */
public class LocationService extends Service {

    private BdLocationService bdLocationService;

    private LocationBinder mLocalBinder;

    private String TAG = "LocationService";

    private LocationServiceConnection mLocationServiceConnection;

    public static LatLng currentLocation;

    AlarmManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocalBinder = new LocationBinder();
        if (mLocationServiceConnection == null) {
            mLocationServiceConnection = new LocationServiceConnection();
        }
    }


    /**
     * 通过AIDL实现进程间通信
     */
    class LocationBinder extends IProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "LocationService";
        }
    }

    /**
     * 连接远程服务
     */
    class LocationServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                // 与远程服务通信
                IProcessService process = IProcessService.Stub.asInterface(service);
                Log.i(TAG, "连接" + process.getServiceName() + "服务成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // RemoteException连接过程出现的异常，才会回调,unbind不会回调
            // 监测，远程服务已经死掉，则重启远程服务
            Log.i(TAG, "远程服务被杀死");
            // 启动远程服务
            startService(new Intent(LocationService.this, RemoteService.class));
            // 绑定远程服务
            bindService(new Intent(LocationService.this, RemoteService.class), mLocationServiceConnection, Context.BIND_IMPORTANT);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, RemoteService.class), mLocationServiceConnection, Context.BIND_IMPORTANT);
        bdLocationService = new BdLocationService(this);
        bdLocationService.registerListener(mListener);
        bdLocationService.start();

        //Wake up every 5 minutes
        long intervalMillis = 5 * 60 * 1000;
        Intent intentReceiver = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentReceiver, 0);
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, intervalMillis, pendingIntent);
        return START_STICKY;
    }

    private Map<String, LatLng> bufferMap;
    private static int maxSize = 10;

    private void handlerLocation(double longitude, double latitude) {
        String location = longitude + "," + latitude;
        if (bufferMap == null) {
            bufferMap = new LinkedHashMap<String, LatLng>() {
                @Override
                protected boolean removeEldestEntry(Entry<String, LatLng> eldest) {
                    return size() > maxSize;
                }
            };
            bufferMap.put(location, new LatLng(latitude, longitude));
        } else if (bufferMap.get(location) == null) {
            bufferMap.put(location, new LatLng(latitude, longitude));
        }
        currentLocation = bufferMap.get(location);
        Log.i(TAG, "获取经纬度：" + currentLocation.latitude + "," + currentLocation.longitude);
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            handlerLocation(bdLocation.getLongitude(), bdLocation.getLatitude());
        }
    };
}
