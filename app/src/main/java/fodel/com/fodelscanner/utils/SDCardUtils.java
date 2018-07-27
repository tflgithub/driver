package fodel.com.fodelscanner.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * Created by tfl on 2016/4/1.
 * <p/>
 * sdcard相关辅助类
 */
public class SDCardUtils {

    private SDCardUtils() {
        /** cannot be instantiated **/
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    public static boolean isExternalStorageRemovable() {
        if (AndroidVersionCheckUtils.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * 获得外部应用程序缓存目录
     *
     * @param context 上下文信息
     * @return 外部缓存目录
     */
    public static File getExternalCacheDir(Context context) {
        if (AndroidVersionCheckUtils.hasFroyo()) {
            return context.getExternalCacheDir();
        }
        final String cacheDir = "/Android/data/" + context.getPackageName()
                + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath()
                + cacheDir);
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {

        // 检查是否安装或存储媒体是内置的,如果是这样,试着使用
        // 外部缓存 目录
        // 否则使用内部缓存 目录

        String cachePath = context.getCacheDir().getPath();
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState()) || !isExternalStorageRemovable()) {
                cachePath = getExternalCacheDir(context).getPath();
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DiskCacheDir", e.getMessage() + "");
        }


        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String filePath) {
        if (filePath == null || filePath.equals("")) {
            return;
        }
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
    }
}
