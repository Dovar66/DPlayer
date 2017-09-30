package com.dovar.pili.qos;

import android.content.Context;
import android.content.Intent;

import com.dovar.pili.report.A;
import com.dovar.pili.report.core.CoreE;

/**
 * Created by heweizong on 2017/9/29.
 */

public class c {
    public Context mContext;

    private c() {
    }

    public static c a() {
        return c.ClassB.a;
    }

    public void a(Context mContext) {
        if (mContext != null) {
            this.mContext = mContext.getApplicationContext();
        } else {
            this.mContext = null;
        }
    }

    public int b() {
        return CoreE.a();
    }

    public void a(Intent mIntent) {
        if (this.mContext != null) {
            A.a().a(mIntent);
        }
    }

    private static class ClassB {
        public static final c a = new c();
    }
}
