package com.dovar.pili.qos;

import android.content.Context;
import android.content.Intent;

import com.dovar.pili.report.A;
import com.dovar.pili.report.core.CoreE;

/**
 * Created by heweizong on 2017/9/29.
 */

public class c {
    public Context a;

    private c() {
    }

    public static c a() {
        return c.ClassB.a;
    }

    public void a(Context var1) {
        if(var1 != null) {
            this.a = var1.getApplicationContext();
        } else {
            this.a = null;
        }

    }

    public int b() {
        return CoreE.a();
    }

    public void a(Intent var1) {
        if(this.a != null) {
            A.a().a(var1);
        }
    }

    private static class ClassB {
        public static final c a = new c();
    }
}
