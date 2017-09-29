package com.dovar.pili.report.core;

import android.view.Choreographer;

import java.util.concurrent.TimeUnit;

/**
 * Created by heweizong on 2017/9/29.
 */

public class CoreA implements Choreographer.FrameCallback {
    public static final CoreA a = new CoreA();
    private static final long b;
    private static final long c;
    private static long d;
    private static long e;
    private static int f;
    private static int g;
    private static boolean h;

    public CoreA() {
    }

    public void a() {
        if(h) {
            h = false;
            g = 0;
            f = 0;
            e = 0L;
            d = 0L;
        }
    }

    public void b() {
        h = true;
    }

    public int c() {
        this.d();
        return g;
    }

    private void d() {
        if(g == 0 || e - d >= c) {
            long var1 = e - d;
            g = Math.round((float)((long)f * b) / (float)var1);
            d = e;
            f = 0;
        }

    }

    public void doFrame(long var1) {
        ++f;
        if(d == 0L) {
            d = var1;
            Choreographer.getInstance().postFrameCallback(this);
        } else {
            e = var1;
            if(h) {
                Choreographer.getInstance().removeFrameCallback(this);
            } else {
                Choreographer.getInstance().postFrameCallback(this);
            }

        }
    }

    static {
        b = TimeUnit.NANOSECONDS.convert(1L, TimeUnit.SECONDS);
        c = TimeUnit.NANOSECONDS.convert(10L, TimeUnit.SECONDS);
        d = 0L;
        e = 0L;
        f = 0;
        g = 0;
        h = false;
    }
}
