package com.dovar.pili;

import android.content.Context;
import android.net.Uri;

import com.dovar.pili.network.NetworkA;

import java.net.UnknownHostException;

/**
 * Created by heweizong on 2017/9/29.
 */

public class PLNetworkManager {
    private NetworkA a;

    private PLNetworkManager() {
        this.a = new NetworkA();
    }

    public static PLNetworkManager getInstance() {
        return PLNetworkManager.ClassA.a;
    }

    public void setDnsServer(String var1) throws UnknownHostException {
        this.a.a(var1);
    }

    public void setDnsCacheUpdateInterval(int var1) {
        this.a.a(var1);
    }

    public void startDnsCacheService(Context var1) throws UnknownHostException {
        this.a.a(var1);
    }

    public void startDnsCacheService(Context var1, String[] var2) throws UnknownHostException {
        this.a.a(var1, var2);
    }

    public void stopDnsCacheService(Context var1) {
        this.a.b(var1);
    }

    String a(String var1) {
        return this.a.b(var1);
    }

    Uri a(Uri var1) {
        return this.a.a(var1);
    }

    private static class ClassA {
        public static final PLNetworkManager a = new PLNetworkManager();
    }
}
