package com.dovar.pili.report.core;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.dovar.pili.report.A;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.zip.GZIPOutputStream;

/**
 * Created by heweizong on 2017/9/29.
 */

public class CoreE {
    private static boolean a = false;
    private static int b = 120000;
    private static int c = '\uea60';
    private static int d = 30000;
    private boolean e = false;
    private Handler f;
    private HandlerThread g;
    private CoreB h;
    private Object i = new Object();
    private Handler.Callback j = new Handler.Callback() {
        public boolean handleMessage(Message var1) {
            switch(var1.what) {
                case 0:
                    CoreE.this.c((String)var1.obj);
                    break;
                case 1:
                    CoreE.this.a(true);
                    break;
                case 2:
                    CoreE.this.d();
                case 3:
                case 5:
                default:
                    break;
                case 4:
                    CoreE.this.d((String)var1.obj);
                    break;
                case 6:
                    CoreE.this.b(true);
            }

            return true;
        }
    };

    public CoreE() {
    }

    public static int a() {
        return d;
    }

    public void a(Context var1) {
        if(this.g == null) {
            this.h = new CoreB();
            this.h.a(var1.getApplicationContext());
            this.g = new HandlerThread("QosReporter");
            this.g.start();
            this.f = new Handler(this.g.getLooper(), this.j);
            this.f.sendEmptyMessageDelayed(1, (long)b);
        }
    }

    public void b() {
        if(this.g != null) {
            this.f.removeCallbacksAndMessages((Object)null);
            this.f.sendEmptyMessageDelayed(2, 10L);
        }
    }

    private void a(int var1, String var2) {
        Object var3 = this.i;
        synchronized(this.i) {
            if(this.g != null && this.f != null) {
                Message var4 = this.f.obtainMessage(var1, var2);
                this.f.sendMessage(var4);
            }
        }
    }

    public void a(String var1) {
        this.a(4, var1);
    }

    public void b(String var1) {
        this.a(0, var1);
    }

    private void c(String var1) {
        if(!this.h.a(var1)) {
            ;
        }

    }

    private void d(String var1) {
        if(CoreC.a().a(var1) && !this.e && this.f != null) {
            this.e = true;
            this.f.sendEmptyMessageDelayed(6, (long)c);
        }

    }

    private void a(boolean var1) {
        String var2 = this.h.c();
        if(var2 != null && this.a("http://misc-pili-qos-report.qiniuapi.com/raw/log/misc-v5", var2)) {
            this.h.b();
        }

        if(var1 && this.f != null) {
            this.f.sendEmptyMessageDelayed(1, (long)b);
        }

    }

    private void c() {
        Intent var1 = new Intent("pldroid-player-qos-filter");
        var1.putExtra("pldroid-qos-msg-type", 162);
        A.a().a(var1);
    }

    private void b(boolean var1) {
        String var2 = CoreC.a().c();
        if(var2 != null && this.a("http://play-pili-qos-report.qiniuapi.com/raw/log/play-v5", var2)) {
            CoreC.a().b();
        }

        if(var1 && this.f != null) {
            this.c();
            this.f.sendEmptyMessageDelayed(6, (long)c);
        }

    }

    private void d() {
        if(this.g != null) {
            this.f.removeCallbacksAndMessages((Object)null);
            Object var1 = this.i;
            synchronized(this.i) {
                this.f = null;
            }

            this.a(false);
            if(this.e) {
                this.b(false);
            }

            this.g.quit();
            this.g = null;
            this.h.a();
        }
    }

    private void a(String var1, int var2, int var3) {
        if(var2 >= 10000 && var3 >= 10000) {
            if(var1.equals("http://misc-pili-qos-report.qiniuapi.com/raw/log/misc-v5")) {
                if(var2 != b) {
                    b = var2;
                }
            } else if(var1.equals("http://play-pili-qos-report.qiniuapi.com/raw/log/play-v5") && var2 != c) {
                c = var2;
                d = var3;
            }

        }
    }

    private boolean a(String var1, String var2) {
        if(a) {
            Log.d("QosReporter", "url: \n" + var1 + "\ncontent: \n" + var2);
        }

        HttpURLConnection var3;
        try {
            var3 = (HttpURLConnection)(new URL(var1)).openConnection();
        } catch (IOException var29) {
            if(a) {
                var29.printStackTrace();
            }

            return false;
        } catch (Exception var30) {
            if(a) {
                var30.printStackTrace();
            }

            return false;
        }

        var3.setConnectTimeout(3000);
        var3.setReadTimeout(10000);

        try {
            var3.setRequestMethod("POST");
        } catch (ProtocolException var31) {
            if(a) {
                var31.printStackTrace();
            }

            return false;
        }

        var3.setRequestProperty("Content-Type", a?"application/octet-stream":"application/x-gzip");
        var3.setRequestProperty("Accept-Encoding", "gzip");

        try {
            byte[] var4 = var2.getBytes();
            if(var4 == null) {
                return false;
            }

            if(a) {
                var3.getOutputStream().write(var4);
            } else {
                ByteArrayOutputStream var5 = new ByteArrayOutputStream();
                GZIPOutputStream var6 = new GZIPOutputStream(var5);
                var6.write(var4);
                var6.close();
                var3.getOutputStream().write(var5.toByteArray());
            }

            var3.getOutputStream().flush();
        } catch (IOException var38) {
            if(a) {
                var38.printStackTrace();
            }

            return false;
        } catch (Exception var39) {
            return false;
        }

        boolean var40 = false;

        int var41;
        try {
            var41 = var3.getResponseCode();
        } catch (IOException var32) {
            if(a) {
                var32.printStackTrace();
            }

            return false;
        }

        if(a) {
            Log.d("QosReporter", "response code = " + var41);
        }

        if(var41 != 200) {
            return false;
        } else {
            int var42 = var3.getContentLength();
            if(var42 == 0) {
                return false;
            } else {
                if(var42 < 0) {
                    var42 = 16384;
                }

                InputStream var43;
                try {
                    var43 = var3.getInputStream();
                } catch (IOException var34) {
                    if(a) {
                        var34.printStackTrace();
                    }

                    return false;
                } catch (Exception var35) {
                    if(a) {
                        var35.printStackTrace();
                    }

                    return false;
                }

                byte[] var7 = new byte[var42];
                boolean var8 = false;

                int var44;
                label336: {
                    boolean var10;
                    try {
                        try {
                            var44 = var43.read(var7);
                            break label336;
                        } catch (IOException var36) {
                            if(a) {
                                var36.printStackTrace();
                            }
                        }

                        var10 = false;
                    } finally {
                        try {
                            var43.close();
                        } catch (IOException var33) {
                            if(a) {
                                var33.printStackTrace();
                            }

                            return false;
                        }
                    }

                    return var10;
                }

                if(var44 <= 0) {
                    return false;
                } else {
                    String var9 = new String(var7);
                    var9 = var9.trim();
                    if(a) {
                        Log.d("QosReporter", var9);
                    }

                    try {
                        JSONObject var45 = new JSONObject(var9);
                        int var11 = var45.optInt("reportInterval");
                        int var12 = var45.optInt("sampleInterval");
                        this.a(var1, var11 * 1000, var12 * 1000);
                    } catch (Exception var28) {
                        var28.printStackTrace();
                    }

                    return true;
                }
            }
        }
    }
}
