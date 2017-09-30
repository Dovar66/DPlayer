package com.dovar.pili.report.common;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Debug;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Choreographer;

import com.dovar.pili.report.core.CoreA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by heweizong on 2017/9/29.
 */

public class A {
    private static String a = "AUTO";
    private static String[] b = new String[]{"/sys/devices/platform/omap/omap_temp_sensor.0/temperature", "/sys/kernel/debug/tegra_thermal/temp_tj", "/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp", "/sys/class/thermal/thermal_zone0/temp", "/sys/class/thermal/thermal_zone1/temp", "/sys/devices/platform/s5p-tmu/curr_temp", "/sys/devices/virtual/thermal/thermal_zone0/temp", "/sys/devices/virtual/thermal/thermal_zone1/temp", "/sys/devices/system/cpu/cpufreq/cput_attributes/cur_temp", "/sys/devices/platform/s5p-tmu/temperature"};

    public static String a(Context var0) {
        SharedPreferences var1 = var0.getSharedPreferences("qos", 0);
        String var2 = var1.getString("deviceId", "");
        if ("".equals(var2)) {
            var2 = c();
            SharedPreferences.Editor var3 = var1.edit();
            var3.putString("deviceId", var2);
            var3.commit();
        }

        return var2;
    }

    public static String a() {
        DatagramSocket var0;
        InetAddress var1;
        try {
            var0 = new DatagramSocket();
            var1 = InetAddress.getByName("114.114.114.114");
            var0.connect(var1, 53);
        } catch (IOException var3) {
            return "";
        }

        var1 = var0.getLocalAddress();
        var0.close();
        if (var1 == null) {
            return "";
        } else {
            String var2 = var1.getHostAddress();
            return var2 != null && !"::".equals(var2) ? var2 : "";
        }
    }

    private static InetAddress[] j() {
        try {
            Process var0 = Runtime.getRuntime().exec("getprop");
            InputStream var1 = var0.getInputStream();
            LineNumberReader var2 = new LineNumberReader(new InputStreamReader(var1));
            String var3 = null;
            ArrayList var4 = new ArrayList(5);

            while (true) {
                String var6;
                String var7;
                do {
                    int var5;
                    do {
                        do {
                            if ((var3 = var2.readLine()) == null) {
                                if (var4.size() > 0) {
                                    return (InetAddress[]) var4.toArray(new InetAddress[var4.size()]);
                                }

                                return null;
                            }

                            var5 = var3.indexOf("]: [");
                        } while (var5 == -1);
                    } while (var5 + 4 + 7 > var3.length() - 1);

                    var6 = var3.substring(1, var5);
                    var7 = var3.substring(var5 + 4, var3.length() - 1);
                }
                while (!var6.endsWith(".dns") && !var6.endsWith(".dns1") && !var6.endsWith(".dns2") && !var6.endsWith(".dns3") && !var6.endsWith(".dns4"));

                InetAddress var8 = InetAddress.getByName(var7);
                if (var8 != null) {
                    var7 = var8.getHostAddress();
                    if (var7 != null && var7.length() != 0) {
                        var4.add(var8);
                    }
                }
            }
        } catch (IOException var9) {
            Logger.getLogger("AndroidDnsServer").log(Level.WARNING, "Exception in findDNSByExec", var9);
            return null;
        }
    }

    private static InetAddress[] k() {
        try {
            Class var0 = Class.forName("android.os.SystemProperties");
            Method var1 = var0.getMethod("get", new Class[]{String.class});
            ArrayList var2 = new ArrayList(5);
            String[] var3 = new String[]{"net.dns1", "net.dns2", "net.dns3", "net.dns4"};
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String var6 = var3[var5];
                String var7 = (String) var1.invoke((Object) null, new Object[]{var6});
                if (var7 != null && var7.length() != 0) {
                    InetAddress var8 = InetAddress.getByName(var7);
                    if (var8 != null) {
                        var7 = var8.getHostAddress();
                        if (var7 != null && var7.length() != 0 && !var2.contains(var8)) {
                            var2.add(var8);
                        }
                    }
                }
            }

            if (var2.size() > 0) {
                return (InetAddress[]) var2.toArray(new InetAddress[var2.size()]);
            }
        } catch (Exception var9) {
            Logger.getLogger("AndroidDnsServer").log(Level.WARNING, "Exception in findDNSByReflection", var9);
        }

