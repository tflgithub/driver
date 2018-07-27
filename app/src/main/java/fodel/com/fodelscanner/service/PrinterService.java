package fodel.com.fodelscanner.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qs.helper.printer.BtService;
import com.qs.helper.printer.Device;
import com.qs.helper.printer.PrinterClass;

import java.util.ArrayList;
import java.util.List;

import fodel.com.fodelscanner.MyApplication;

public class PrinterService extends Service {

    private List<Device> deviceList = null;
    public static PrinterClass pl = null;// 打印机操作类
    public static boolean isConnected = false;

    public static void downloadAndPrint(String url) {
        if (isConnected) {
            Glide.with(MyApplication.getApplication().getApplicationContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (pl.getState() == PrinterClass.STATE_CONNECTED) {
                        pl.write(new byte[]{0x1F, 0x1B, 0x1F, (byte) 0x80,
                                0x04, 0x05, 0x06, 0x44});
                        pl.write(draw2PxPoint(zoomImg(resource, 570, 280)));
                        //黑标走纸
                        pl.write(new byte[]{0x1d, 0x0c});
                    }
                }
            });
        }
    }

    public static final int MESSAGE_STATE_CHANGE = 1;


    private Handler mHandler1 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:// 蓝牙连接状态
                    switch (msg.arg1) {
                        case PrinterClass.STATE_CONNECTED:// 已经连接
                            break;
                        case PrinterClass.STATE_CONNECTING:// 正在连接
                            Toast.makeText(getApplicationContext(),
                                    "Printer connecting...", Toast.LENGTH_SHORT).show();
                            break;
                        case PrinterClass.STATE_LISTEN:
                        case PrinterClass.STATE_NONE:
                            break;
                        case PrinterClass.SUCCESS_CONNECT://连接成功
                            pl.write(new byte[]{0x1b, 0x2b});// 检测打印机型号
                            Toast.makeText(getApplicationContext(),
                                    "Printer success connect", Toast.LENGTH_SHORT).show();
                            break;
                        case PrinterClass.FAILED_CONNECT://连接失败
                            Toast.makeText(getApplicationContext(),
                                    "Printer failed connect", Toast.LENGTH_SHORT).show();
                            break;
                        case PrinterClass.LOSE_CONNECT://连接丢失
                            Toast.makeText(getApplicationContext(), "Printer lose connect",
                                    Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:// 获取蓝牙端口号
                    Device d = (Device) msg.obj;
                    if (d != null) {
                        deviceList.add(d);
                    }
                    break;
            }
        }
    };

    // 缩放图片
    private static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 把一张Bitmap图片转化为打印机可以打印的字节流
     *
     * @param bmp
     * @return
     */
    private static byte[] draw2PxPoint(Bitmap bmp) {
        // 用来存储转换后的 bitmap 数据。为什么要再加1000，这是为了应对当图片高度无法
        // 整除24时的情况。比如bitmap 分辨率为 240 * 250，占用 7500 byte，
        // 但是实际上要存储11行数据，每一行需要 24 * 240 / 8 =720byte 的空间。再加上一些指令存储的开销，
        // 所以多申请 1000byte 的空间是稳妥的，不然运行时会抛出数组访问越界的异常。
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] data = new byte[size];
        int k = 0;
        // 设置行距为0的指令
        data[k++] = 0x1B;
        data[k++] = 0x33;
        data[k++] = 0x00;
        // 逐行打印
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            // 打印图片的指令
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33;
            data[k++] = (byte) (bmp.getWidth() % 256); // nL
            data[k++] = (byte) (bmp.getWidth() / 256); // nH
            // 对于每一行，逐列打印
            for (int i = 0; i < bmp.getWidth(); i++) {
                // 每一列24个像素点，分为3个字节存储
                for (int m = 0; m < 3; m++) {
                    // 每个字节表示8个像素点，0表示白色，1表示黑色
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            data[k++] = 10;// 换行
        }
        return data;
    }

    /**
     * 灰度图片黑白化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    public static byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
            int blue = pixel & 0x000000ff; // 取低两位
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    /**
     * 图片灰度的转化
     */
    private static int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // 灰度转化公式
        return gray;
    }

    @Override
    public void onCreate() {
        pl = new BtService(this, mHandler1, mHandler2);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        deviceList = new ArrayList<>();
        scanAndConnectionPrinter();
        return START_STICKY;
    }

    private void scanAndConnectionPrinter() {
        Log.e("bluetooth:", pl.getState() + "");
        if (pl.getState() != PrinterClass.STATE_CONNECTED) {
            isConnected = false;
            if (pl != null) {
                pl.scan();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!isConnected) {
                            deviceList.addAll(pl.getDeviceList());
                            Log.e("bluetooth", "扫描到" + deviceList.size() + "个设备");
                            if (deviceList != null) {
                                for (int i = 0; i < deviceList.size(); i++) {
                                    Log.e("bluetooth", deviceList.get(i).deviceName);
                                    if (deviceList.get(i).deviceName.equals("Qsprinter")) {
                                        pl.connect(deviceList.get(i).deviceAddress);
                                        Log.e("bluetooth", "开始连接设备...");
                                        mHandler1.obtainMessage(1, 2, 0).sendToTarget();
                                        isConnected = true;
                                        break;
                                    }
                                }
                            }
                            SystemClock.sleep(3000);
                        }
                    }
                }).start();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
