package com.dovar.pili.report;

import android.content.Context;

import com.dovar.pili.report.core.CoreD;


/**
 * Created by heweizong on 2017/9/29.
 */

public class B {
    private static boolean a = false;

    public static void a(Context mContext) {
        if (!a && mContext != null) {
            a = true;
            A.a().a(mContext.getApplicationContext());
            A.a().a(true);
            CoreD.a().a(mContext.getApplicationContext());
        }
    }

    public static void a() {
        CoreD.a().b(null);
        A.a().b();
        com.dovar.pili.report.common.A.h();
        a = false;
    }
}
