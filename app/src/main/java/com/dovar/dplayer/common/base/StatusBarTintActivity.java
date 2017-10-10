package com.dovar.dplayer.common.base;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;

import com.dovar.dplayer.R;
import com.dovar.dplayer.common.utils.DisplayUtil;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by heweizong on 2017/7/13.
 * 沉浸式标题栏，支持背景图片或色值着色
 * 需要在swipeBack之前处理，否则侧滑返回时两个页面的标题栏颜色一样
 */

public abstract class StatusBarTintActivity extends BaseModuleActivity {
    private ImageView iv_bg;
    private Space space_status;
    private boolean tintStatusBarByColor;//or tintStatusBarByImage
    private boolean enableTintStatusBar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window win = getWindow();
//            WindowManager.LayoutParams winParams = win.getAttributes();
//            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//            winParams.flags |= bits;
//            win.setAttributes(winParams);
//        }
        setStatusBar(true);

        if (enableTintStatusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //activity根布局
            AutoRelativeLayout dectorView = new AutoRelativeLayout(this);

            //添加背景图
            iv_bg = new ImageView(this);
            RelativeLayout.LayoutParams lp_bg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            iv_bg.setId(R.id.id_statusbar_tint_bg);
            dectorView.addView(iv_bg, lp_bg);

            //标题栏占位布局
            space_status = new Space(this);
            space_status.setId(R.id.id_statusbar_tint_space);
            RelativeLayout.LayoutParams lp_space = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            dectorView.addView(space_status, lp_space);

            //contentView
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.BELOW, R.id.id_statusbar_tint_space);
            View subContentView = View.inflate(this, layoutResID, null);
            dectorView.addView(subContentView, lp);

            //将背景色转移到dectorView
            dectorView.setBackground(subContentView.getBackground());
            subContentView.setBackground(null);

            super.setContentView(dectorView);
        } else {
            super.setContentView(layoutResID);
        }
    }

    public ImageView getBackgroundImageView() {
        return iv_bg;
    }

    public void setStatusBarTintResource(int res) {
        if (space_status != null) {
            space_status.setBackgroundResource(res);
            tintStatusBarByColor = true;

            setStatusBarTintEnabled(true);
        }
    }

    public void setStatusBarTintEnabled(boolean enabled) {
        this.enableTintStatusBar = enabled;
        if (iv_bg == null || space_status == null) return;


        if (enabled) {
            if (tintStatusBarByColor) {
                iv_bg.setVisibility(View.GONE);
                space_status.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams lp_space = space_status.getLayoutParams();
                lp_space.height = DisplayUtil.getStatusBarHeight(this);
                space_status.setLayoutParams(lp_space);
            } else {
                iv_bg.setVisibility(View.VISIBLE);
                space_status.setVisibility(View.GONE);
            }
        } else {
            iv_bg.setVisibility(View.GONE);
            space_status.setVisibility(View.GONE);
        }
    }

    /**
     * 设置透明状态栏与导航栏
     *
     * @param navi true不设置导航栏|false设置导航栏
     */
    public void setStatusBar(boolean navi) {
        //api>21,全透明状态栏和导航栏;api>19,半透明状态栏和导航栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            if (navi) {
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//状态栏不会被隐藏但activity布局会扩展到状态栏所在位置
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//导航栏不会被隐藏但activity布局会扩展到导航栏所在位置
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                window.setNavigationBarColor(Color.TRANSPARENT);
//            } else {
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            }
//        } else
 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (navi) {
                //半透明导航栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            //半透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
