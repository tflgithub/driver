package fodel.com.fodelscanner.upgrade;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;

import com.tfl.filedownloader.DownLoadListener;
import com.tfl.filedownloader.DownLoadManager;
import com.tfl.filedownloader.FileHelper;
import com.tfl.filedownloader.db.SQLDownLoadInfo;

import java.io.File;

import fodel.com.fodelscanner.R;

/**
 * Created by tfl on 2016/10/24.
 */
public class UpdateDialog {

    private DownLoadManager manager;

    private Context mContext;

    private String downloadUrl;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    public UpdateDialog(DownLoadManager manager, Context context, String path, String content, int upgrade) {
        this.manager = manager;
        this.mContext = context;
        this.downloadUrl = path;
        initDialog(content,upgrade);
    }

    private void initDialog(String content, int upgrade) {
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.android_auto_update_dialog_title);
        builder.setMessage(Html.fromHtml(content))
                .setPositiveButton(R.string.android_auto_update_dialog_btn_download, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        downLoader();
                    }
                });
        if (upgrade != Constants.FORCE_UPDATE_YES) {
            builder.setNegativeButton(R.string.android_auto_update_dialog_btn_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        dialog = builder.create();
        //点击对话框外面,对话框不消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    public void show() {
        if (manager.isTaskdownloading(getFileName())) {
            return;
        }
        if (dialog.isShowing()) {
            return;
        }
        dialog.show();
    }


    private static ProgressDialog mProgressDialog;


    public void downLoader() {
        int state = manager.addTask(getFileName(), downloadUrl, getFileName());
        if (state == -1) {
            File file = new File(FileHelper.getFileDefaultPath() + "/(" + FileHelper.filterIDChars(getFileName()) + ")" + getFileName());
            ApkController.getDefault().install(file, mContext);
        } else {
            manager.startTask(getFileName());
            manager.setSingleTaskListener(getFileName(), new DownLoadListener() {
                @Override
                public void onStart(SQLDownLoadInfo sqlDownLoadInfo) {
                    mProgressDialog = new ProgressDialog(mContext);
                    mProgressDialog.setTitle(mContext.getString(R.string.download_new_version));
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.show();
                    mProgressDialog.setCancelable(false);
                }

                @Override
                public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
                    mProgressDialog.dismiss();
                }

                @Override
                public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
                    mProgressDialog.dismiss();
                }

                @Override
                public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
                    int result = manager.getTaskInfo(getFileName()).getProgress();
                    mProgressDialog.setProgress(result);
                }

                @Override
                public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo) {
                    mProgressDialog.dismiss();
                    ApkController.getDefault().install(new File(sqlDownLoadInfo.getFilePath()), mContext);
                }
            });
        }
    }

    private String getFileName() {
        return downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);
    }

}
