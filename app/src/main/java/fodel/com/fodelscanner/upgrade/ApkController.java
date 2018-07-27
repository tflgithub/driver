package fodel.com.fodelscanner.upgrade;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

/**
 * Created by Happiness on 2017/4/27.
 */

public class ApkController {

    /****表示安装时以更新方式安装，即app不存在时安装，否则进行卸载再安装****/
    private static final String TAG = "ApkController";

    public ApkController() {
    }

    public static ApkController instance;

    public static ApkController getDefault() {
        if (instance == null) {
            synchronized (ApkController.class) {
                if (instance == null) {
                    instance = new ApkController();
                }
            }
        }
        return instance;
    }

    public void install(File file, Context context) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
        try {
            String[] command = {"chmod", "777", file.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