        return null;
    }

    public static String b() {
        InetAddress[] var0 = k();
        if (var0 == null) {
            var0 = j();
            if (var0 == null) {
                return "";
            }
        }

        String[] var1 = new String[var0.length];
        String var2 = "";

        for (int var3 = 0; var3 < var1.length; ++var3) {
            var1[var3] = var0[var3].toString();
            if (var1[var3].indexOf(47) == 0) {
                var1[var3] = var1[var3].substring(1);
                var2 = var1[var3];
            }
        }

        return var2;
    }

    public static String c() {
        Random var0 = new Random();
        return System.currentTimeMillis() + "" + var0.nextInt(999);
    }

    public static String a(String var0) {
        StringBuilder var1 = new StringBuilder();
        int var2 = 0;

        for (int var3 = var0.length(); var2 < var3; ++var2) {
            char var4 = var0.charAt(var2);
            if (var4 > 31 && var4 < 127) {
                var1.append(var4);
            }
        }

        return var1.toString();
    }

    public static String d() {
        String var0 = Build.VERSION.RELEASE;
        return var0 == null ? "" : a(var0.trim());
    }

    public static String e() {
        String var0 = Build.MODEL.trim();
        String var1 = a(Build.MANUFACTURER.trim(), var0);
        if (TextUtils.isEmpty(var1)) {
            var1 = a(Build.BRAND.trim(), var0);
        }

        String var2 = a((var1 == null ? "" : var1) + var0);
        return var2.replace(" ", "_");
    }

    private static String a(String var0, String var1) {
        String var2 = var0.toLowerCase(Locale.getDefault());
        return !var2.startsWith("unknown") && !var2.startsWith("alps") && !var2.startsWith("android") && !var2.startsWith("sprd") && !var2.startsWith("spreadtrum") && !var2.startsWith("rockchip") && !var2.startsWith("wondermedia") && !var2.startsWith("mtk") && !var2.startsWith("mt65") && !var2.startsWith("nvidia") && !var2.startsWith("brcm") && !var2.startsWith("marvell") && !var1.toLowerCase(Locale.getDefault()).contains(var2) ? var0 : null;
    }

    public static A.ClassB f() {
        BufferedReader var0 = null;

        long var1;
        long var3;
        label710:
        {
            A.ClassB var10;
            try {
                var0 = new BufferedReader(new FileReader("/proc/stat"));
                String[] var9 = var0.readLine().split("[ ]+", 9);
                var1 = Long.parseLong(var9[1]) + Long.parseLong(var9[2]) + Long.parseLong(var9[3]);
                var3 = var1 + Long.parseLong(var9[4]) + Long.parseLong(var9[5]) + Long.parseLong(var9[6]) + Long.parseLong(var9[7]);
                break label710;
            } catch (IOException var111) {
                var10 = new A.ClassB(0.0F, 0.0F);
                return var10;
            } catch (Exception var112) {
                var10 = new A.ClassB(0.0F, 0.0F);
            } finally {
                if (var0 != null) {
                    try {
                        var0.close();
                    } catch (IOException var103) {

                    }
                }

            }

            return var10;
        }

        var0 = null;

        String[] var11;
        A.ClassB var12;
        long var114;
        label688:
        {
            try {
                var0 = new BufferedReader(new FileReader("/proc/" + android.os.Process.myPid() + "/stat"));
                var11 = var0.readLine().split("[ ]+", 18);
                var114 = Long.parseLong(var11[13]) + Long.parseLong(var11[14]) + Long.parseLong(var11[15]) + Long.parseLong(var11[16]);
                var0.close();
                break label688;
            } catch (IOException var109) {
                var12 = new A.ClassB(0.0F, 0.0F);
            } finally {
                if (var0 != null) {
                    try {
                        var0.close();
                    } catch (IOException var101) {

                    }
                }

            }

            return var12;
        }

        try {
            Thread.sleep(100L);
        } catch (InterruptedException var104) {

        }

        var0 = null;

        long var5;
        long var7;
        label681:
        {
            try {
                var0 = new BufferedReader(new FileReader("/proc/stat"));
                var11 = var0.readLine().split("[ ]+", 9);
                var5 = Long.parseLong(var11[1]) + Long.parseLong(var11[2]) + Long.parseLong(var11[3]);
                var7 = var5 + Long.parseLong(var11[4]) + Long.parseLong(var11[5]) + Long.parseLong(var11[6]) + Long.parseLong(var11[7]);
                break label681;
            } catch (IOException var107) {
                var12 = new A.ClassB(0.0F, 0.0F);
            } finally {
                if (var0 != null) {
                    try {
                        var0.close();
                    } catch (IOException var102) {
                        ;
                    }
                }

            }

            return var12;
        }

        var0 = null;

        long var115;
        label674:
        {
            A.ClassB var14;
            try {
                var0 = new BufferedReader(new FileReader("/proc/" + android.os.Process.myPid() + "/stat"));
                String[] var13 = var0.readLine().split("[ ]+", 18);
                var115 = Long.parseLong(var13[13]) + Long.parseLong(var13[14]) + Long.parseLong(var13[15]) + Long.parseLong(var13[16]);
                var0.close();
                break label674;
            } catch (IOException var105) {
                var14 = new A.ClassB(0.0F, 0.0F);
            } finally {
                if (var0 != null) {
                    try {
                        var0.close();
                    } catch (IOException var100) {

                    }
                }

            }

            return var14;
        }

        long var116 = var7 - var3;
        float var15 = (float) ((var5 - var1) * 100L) / (float) var116;
        float var16 = (float) ((var115 - var114) * 100L) / (float) var116;
        return var15 >= 0.0F && var15 <= 100.0F ? new A.ClassB(var15, var16) : new A.ClassB(0.0F, 0.0F);
    }

    @TargetApi(16)
    public static A.b b(Context var0) {
        if (Build.VERSION.SDK_INT < 16) {
            return new A.b(0L, 0L, 0L, 0L);
        } else {
            ActivityManager var1 = (ActivityManager) var0.getSystemService(Context.ACTIVITY_SERVICE);
            if (var1 == null) {
                return new A.b(0L, 0L, 0L, 0L);
            } else {
                ActivityManager.MemoryInfo var2 = new ActivityManager.MemoryInfo();
                var1.getMemoryInfo(var2);
                android.os.Debug.MemoryInfo var3 = new android.os.Debug.MemoryInfo();
                Debug.getMemoryInfo(var3);
                long var4 = (long) ((var3.dalvikPrivateDirty + var3.nativePrivateDirty) * 1024);
                return new A.b(var2.totalMem, var2.totalMem - var2.availMem, var2.threshold, var4);
            }
        }
    }

    public static String c(Context var0) {
        if (var0 == null) {
            return "";
        } else {
            ConnectivityManager var1 = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (var1 == null) {
                return "None";
            } else {
                try {
                    NetworkInfo var2 = var1.getActiveNetworkInfo();
                    return var2 != null && var2.isConnected() && var2.getState() == NetworkInfo.State.CONNECTED ? (var2.getType() == 1 ? "WIFI" : var2.getSubtypeName()) : "None";
                } catch (Exception var3) {
                    return "Unknown";
                }
            }
        }
    }

    public static String d(Context var0) {
        try {
            PackageInfo var1 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0);
            return var1.packageName;
        } catch (PackageManager.NameNotFoundException var3) {
            return "";
        }
    }

    public static String e(Context var0) {
        try {
            PackageInfo var1 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 0);
            return var1.versionName;
        } catch (PackageManager.NameNotFoundException var3) {
            return "";
        }
    }

    public static boolean f(Context var0) {
        boolean var1 = false;
        if (var0 != null) {
            var1 = var0.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED && var0.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED;
        }

        return var1;
    }

    public static boolean g(Context var0) {
        boolean var1 = false;
        if (var0 != null) {
            var1 = var0.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == PackageManager.PERMISSION_GRANTED;
        }

        return var1;
    }

    public static String[] h(Context var0) {
        String[] var1 = null;
        if (g(var0)) {
            WifiManager var2 = (WifiManager) var0.getSystemService(Context.WIFI_SERVICE);
            if (var2 != null) {
                WifiInfo var3 = var2.getConnectionInfo();
                if (var3 != null) {
                    String var4 = var3.getSSID();
                    var1 = new String[]{var4, Integer.toString(var3.getRssi())};
                }
            }
        }

        return var1;
    }

    public static String[] i(Context var0) {
        String[] var1 = null;
        if (var0 != null && f(var0) && Build.VERSION.SDK_INT > 17) {
            TelephonyManager var2 = (TelephonyManager) var0.getSystemService(Context.TELEPHONY_SERVICE);
            if (var2 == null) {
                return var1;
            }

            var1 = new String[]{"", ""};
            var1[0] = var2.getNetworkOperatorName();
            List var3 = var2.getAllCellInfo();
            if (var3 != null) {
                for (int var4 = 0; var4 < var3.size(); ++var4) {
                    if (((CellInfo) var3.get(var4)).isRegistered()) {
                        CellInfo var5 = (CellInfo) var3.get(var4);
                        if (var5 instanceof CellInfoCdma) {
                            CellInfoCdma var6 = (CellInfoCdma) var5;
                            CellSignalStrengthCdma var7 = var6.getCellSignalStrength();
                            var1[1] = String.valueOf(var7.getLevel());
                        } else if (var5 instanceof CellInfoWcdma) {
                            CellInfoWcdma var8 = (CellInfoWcdma) var5;
                            CellSignalStrengthWcdma var11 = var8.getCellSignalStrength();
                            var1[1] = String.valueOf(var11.getLevel());
                        } else if (var5 instanceof CellInfoGsm) {
                            CellInfoGsm var9 = (CellInfoGsm) var5;
                            CellSignalStrengthGsm var12 = var9.getCellSignalStrength();
                            var1[1] = String.valueOf(var12.getLevel());
                        } else if (var5 instanceof CellInfoLte) {
                            CellInfoLte var10 = (CellInfoLte) var5;
                            CellSignalStrengthLte var13 = var10.getCellSignalStrength();
                            var1[1] = String.valueOf(var13.getLevel());
                        }
                    }
                }
            }
        }

        return var1;
    }

    public static boolean b(String var0) {
        return var0 == null ? false : var0.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$");
    }

    public static void g() {
        if (Build.VERSION.SDK_INT >= 16) {
            CoreA.a.a();

            try {
                Choreographer.getInstance().postFrameCallback(CoreA.a);
            } catch (RuntimeException var1) {
                var1.printStackTrace();
            }
        }

    }

    public static void h() {
        if (Build.VERSION.SDK_INT >= 16) {
            CoreA.a.b();
        }

    }

    public static int i() {
        return Build.VERSION.SDK_INT >= 16 ? CoreA.a.c() : 60;
    }

    public static class b {
        public final long a;
        public final long b;
        public final long c;
        public final long d;

        public b(long var1, long var3, long var5, long var7) {
            this.a = var1;
            this.b = var3;
            this.d = var5;
            this.c = var7;
        }
    }

    public static class ClassB {
        public final float a;
        public final float b;

        public ClassB(float var1, float var2) {
            this.a = var1;
            this.b = var2;
        }
    }
}
