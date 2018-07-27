package fodel.com.fodelscanner.upgrade;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.tfl.filedownloader.DownLoadService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tfl on 2016/10/24.
 */
public class CheckUpdateTask extends AsyncTask<String, Void, String> {

    private Context mContext;

    public CheckUpdateTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result)) {
            parseJson(result);
        }
    }

    private void parseJson(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            String apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
            int apkCode = obj.getInt(Constants.APK_VERSION_CODE);
            String content = obj.getString(Constants.APK_UPDATE_CONTENT);
            int versionCode = getVersionCode(mContext);
            int forceUpdate = obj.getInt(Constants.FORCE_UPDATE);
            if (apkCode > versionCode) {
                new UpdateDialog(DownLoadService.getDownLoadManager(), mContext, apkUrl, content, forceUpdate).show();
            }
        } catch (JSONException e) {
            Log.e(Constants.TAG, "parse json error");
        }
    }

    private int getVersionCode(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return 0;
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpUtils.get(params[0]);
    }
}
