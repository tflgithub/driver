package fodel.com.fodelscanner.view;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Happiness on 2017/2/22.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback {

    private SurfaceHolder holder;
    private Camera camera;
    private boolean af;


    public CameraView(Context context) {
        super(context);
        holder = getHolder();//生成Surface Holder
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//指定Push Buffer
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();//摄像头的初始化
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(width, height);
        camera.setParameters(parameters);//设置参数
        camera.startPreview();//开始预览
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//屏幕触摸事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下时自动对焦
            camera.autoFocus(null);
            af = true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP && af == true) {//放开后拍照
            camera.takePicture(null, null, this);
            af = false;
        }
        return true;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        try {
            String path = Environment.getExternalStorageDirectory() + "/test.jpg";
            data2file(data, path);
        } catch (Exception e) {
        }
        camera.startPreview();
    }

    private void data2file(byte[] w, String fileName) throws Exception {//将二进制数据转换为文件的函数
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            out.write(w);
            out.close();
        } catch (Exception e) {
            if (out != null)
                out.close();
            throw e;
        }
    }
}
