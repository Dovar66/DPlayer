package com.dovar.dplayer.common.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.dovar.dplayer.common.MyApplication;

/**
 * 考虑到不同ROM的Toast样式不同，本着保留Toast多样性的原则，特别是如Smartision OS默认的样式相对美观
 * 所以此工具类在做Toast管理与扩展时优先考虑采用Rom的默认样式
 *
 * note:华为EMUI关闭应用的通知总开关后，系统的Toast也无法展示，此时需要采用自定义Toast
 */
public class ToastUtil {
    private static Toast mToast;
    private static final int Tag_null = 0;
    /**
     * 修改mToast 的gravity或者使用不同contentView时，请设置不同的Tag，勿使用Tag_default
     */
    private static final int Tag_default = 1;//只能用于默认样式的Toast
    private static final int Tag_showAtCenter = 2;

    private static Toast initToast() {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
            mToast.getView().setTag(Tag_default);
        }
        return mToast;
    }

    //统一管理
    private static void showToast(Toast mToast) {
        if (mToast != null) {
            mToast.show();
        }
    }

    private static int getTagIgnoreException(Toast toast) {
        try {
            return (int) toast.getView().getTag();
        } catch (Exception e) {
            e.printStackTrace();
            return Tag_null;
        }
    }

    /**
     * 弹出Toast消息
     *
     * @param msg 要显示的消息
     */
    public static void show(String msg) {
        if (MyApplication.getInstance().appInBackground) return;
        initToast();
        int tag = getTagIgnoreException(mToast);
        if (tag != Tag_default) {
            mToast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
            mToast.getView().setTag(Tag_default);
        }
        mToast.setText(msg);
        showToast(mToast);
    }

    /**
     * @param msg String ResourceID
     * @see #show(String)
     */
    public static void show(int msg) {
        initToast();
        int tag = getTagIgnoreException(mToast);
        if (tag != Tag_default) {
            mToast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
            mToast.getView().setTag(Tag_default);
        }
        mToast.setText(msg);
        showToast(mToast);
    }

    /**
     * 弹出Toast消息
     *
     * @param msg  要显示的消息
     * @param time 消失显示时间
     */
    public static void show(String msg, int time) {
        initToast();
        int tag = getTagIgnoreException(mToast);
        if (tag != Tag_default) {
            mToast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
            mToast.getView().setTag(Tag_default);
        }
        mToast.setText(msg);
        mToast.setDuration(time);
        showToast(mToast);
    }

    //展示在页面中间
    public static void showAtCenter(String msg) {
        initToast();
        int tag = getTagIgnoreException(mToast);
        if (tag != Tag_showAtCenter) {
            mToast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
            mToast.getView().setTag(Tag_showAtCenter);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }
        mToast.setText(msg);
        showToast(mToast);
    }

}
