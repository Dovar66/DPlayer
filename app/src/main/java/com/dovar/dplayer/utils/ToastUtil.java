package com.dovar.dplayer.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.dovar.dplayer.MyApplication;

public class ToastUtil {
    private static Toast mToast;
    private static final int Tag_null = 0;
    /**
     * 修改mToast 的gravity或者使用不同contentView时，请设置不同的Tag，勿使用Tag_default
     */
    private static final int Tag_default = 1;//只能用于默认样式的Toast
    private static final int Tag_point_tip = 2;
    private static final int Tag_comment_succeed = 3;
    private static final int Tag_noEnough_point = 4;
    private static final int Tag_showRechargeSuccess = 5;
    private static final int Tag_showRechargeUnsuccess = 6;
    private static final int Tag_showRechargeCancel = 7;
    private static final int Tag_showAtCenter = 8;

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
