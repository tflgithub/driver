package fodel.com.fodelscanner.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import fodel.com.fodelscanner.IProcessService;

/**
 * Created by Administrator on 2017/3/7.
 */
public class RemoteService extends Service{

    String TAG = "RemoteService";

    private ServiceBinder mServiceBinder;

    private RemoteServiceConnection mRemoteServiceConn;


    @Override
    public void onCreate() {
        super.onCreate();
        mServiceBinder = new ServiceBinder();
        if (mRemoteServiceConn == null) {
            mRemoteServiceConn = new RemoteServiceConnection();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, LocationService.class), mRemoteServiceConn, Context.BIND_IMPORTANT);
         return START_STICKY;
    }

    /**
     * 通过AIDL实现进程间通信
     */
    class ServiceBinder extends IProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "RemoteService";
        }
    }

    /**
     * 连接远程服务
     */
    class RemoteServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                // 与本地服务通信
                IProcessService process = IProcessService.Stub.asInterface(service);
                Log.i(TAG, "连接" + process.getServiceName() + "服务成功");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 监测，本地服务已经死掉，则重启本地服务
            Log.i(TAG, "本地服务被杀死");
            // 启动本地服务
            startService(new Intent(RemoteService.this, LocationService.class));

            // 绑定本地服务
            bindService(new Intent(RemoteService.this, LocationService.class), mRemoteServiceConn, Context.BIND_IMPORTANT);
        }
    }
}
