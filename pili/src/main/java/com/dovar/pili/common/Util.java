package com.dovar.pili.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by heweizong on 2017/9/29.
 */

public class Util {
    public static final int SDK_INT;

    public Util() {
    }

    public static String getUserAgent(Context var0, String var1) {
        String var2;
        try {
            String var3 = var0.getPackageName();
            PackageInfo var4 = var0.getPackageManager().getPackageInfo(var3, 0);
            var2 = var4.versionName;
        } catch (PackageManager.NameNotFoundException var5) {
            var2 = "?";
        }

        return var1 + "/" + var2 + " (Linux;Android " + Build.VERSION.RELEASE + ") " + "PLDroidPlayer/" + "1.6.0";
    }

    public static Pair<Integer, Integer> getResolution(Context var0) {
        return Build.VERSION.SDK_INT >= 17?getRealResolution(var0):getRealResolutionOnOldDevice(var0);
    }

    private static Pair<Integer, Integer> getRealResolutionOnOldDevice(Context var0) {
        try {
            WindowManager var1 = (WindowManager)var0.getSystemService(Context.WINDOW_SERVICE);
            Display var8 = var1.getDefaultDisplay();
            Method var3 = Display.class.getMethod("getRawWidth", new Class[0]);
            Method var4 = Display.class.getMethod("getRawHeight", new Class[0]);
            Integer var5 = (Integer)var3.invoke(var8, new Object[0]);
            Integer var6 = (Integer)var4.invoke(var8, new Object[0]);
            return new Pair(var5, var6);
        } catch (Exception var7) {
            DisplayMetrics var2 = var0.getResources().getDisplayMetrics();
            return new Pair(Integer.valueOf(var2.widthPixels), Integer.valueOf(var2.heightPixels));
        }
    }

    @TargetApi(17)
    private static Pair<Integer, Integer> getRealResolution(Context var0) {
        WindowManager var1 = (WindowManager)var0.getSystemService(Context.WINDOW_SERVICE);
        Display var2 = var1.getDefaultDisplay();
        DisplayMetrics var3 = new DisplayMetrics();
        var2.getRealMetrics(var3);
        return new Pair(Integer.valueOf(var3.widthPixels), Integer.valueOf(var3.heightPixels));
    }

    public static boolean isUrlLocalFile(String var0) {
        return getPathScheme(var0) == null || "file".equals(getPathScheme(var0));
    }

    public static String getPathScheme(String var0) {
        return Uri.parse(var0).getScheme();
    }

    /** @deprecated */
    @Deprecated
    public static boolean isLiveStreaming(String var0) {
        return var0.startsWith("rtmp://") || var0.endsWith(".m3u8");
    }

    public static int getDisplayDefaultRotation(Context var0) {
        WindowManager var1 = (WindowManager)var0.getSystemService(Context.WINDOW_SERVICE);
        return var1.getDefaultDisplay().getRotation();
    }

    public static boolean isNetworkConnected(Context var0) {
        if(var0 == null) {
            return false;
        } else {
            ConnectivityManager var1 = (ConnectivityManager)var0.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo var2 = var1.getActiveNetworkInfo();
            return var2 != null?var2.isAvailable():false;
        }
    }

    static {
        SDK_INT = Build.VERSION.SDK_INT;
    }
}
