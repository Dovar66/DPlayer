package com.dovar.pili.report.core;

/**
 * Created by heweizong on 2017/9/29.
 */

public class CoreC {
    private Object a = new Object();
    private StringBuilder b = new StringBuilder();

    public CoreC() {
    }

    public static CoreC a() {
        return CoreC.ClassB.a;
    }

    public void b() {
        Object var1 = this.a;
        synchronized(this.a) {
            this.b.delete(0, this.b.length());
        }
    }

    public String c() {
        if(this.b != null && this.b.length() != 0) {
            Object var1 = this.a;
            synchronized(this.a) {
                return this.b.toString();
            }
        } else {
            return null;
        }
    }

    public boolean a(String var1) {
        if(var1 != null && !var1.equals("")) {
            if(this.b.length() > 65536) {
                return false;
            } else {
                try {
                    Object var2 = this.a;
                    synchronized(this.a) {
                        this.b.append(var1);
                    }
                } catch (OutOfMemoryError var5) {
                    var5.printStackTrace();
                }

                return true;
            }
        } else {
            return false;
        }
    }

    private static class ClassB {
        public static final CoreC a = new CoreC();
    }
}
