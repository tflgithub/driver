package fodel.com.fodelscanner.utils;

import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import fodel.com.fodelscanner.scanner.api.entity.response.BaseEntity;

/**
 * Created by tfl on 2015/7/20.
 */
public class JsonUtils {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private JsonUtils() {

    }


    public static String objectToJson(Object ts) {
        String jsonStr = null;
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }


    public static List<?> jsonToList(String jsonStr, Type type) {
        List<?> objList = null;
        if (gson != null) {
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    public static BaseEntity<?> jsonToBaseEntity(String json, Type type) {
        BaseEntity<?> result = null;
        if (json != null) {
            try {
                result = gson.fromJson(json, type);
            } catch (Exception e) {
                LogUtils.e(e.getMessage() + "");
            }
        }
        return result;
    }


    public static Map<?, ?> jsonToMap(String jsonStr) {
        Map<?, ?> objMap = null;
        if (gson != null) {
            Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
            }
                    .getType();
            objMap = gson.fromJson(jsonStr, type);
        }
        return objMap;
    }


    public static Object jsonToBean(String jsonStr, Class<?> cl) throws Exception {
        Object obj = null;
        if (gson != null) {
            obj = gson.fromJson(jsonStr, cl);
        }
        return obj;
    }


    public static Object getJsonValue(String jsonStr, String key) {
        Object rulsObj = null;
        Map<?, ?> rulsMap = jsonToMap(jsonStr);
        if (rulsMap != null && rulsMap.size() > 0) {
            rulsObj = rulsMap.get(key);
        }
        return rulsObj;
    }

    public static void main(String[] args) {

    }
}
