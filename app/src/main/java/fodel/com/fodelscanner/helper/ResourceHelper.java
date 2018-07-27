package fodel.com.fodelscanner.helper;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by Happiness on 2017/3/3.
 */
public class ResourceHelper {

    private static ResourceHelper mResource = null;
    private static Class<?> mDrawable = null;
    private static String mPackageName = "fodel.com.fodelscanner";

    public ResourceHelper(String packageName) {
        try {
            mDrawable = Class.forName(packageName + ".R$drawable");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ResourceHelper getInstance(Context context) {
        if (mResource == null) {
//            mPackageName = (mPackageName == null ? context.getPackageName()
//                    : mPackageName);
            mResource = new ResourceHelper(mPackageName);
        }
        return mResource;
    }

    private int getResourceId(Class<?> classType, String resourceName) {
        if (classType == null) {
            throw new IllegalArgumentException(
                    "ResClass is not initialized. Please make sure you have added neccessary resources. Also make sure you have "
                            + mPackageName
                            + ".R$* configured in obfuscation. field="
                            + resourceName);
        }
        try {
            Field field = classType.getField(resourceName);
            return field.getInt(resourceName);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ResourceHelper",
                    "Error getting resource. Make sure you have copied all resources (res/) from SDK to your project.");
        }
        return -1;
    }

    public int getDrawableId(String resourceName) {
        return getResourceId(mDrawable, resourceName);
    }
}
