package com.dovar.dplayer.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.dovar.dplayer.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static android.content.Context.ACTIVITY_SERVICE;

public class PhoneUtil {
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    public static String getIMEI(Context context1) {
        /*if(TextUtils.isEmpty(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId())){

        }*/
        Context context = MyApplication.getInstance();
        if (!TextUtils.isEmpty(SharedpreferencesUtil.getUniqueId(context))) {
            return SharedpreferencesUtil.getUniqueId(context);
        } else {
            if (!TextUtils.isEmpty(((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId())) {
                //判断是否全是0
                if (!((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId().equals("000000000000000")) {
                    SharedpreferencesUtil.setUniqueId(context, ((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId());
                    return ((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId();
                } else {
                    if (!TextUtils.isEmpty(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))) {
                        SharedpreferencesUtil.setUniqueId(context, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    } else {
                        SharedpreferencesUtil.setUniqueId(context, getUniquePsuedoID());
                        return getUniquePsuedoID();
                    }
                }
            } else {
                if (!TextUtils.isEmpty(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))) {
                    SharedpreferencesUtil.setUniqueId(context, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                } else {
                    SharedpreferencesUtil.setUniqueId(context, getUniquePsuedoID());
                    return getUniquePsuedoID();
                }
            }
        }
    }

    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        /*try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }*/
        //使用硬件信息拼凑出来的15位号码
        return m_szDevIDShort;
    }

    public static List<String> getAppList(Context context) {
        List<String> nameLists = new ArrayList<>();
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo pInfo : packs) {
            /*nameLists.add(pInfo.applicationInfo.loadLabel(context.getPackageManager())
                    .toString());*/
            nameLists.add(pInfo.packageName);
        }
        return nameLists;
    }

    public static String getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断手机是否开启重力感应
     *
     * @param context
     * @return
     */
    public static boolean screenIsOpenRotate(Context context) {
        int gravity = 0;
        try {
            gravity = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (gravity == 1) {
            return true;
        }
        return false;
    }

    public static boolean isAppOnForeground(Context context, String packageName) {
        ActivityManager mActivityManager = ((ActivityManager) context.getSystemService(ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            LogUtil.d("mxs", "mxs top Activity = " + tasksInfo.get(0).topActivity.getPackageName());
            // 应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExsitMianActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }


    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("mxs",
                        String.format("mxs the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("mxs",
                String.format("mxs the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    /**
     * 小米系统
     *
     * @return
     */
    public static boolean isMIUI() {
        if (SharedpreferencesUtil.getSystemName(MyApplication.getInstance()).equals(SharedpreferencesUtil.SYSTEM_XIAOMI)) {
            return true;
        }

        Properties prop = new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        if (isMIUI) {
            SharedpreferencesUtil.saveSystemName(MyApplication.getInstance(), SharedpreferencesUtil.SYSTEM_XIAOMI);
        } else {
            SharedpreferencesUtil.saveSystemName(MyApplication.getInstance(), "");

        }
        return isMIUI;
    }

    /**
     * 华为系统
     *
     * @return
     */
    public static boolean isEMUI() {
        if (SharedpreferencesUtil.getSystemName(MyApplication.getInstance()).equals(SharedpreferencesUtil.SYSTEM_HUAWEI)) {
            return true;
        }

        Properties prop = new Properties();
        boolean isEMUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isEMUI = prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                || prop.getProperty(KEY_EMUI_VERSION, null) != null
                || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null;
        if (isEMUI) {
            SharedpreferencesUtil.saveSystemName(MyApplication.getInstance(), SharedpreferencesUtil.SYSTEM_HUAWEI);
        } else {
            SharedpreferencesUtil.saveSystemName(MyApplication.getInstance(), "");

        }
        return isEMUI;
    }

    public static boolean checkAppExist(String packageName) {
        List<String> list = getAppList(MyApplication.getInstance());
        boolean exist = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(packageName)) {
                exist = true;
                break;
            }
        }
        return exist;
    }


    // 判断是魅族操作系统
    public static boolean isMeizuFlyme() {
        if (SharedpreferencesUtil.getSystemName(MyApplication.getInstance()).equals(SharedpreferencesUtil.SYSTEM_MEIZU)) {
            return true;
        }

        boolean isFlyme = getMeizuFlymeOSFlag().toLowerCase().contains("flyme");
        if (isFlyme) {
            SharedpreferencesUtil.saveSystemName(MyApplication.getInstance(), SharedpreferencesUtil.SYSTEM_MEIZU);
        } else {
            SharedpreferencesUtil.saveSystemName(MyApplication.getInstance(), "");

        }
        return isFlyme;
    }

    /**
     * 获取魅族系统操作版本标识
     */
    public static String getMeizuFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id", "");
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
        }
        return defaultValue;
    }

    /**
     * 华为系统
     *
     * @return
     */
    public static boolean isEMUI4Location() {
        Properties prop = new Properties();
        boolean isEMUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isEMUI = prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                || prop.getProperty(KEY_EMUI_VERSION, null) != null
                || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null;
        return isEMUI;
    }
}
