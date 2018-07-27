package fodel.com.fodelscanner.scanner.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.cn.xtouch.tfl.alert.AlertView;
import com.cn.xtouch.tfl.alert.OnDismissListener;
import com.cn.xtouch.tfl.alert.OnItemClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.helper.CameraHelper;
import fodel.com.fodelscanner.utils.KeyBoardUtils;
import fodel.com.fodelscanner.utils.ParseImage;
import fodel.com.fodelscanner.utils.ToastUtils;
import fodel.com.fodelscanner.view.MaskSurfaceView;
import fodel.com.fodelscanner.view.ProcessProgressDialog;

public class CameraActivity extends BaseActivity implements CameraHelper.OnCaptureCallback, OnItemClickListener, OnDismissListener {

    @ViewInject(R.id.surface_view)
    MaskSurfaceView maskSurfaceView;
    @ViewInject(R.id.btn1)
    private Button button1;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        ViewUtils.inject(this);
        maskSurfaceView.setMaskSize(600, 200);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void initInjector() {

    }


    @Override
    public void onCapture(boolean success, String filePath) {
        if (success) {
            try {
                new ParseImageAsync().execute(filePath);
            } catch (Exception e) {
            }
        }
    }


    private AlertView mAlertViewExt;
    private EditText etName;
    private InputMethodManager imm;

    private void myAlertDialog() {
        mAlertViewExt = new AlertView("Parcel Number",
                null, null, "Cancel", null, new String[]{"Ok"}, this,
                AlertView.Style.Alert, this);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.alertext_form, null);
        etName = (EditText) extView.findViewById(R.id.etName);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                // 输入框出来则往上移动
                boolean isOpen = imm.isActive();
                mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
            }
        });
        mAlertViewExt.addExtView(extView);
        mAlertViewExt.show();
    }

    @Override
    public void onItemClick(Object o, int position) {
        // 判断是否是拓展窗口View，而且点击的是非取消按钮
        if (o == mAlertViewExt && position != AlertView.CANCELPOSITION) {
            KeyBoardUtils.closeKeybord(etName, this);
            String codeString = etName.getText().toString();
            if (codeString.isEmpty()) {
                ToastUtils.showShort(this, "Please enter a valid number");
                return;
            }
            takeOrder(codeString);
        }
    }

    private void takeOrder(String result) {
        if (result != null && !result.equals("")) {
            Intent intent = new Intent();
            intent.putExtra("result", result);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onDismiss(Object o) {
        KeyBoardUtils.closeKeybord(etName, this);
    }

    /**
     * 异步任务，识别图片
     *
     * @author duanbokan
     */
    public class ParseImageAsync extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            showProgressDialog(CameraActivity.this, "Image is being recognized..");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                result = ParseImage.getInstance().parseImageToString(params[0]);
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
                result = "";
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            takeOrder(result);
            super.onPostExecute(result);
        }
    }


    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                try {
                    CameraHelper.getInstance().tackPicture(CameraActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setTitle("Tip");
                    builder.setMessage("There is no camera right, please check that the permission has not been opened");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            button1.setEnabled(false);
                        }
                    });
                    builder.create().show();
                    return;
                }
                break;
            case R.id.btn2:
                myAlertDialog();
                break;
            case R.id.btn3:
                finish();
                break;
        }
    }


    private Dialog dialog;

    /**
     * 处理类加载
     *
     * @param context
     * @param msg
     */
    protected void showProgressDialog(Context context, String msg) {
        if (dialog != null) {
            dialog.cancel();
        }
        dialog = new ProcessProgressDialog(context, msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 关闭dialog
     */
    protected void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
