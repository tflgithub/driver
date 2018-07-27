package fodel.com.fodelscanner.utils;

import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import fodel.com.fodelscanner.BuildConfig;
import fodel.com.fodelscanner.MyApplication;

/**
 * Created by tfl on 2016/4/1.
 * 日志工具
 */
public class LogUtils {

    public static boolean isDebug = BuildConfig.DEBUG;
    private static final String TAG = "scanner";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void saveInfoToSD(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        String value;
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        FileOutputStream fos = null;
        String fileName = null;
        try {
            File dir = SDCardUtils.getDiskCacheDir(MyApplication.getApplication().getApplicationContext(), "error");
            if (!dir.exists()) {
                dir.mkdir();
            }
            fileName = dir.toString()
                    + File.separator
                    + SystemClock.currentThreadTimeMillis() + ".txt";
            File newFile = new File(fileName);
            fos = new FileOutputStream(newFile);
            fos.write(sb.toString().getBytes());
        } catch (FileNotFoundException fne) {
        } catch (Exception e) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
