package com.dovar.pili.report;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by heweizong on 2017/9/29.
 */

public class A {
    private static final Object a = new Object();
    private static A b;
    private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> c = new HashMap();
    private final HashMap<String, ArrayList<A.ClassB>> d = new HashMap();
    private final ArrayList<A.ClassA> e = new ArrayList();
    private Context f;
    private Handler g;
    private HandlerThread h;
    private boolean i = false;
    private boolean j = false;

    private A() {
    }


    public static A a() {
        Object var0 = a;
        synchronized(a) {
            if(b == null) {
                b = new A();
            }

            return b;
        }
    }

    public void a(Context var1) {
        HashMap var2 = this.c;
        synchronized(this.c) {
            if(var1 != null && !this.j) {
                this.j = true;
                this.f = var1.getApplicationContext();
                this.h = new HandlerThread("QosBroadcastManager");
                this.h.start();
                this.g = new Handler(this.h.getLooper()) {
                    public void handleMessage(Message var1) {
                        switch(var1.what) {
                            case 1:
                                A.this.c();
                                break;
                            default:
                                super.handleMessage(var1);
                        }

                    }
                };
            }
        }
    }

    public void b() {
        HashMap var1 = this.c;
        synchronized(this.c) {
            if(this.j) {
                this.j = false;
                this.h.quit();
                this.h = null;
            }
        }
    }

    public void a(boolean var1) {
        this.i = var1;
    }

    public void a(BroadcastReceiver var1, IntentFilter var2) {
        if(this.f == null) {
            Log.e("QosBroadcastManager", "Context is NULL");
        }

        HashMap var3 = this.c;
        synchronized(this.c) {
            A.ClassB var4 = new A.ClassB(var2, var1);
            ArrayList var5 = (ArrayList)this.c.get(var1);
            if(var5 == null) {
                var5 = new ArrayList(1);
                this.c.put(var1, var5);
            }

            var5.add(var2);

            for(int var6 = 0; var6 < var2.countActions(); ++var6) {
                String var7 = var2.getAction(var6);
                ArrayList var8 = (ArrayList)this.d.get(var7);
                if(var8 == null) {
                    var8 = new ArrayList(1);
                    this.d.put(var7, var8);
                }

                var8.add(var4);
            }

        }
    }

    public void a(BroadcastReceiver var1) {
        if(this.f == null) {
            throw new IllegalStateException("Context is NULL");
        } else {
            HashMap var2 = this.c;
            synchronized(this.c) {
                ArrayList var3 = (ArrayList)this.c.remove(var1);
                if(var3 != null) {
                    for(int var4 = 0; var4 < var3.size(); ++var4) {
                        IntentFilter var5 = (IntentFilter)var3.get(var4);

                        for(int var6 = 0; var6 < var5.countActions(); ++var6) {
                            String var7 = var5.getAction(var6);
                            ArrayList var8 = (ArrayList)this.d.get(var7);
                            if(var8 != null) {
                                for(int var9 = 0; var9 < var8.size(); ++var9) {
                                    if(((A.ClassB)var8.get(var9)).b == var1) {
                                        var8.remove(var9);
                                        --var9;
                                    }
                                }

                                if(var8.size() <= 0) {
                                    this.d.remove(var7);
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    public boolean a(Intent var1) {
        HashMap var2 = this.c;
        synchronized(this.c) {
            if(this.i && this.f != null && this.j) {
                String var3 = var1.getAction();
                String var4 = var1.resolveTypeIfNeeded(this.f.getContentResolver());
                Uri var5 = var1.getData();
                String var6 = var1.getScheme();
                Set var7 = var1.getCategories();
                boolean var8 = (var1.getFlags() & 8) != 0;
                if(var8) {
                    Log.v("QosBroadcastManager", "Resolving type " + var4 + " scheme " + var6 + " of intent " + var1);
                }

                ArrayList var9 = (ArrayList)this.d.get(var1.getAction());
                if(var9 != null) {
                    if(var8) {
                        Log.v("QosBroadcastManager", "Action list: " + var9);
                    }

                    ArrayList var10 = null;

                    int var11;
                    for(var11 = 0; var11 < var9.size(); ++var11) {
                        A.ClassB var12 = (A.ClassB)var9.get(var11);
                        if(var8) {
                            Log.v("QosBroadcastManager", "Matching against filter " + var12.a);
                        }

                        if(var12.c) {
                            if(var8) {
                                Log.v("QosBroadcastManager", "  Filter\'format2 target already added");
                            }
                        } else {
                            int var13 = var12.a.match(var3, var4, var6, var5, var7, "QosBroadcastManager");
                            if(var13 >= 0) {
                                if(var8) {
                                    Log.v("QosBroadcastManager", "  Filter matched!  match=0x" + Integer.toHexString(var13));
                                }

                                if(var10 == null) {
                                    var10 = new ArrayList();
                                }

                                var10.add(var12);
                                var12.c = true;
                            } else if(var8) {
                                String var14;
                                switch(var13) {
                                    case -4:
                                        var14 = "category";
                                        break;
                                    case -3:
                                        var14 = "action";
                                        break;
                                    case -2:
                                        var14 = "data";
                                        break;
                                    case -1:
                                        var14 = "type";
                                        break;
                                    default:
                                        var14 = "unknown reason";
                                }

                                Log.v("QosBroadcastManager", "  Filter did not match: " + var14);
                            }
                        }
                    }

                    if(var10 != null) {
                        for(var11 = 0; var11 < var10.size(); ++var11) {
                            ((A.ClassB)var10.get(var11)).c = false;
                        }

                        this.e.add(new A.ClassA(var1, var10));
                        if(!this.g.hasMessages(1)) {
                            this.g.sendEmptyMessage(1);
                        }

                        return true;
                    }
                }

                return false;
            } else {
                return false;
            }
        }
    }

    private void c() {
        while(true) {
            A.ClassA[] var1 = null;
            HashMap var2 = this.c;
            synchronized(this.c) {
                int var3 = this.e.size();
                if(var3 <= 0) {
                    return;
                }

                var1 = new A.ClassA[var3];
                this.e.toArray(var1);
                this.e.clear();
            }

            for(int var6 = 0; var6 < var1.length; ++var6) {
                A.ClassA var7 = var1[var6];

                for(int var4 = 0; var4 < var7.b.size(); ++var4) {
                    ((A.ClassB)var7.b.get(var4)).b.onReceive(this.f, var7.a);
                }
            }
        }
    }

    private static class ClassA {
        final Intent a;
        final ArrayList<A.ClassB> b;

        ClassA(Intent var1, ArrayList<A.ClassB> var2) {
            this.a = var1;
            this.b = var2;
        }
    }

    private static class ClassB {
        final IntentFilter a;
        final BroadcastReceiver b;
        boolean c;

        ClassB(IntentFilter var1, BroadcastReceiver var2) {
            this.a = var1;
            this.b = var2;
        }

        public String toString() {
            StringBuilder var1 = new StringBuilder(128);
            var1.append("Receiver{");
            var1.append(this.b);
            var1.append(" filter=");
            var1.append(this.a);
            var1.append("}");
            return var1.toString();
        }
    }
}
