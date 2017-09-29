package com.dovar.pili.report.core;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

import com.dovar.pili.report.A;

/**
 * Created by heweizong on 2017/9/29.
 */

public class CoreD {
    private static Context a;
    private CoreF b;
    private Object c;
    private boolean d;
    private BroadcastReceiver e;
    private BroadcastReceiver f;

    private CoreD() {
        this.c = new Object();
        this.d = false;
        this.e = new BroadcastReceiver() {
            public void onReceive(Context var1, Intent var2) {
                if("pldroid-player-qos-filter".equals(var2.getAction())) {
                    CoreD.this.a(var2);
                }
            }
        };
        this.f = new BroadcastReceiver() {
            @TargetApi(11)
            public void onReceive(Context var1, Intent var2) {
                if("android.net.conn.CONNECTIVITY_CHANGE".equals(var2.getAction())) {
                    AsyncTask.execute(new Runnable() {
                        public void run() {
                            CoreD.this.b.d();
                        }
                    });
                }
            }
        };
    }

    public static CoreD a() {
        return CoreD.ClassB.a;
    }

    public void a(Context var1) {
        Object var2 = this.c;
        synchronized(this.c) {
            if(!this.d && var1 != null) {
                a = var1.getApplicationContext();
                this.b = new CoreF();
                this.b.a(var1);
                A.a().a(this.e, new IntentFilter("pldroid-player-qos-filter"));
                a.registerReceiver(this.f, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                this.d = true;
            }
        }
    }

    public void b(Context var1) {
        Object var2 = this.c;
        synchronized(this.c) {
            if(a != null && this.d) {
                a.unregisterReceiver(this.f);
                a = null;
                A.a().a(this.e);
                this.b.a();
                this.d = false;
            }
        }
    }

    private boolean a(Intent var1) {
        int var2 = var1.getIntExtra("pldroid-qos-msg-type", -1);
        switch(var2) {
            case 4:
                String var3 = var1.getStringExtra("reqid");
                this.b.b().a(var1.getStringExtra("scheme"), var1.getStringExtra("domain"), var1.getStringExtra("remoteIp"), var1.getStringExtra("path"), var3);
                this.b.e();
                com.dovar.pili.report.common.A.g();
                break;
            case 162:
                this.b.e();
                break;
            case 193:
                this.b(var1);
                break;
            case 195:
                this.b.a(var1);
                break;
            case 196:
                this.b.b(var1);
        }

        return true;
    }

    private void b(Intent var1) {
        long var2 = var1.getLongExtra("beginAt", 0L);
        long var4 = var1.getLongExtra("endAt", 0L);
        long var6 = var1.getLongExtra("bufferingTimes", 0L);
        int var8 = var1.getIntExtra("videoSourceFps", 0);
        int var9 = var1.getIntExtra("dropVideoFrames", 0);
        int var10 = var1.getIntExtra("audioSourceFps", 0);
        int var11 = var1.getIntExtra("audioDropFrames", 0);
        int var12 = var1.getIntExtra("videoRenderFps", 0);
        int var13 = var1.getIntExtra("audioRenderFps", 0);
        int var14 = var1.getIntExtra("videoBufferTime", 0);
        int var15 = var1.getIntExtra("audioBufferTime", 0);
        long var16 = var1.getLongExtra("audioBitrate", 0L);
        long var18 = var1.getLongExtra("videoBitrate", 0L);
        if(this.b.c().a(var2, var4, var6, var8, var9, var10, var11, var12, var13, var14, var15, var16, var18)) {
            this.b.f();
        }
    }

    private static class ClassB {
        public static final CoreD a = new CoreD();
    }
}
