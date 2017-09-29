package com.dovar.pili.report.core;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by heweizong on 2017/9/29.
 */

public class CoreB {
    private long a = 0L;
    private long b = 0L;
    private long c = 0L;
    private long d = 0L;
    private Context e;
    private boolean f = false;
    private String g = null;

    public CoreB() {
    }

    private static String a(Context var0, String var1, long var2) {
        FileInputStream var4 = null;
        BufferedReader var5 = null;

        String var9;
        try {
            var4 = var0.openFileInput(var1);
            var5 = new BufferedReader(new InputStreamReader(var4));
            var5.skip(var2);
            StringBuilder var6 = new StringBuilder();

            String var7;
            while((var7 = var5.readLine()) != null) {
                var6.append(var7);
                var6.append("\n");
            }

            String var8 = var6.toString();
            if(!"".equals(var8)) {
                var9 = var8;
                return var9;
            }

            var9 = null;
        } catch (FileNotFoundException var15) {
            return null;
        } catch (IOException var16) {
            var16.printStackTrace();
            return null;
        } catch (OutOfMemoryError var17) {
            var17.printStackTrace();
            return null;
        } finally {
            a((Closeable)var4);
            a((Closeable)var5);
        }

        return var9;
    }

    private static boolean a(Context var0, String var1, String var2, int var3) {
        FileOutputStream var4 = null;
        BufferedWriter var5 = null;

        try {
            var4 = var0.openFileOutput(var1, var3);
            var5 = new BufferedWriter(new OutputStreamWriter(var4));
            var5.write(var2);
            var5.close();
            boolean var6 = true;
            return var6;
        } catch (FileNotFoundException var12) {
            ;
        } catch (IOException var13) {
            var13.printStackTrace();
        } catch (OutOfMemoryError var14) {
            var14.printStackTrace();
        } finally {
            a((Closeable)var4);
            a((Closeable)var5);
        }

        return false;
    }

    private static void a(Closeable var0) {
        if(var0 != null) {
            try {
                var0.close();
            } catch (IOException var2) {
                var2.printStackTrace();
            }

        }
    }

    public void a(Context var1) {
        if(!this.f) {
            this.e = var1.getApplicationContext();
            this.f();
            if(this.d()) {
                this.g = a(var1, "pili_qos_cache", 0L);
            }

            this.f = true;
        }
    }

    public void a() {
        if(this.f) {
            if(this.g != null) {
                a(this.e, "pili_qos_cache", this.g, 0);
                this.g = null;
            }

            this.f = false;
        }
    }

    public void b() {
        this.g = null;
        if(this.d()) {
            this.e.deleteFile("pili_qos_cache");
        }

    }

    public String c() {
        if(!this.f) {
            return null;
        } else if(this.g != null) {
            return this.g;
        } else if(this.a == this.c && this.b == this.d) {
            return null;
        } else {
            synchronized(this) {
                boolean var2 = this.a < this.c;
                String var3 = "pili_qos_log." + this.a;
                this.g = a(this.e, var3, this.b);
                if(this.g == null) {
                    return null;
                }

                this.b += (long)this.g.length();
                if(this.a < this.c) {
                    ++this.a;
                    this.b = 0L;
                }

                this.g();
                if(var2) {
                    long var4 = this.a - 1L;
                    this.e.deleteFile("pili_qos_log." + var4);
                }
            }

            return this.g;
        }
    }

    public boolean a(String var1) {
        if(!this.f) {
            return false;
        } else if(this.c - this.a > 100L) {
            return false;
        } else {
            synchronized(this) {
                String var3 = "pili_qos_log." + this.c;
                if(!a(this.e, var3, var1, '耀')) {
                    return false;
                } else {
                    this.d += (long)var1.length();
                    if(this.d >= 65536L) {
                        ++this.c;
                        this.d = 0L;
                    }

                    this.g();
                    return true;
                }
            }
        }
    }

    private boolean d() {
        String var1 = this.e.getFilesDir().getAbsolutePath() + "/" + "pili_qos_cache";
        return (new File(var1)).exists();
    }

    private void e() {
        String[] var1 = this.e.fileList();
        String[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if(var5.startsWith("pili_qos_")) {
                this.e.deleteFile(var5);
            }
        }

    }

    private boolean f() {
        try {
            String var1 = a(this.e, "pili_qos_index.json", 0L);
            if(var1 == null) {
                this.e();
                return false;
            } else {
                JSONObject var2 = new JSONObject(String.valueOf(var1));
                this.a = var2.getLong("read_file_index");
                this.b = var2.getLong("read_file_position");
                this.c = var2.getLong("write_file_index");
                this.d = var2.getLong("write_file_position");
                return true;
            }
        } catch (JSONException var3) {
            var3.printStackTrace();
            this.e();
            return false;
        }
    }

    private boolean g() {
        try {
            JSONObject var1 = new JSONObject();
            var1.put("read_file_index", this.a);
            var1.put("read_file_position", this.b);
            var1.put("write_file_index", this.c);
            var1.put("write_file_position", this.d);
            return a(this.e, "pili_qos_index.json", var1.toString(), 0);
        } catch (JSONException var2) {
            var2.printStackTrace();
            return false;
        }
    }
}
