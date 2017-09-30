package com.dovar.dplayer.commen;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.WindowManager;

import com.dovar.dplayer.R;
import com.dovar.dplayer.commen.utils.ActivityLifecycle;
import com.dovar.dplayer.commen.utils.PhoneUtil;
import com.dovar.dplayer.commen.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by Administrator on 2017-09-11.
 */

public class MyApplication extends Application {

    private static MyApplication application;
    private ActivityLifecycle mActivityLifecycle;
    public boolean appInBackground = false;//app被压入后台
    private long appLastInForegroundTime = 0;//后台进前台开始时间
    private long appLastGoBackGroundTime;


    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }


    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        mActivityLifecycle = new ActivityLifecycle();
        registerActivityLifecycleCallbacks(mActivityLifecycle);

        //默认使用的高度是设备的可用高度，也就是不包括状态栏和底部的操作栏的
        // 如果你希望拿设备的物理高度进行百分比化,可以在Application的onCreate方法中进行设置:
        AutoLayoutConifg.getInstance().useDeviceSize();
    }

    /**
     * 获取屏幕宽,部分手机可以动态修改屏幕分辨率
     */
    public int getWidth() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    /**
     * 获取屏幕高
     */
    public int getHeight() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }


    public static synchronized void appGoBackground(final boolean goBackground) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (goBackground) {
                        if (!MyApplication.getInstance().mActivityLifecycle.isApplicationInForeground()
                                || !PhoneUtil.isAppOnForeground(MyApplication.getInstance(), MyApplication.getInstance().getPackageName())) {
                            MyApplication.getInstance().appInBackground = true;
                            ToastUtil.show("应用进入后台");
                            MyApplication.getInstance().appLastGoBackGroundTime = System.currentTimeMillis();
                            if (MyApplication.getInstance().appLastInForegroundTime > 0) {
                                MyApplication.getInstance().appLastInForegroundTime = 0;
                            }
                        }
                    } else {
                        if (MyApplication.getInstance().appInBackground) {
                            ToastUtil.show("从后台进入前台");
                            MyApplication.getInstance().appInBackground = false;
                            if (MyApplication.getInstance().appLastGoBackGroundTime > 0) {
                                long backTime = System.currentTimeMillis() - MyApplication.getInstance().appLastGoBackGroundTime;
                                ToastUtil.show("后台停留时间" + (backTime / 1000) + "format2");
                                MyApplication.getInstance().appLastInForegroundTime = System.currentTimeMillis();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }
}
