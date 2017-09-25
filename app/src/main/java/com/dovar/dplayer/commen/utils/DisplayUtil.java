package com.dovar.dplayer.commen.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dovar.dplayer.R;


/**
 * dp、sp、px转换
 * Created by Administrator on 2016/9/12 0012.
 */
public class DisplayUtil {

    private static Toast mToast;

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue scale（DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue fontScale      （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, int pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 弹出对话框
     *
     * @param mView 自定义对话框布局
     */
    public static AlertDialog showDialog(View mView, Context context, int width) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context, R.style.TransparentDialog);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
        Window window = mDialog.getWindow();
        if (window == null) return mDialog;
        window.setContentView(mView);
        WindowManager.LayoutParams p = window.getAttributes();  //获取对话框当前的参数值
        p.width = width;
        window.setAttributes(p);     //设置生效
        return mDialog;
    }

    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        try
        {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0)
            {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e)
        {
            e.printStackTrace();
        }
        return result;
    }

}
