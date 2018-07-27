package fodel.com.fodelscanner.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fodel.com.fodelscanner.MyApplication;
import fodel.com.fodelscanner.TessConstantConfig;

/**
 * Created by Happiness on 2017/2/21.
 */
public class ParseImage {

    private static final String TAG = "ParseImage";

    private static final String IMAGE_PATH_IS_NULL = "Can not find the image";

    private static ParseImage instance;

    public static ParseImage getInstance() {
        if (instance == null) {
            instance = new ParseImage();
        }
        return instance;
    }

    /**
     * 识别图片中的文字,需要放入异步线程中进行执行
     *
     * @return
     * @throws IOException
     */
    public String parseImageToString(String imagePath) throws Exception {
        // 检验图片地址是否正确
        if (imagePath == null || imagePath.equals("")) {
            return IMAGE_PATH_IS_NULL;
        }

        // 获取Bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        // 图片旋转角度
        int rotate = 0;

        ExifInterface exif = new ExifInterface(imagePath);

        // 先获取当前图像的方向，判断是否需要旋转
        int imageOrientation = exif
                .getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

        Log.i(TAG, "Current image orientation is " + imageOrientation);

        switch (imageOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            default:
                break;
        }

        Log.i(TAG, "Current image need rotate: " + rotate);

        // 获取当前图片的宽和高
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // 使用Matrix对图片进行处理
        Matrix mtx = new Matrix();
        mtx.preRotate(rotate);

        // 旋转图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        // 开始调用Tess函数对图像进行识别
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        // 使用默认语言初始化BaseApi
        File dir = new File(TessConstantConfig.TESSBASE_PATH + "tessdata/");
        if (!dir.exists())
            dir.mkdirs();
        mkLanguageFile();
        baseApi.init(TessConstantConfig.TESSBASE_PATH,
                TessConstantConfig.DEFAULT_LANGUAGE_ENG);
        baseApi.setImage(bitmap);
        // 获取返回值
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();
        SDCardUtils.deleteFile(imagePath);
        SDCardUtils.deleteFile(TessConstantConfig.TESSBASE_PATH);
        return recognizedText;
    }

    private void mkLanguageFile() {
        if (!(new File(TessConstantConfig.TESSBASE_PATH + "tessdata/" + TessConstantConfig.DEFAULT_LANGUAGE_ENG + ".traineddata")).exists()) {
            try {
                AssetManager assetManager = MyApplication.getApplication().getAssets();
                InputStream in = assetManager.open("tessdata/" + TessConstantConfig.DEFAULT_LANGUAGE_ENG + ".traineddata");
                OutputStream out = new FileOutputStream(TessConstantConfig.TESSBASE_PATH
                        + "tessdata/" + TessConstantConfig.DEFAULT_LANGUAGE_ENG + ".traineddata");
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v(TAG, "Copied " + TessConstantConfig.DEFAULT_LANGUAGE_ENG + " traineddata");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + TessConstantConfig.DEFAULT_LANGUAGE_ENG + " traineddata " + e.toString());
            }
        }
    }
}
