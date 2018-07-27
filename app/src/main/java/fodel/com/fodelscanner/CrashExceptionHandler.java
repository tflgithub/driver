package fodel.com.fodelscanner;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import fodel.com.fodelscanner.activity.SplashActivity;
import fodel.com.fodelscanner.utils.SDCardUtils;
import fodel.com.fodelscanner.utils.SPUtils;

import static android.content.Intent.*;

public class CrashExceptionHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashExceptionHandler";
    private static CrashExceptionHandler instance;
    private Context context;
    private Map<String, String> infos = new HashMap<>();

    private CrashExceptionHandler(Context context) {
        init(context);
    }

    public static CrashExceptionHandler getInstance(Context context) {
        if (instance == null) {
            instance = new CrashExceptionHandler(context);
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
    }

    private void handleException(Throwable ex) {
        collectDeviceInfo(this.context);
        saveInfoToSD(ex);
        Intent intent = new Intent(context.getApplicationContext(), SplashActivity.class);
        PendingIntent restartIntent = PendingIntent.getActivity(
                context.getApplicationContext(), 0, intent, FLAG_ACTIVITY_NEW_TASK);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                restartIntent);
        MyAcitivityManager.finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void checkCrash() {
        String crashFileName = SPUtils.get(context, "CRASH_FILE_NAME", "").toString();
        final File file = new File(crashFileName);
        if (!file.exists()) {
            return;
        }
        MyApplication.getApplication().getAppComponent().getUploadApi().uploadCashLog(file);
    }

    private void saveInfoToSD(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        String value;
        for (String key : infos.keySet()) {
            value = infos.get(key);
            sb.append(key).append("=").append(value).append("\n");
        }
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
            File dir = SDCardUtils.getDiskCacheDir(context, "crash");
            if (dir.exists()) {
                deleteDir(dir);
            }
            if (!dir.exists()) {
                dir.mkdir();
            }
            fileName = dir.toString()
                    + File.separator
                    + getAssignTime("dd_MM_yyyy HH:mm") + ".txt";
            File newFile = new File(fileName);
            fos = new FileOutputStream(newFile);
            fos.write(sb.toString().getBytes());
        } catch (FileNotFoundException fne) {
        } catch (Exception e) {
        } finally {
            SPUtils.put(context, "CRASH_FILE_NAME", fileName);
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private String getAssignTime(String dateFormatStr) {
        DateFormat dataFormat = new SimpleDateFormat(dateFormatStr);
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(currentTime);
    }

    private void deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    private void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            }
        } catch (Exception e) {
        }
    }
}
