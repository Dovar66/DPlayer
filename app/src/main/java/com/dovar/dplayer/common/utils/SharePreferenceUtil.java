package com.dovar.dplayer.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by edit on 2016/8/29.
 */
public class SharePreferenceUtil {

    private static final String UniqueId = "UniqueId";
    private static final String SP_SYSTEM_NAME = "sp_system_key";
    private static final String SP_SYSTEM = "sp_system";
    public static final String SYSTEM_XIAOMI = "SYSTEM_XIAOMI";
    public static final String SYSTEM_HUAWEI = "SYSTEM_HUAWEI";
    public static final String SYSTEM_MEIZU = "SYSTEM_MEIZU";

    public static String getUniqueId(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(UniqueId,
                    Activity.MODE_PRIVATE);
            return preferences.getString(UniqueId, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setUniqueId(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(UniqueId,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(UniqueId, s);
        editor.apply();
    }

    public static void saveSystemName(Context context, String name) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SP_SYSTEM,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SP_SYSTEM_NAME, name);
        editor.apply();
    }

    public static String getSystemName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SP_SYSTEM,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_SYSTEM_NAME, "");
    }

}
