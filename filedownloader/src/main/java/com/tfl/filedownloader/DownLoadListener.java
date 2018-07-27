package com.tfl.filedownloader;

import com.tfl.filedownloader.db.SQLDownLoadInfo;

/**
 * Created by Happiness on 2017/5/9.
 */

public interface DownLoadListener {

    /**
     * (开始下载文件)
     * @param sqlDownLoadInfo 下载任务对象
     */
    public void onStart(SQLDownLoadInfo sqlDownLoadInfo);

    /**
     * (文件下载进度情况)
     * @param sqlDownLoadInfo 下载任务对象
     * @param isSupportBreakpoint 服务器是否支持断点续传
     */
    public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint);

    /**
     * (停止下载完毕)
     * @param sqlDownLoadInfo 下载任务对象
     * @param isSupportBreakpoint 服务器是否支持断点续传
     */
    public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint);

    /**
     * (文件下载失败)
     * @param sqlDownLoadInfo 下载任务对象
     */
    public void onError(SQLDownLoadInfo sqlDownLoadInfo);


    /**
     * (文件下载成功)
     * @param sqlDownLoadInfo 下载任务对象
     */
    public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo);
}
