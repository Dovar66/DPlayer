package com.dovar.pili;

import android.util.Log;

/**
 * Created by heweizong on 2017/9/29.
 * load library name
 */

public class SharedLibraryNameHelper {
    private String a;

    private SharedLibraryNameHelper() {
        this.a = "pldroidplayer";
    }

    public static SharedLibraryNameHelper getInstance() {
        return SharedLibraryNameHelper.ClassA.a;
    }

    public void renameSharedLibrary(String var1) {
        Log.i("SharedLibraryNameHelper", "renameSharedLibrary newName:" + var1);
        this.a = var1;
    }

    public String getSharedLibraryName() {
        return this.a;
    }

    public void a() {
        if(this.a.contains("/")) {
            System.load(this.a);
        } else {
            System.loadLibrary(this.a);
        }

    }

    private static class ClassA {
        public static final SharedLibraryNameHelper a = new SharedLibraryNameHelper();
    }
}
