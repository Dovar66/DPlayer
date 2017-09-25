package com.dovar.dplayer.commen.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

/**
 * Created by heweizong on 2017/9/22.
 */

@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private long resumed;
    private long paused;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        resumed++;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused++;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public boolean isApplicationInForeground() {
        // 当所有 Activity 的状态中处于 resumed 的大于 paused 状态的，即可认为有Activity处于前台状态中
        return resumed > paused;
    }
}
