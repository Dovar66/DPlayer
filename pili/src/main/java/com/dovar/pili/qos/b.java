package com.dovar.pili.qos;

import android.content.Context;

import com.dovar.pili.report.B;

/**
 * Created by heweizong on 2017/9/29.
 */

public class b {
    private static boolean a = true;
    private static boolean b = false;

    public static void a(Context mContext) {
        if(a || !b) {
            b = true;
            B.a(mContext);
            c.a().a(mContext);
        }
    }

    public static void b(Context var0) {
        if(a || b) {
            b = false;
            c.a().a((Context)null);
        }
    }
}
