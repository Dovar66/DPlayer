package com.dovar.dplayer.common.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.dovar.dplayer.R;
import com.dovar.dplayer.common.MyApplication;
import com.dovar.dplayer.common.utils.DisplayUtil;
import com.dovar.dplayer.common.utils.NetWorkUtil;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by heweizong on 2017/10/11.
 */

public abstract class BaseFragment extends Fragment {
    public View mainView;
    private NetChangeReceiver netChangeReceiver;
    private NetWorkListener netWorkListener = null;
    private int netWorkState;
    public boolean isFragmentPause = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        netWorkState = NetWorkUtil.getActiveNetwork(MyApplication.getInstance());
        if (netChangeReceiver == null) {
            netChangeReceiver = new NetChangeReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            getActivity().registerReceiver(netChangeReceiver, filter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentPause = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (netChangeReceiver != null) {
            try {
                getActivity().unregisterReceiver(netChangeReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            netChangeReceiver = null;
            netWorkListener = null;
        }
    }

    /**
     * 防止多次点击
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public void showSoftInput(EditText editText) {
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    public void hiddenSoftInput(EditText editText) {
        editText.clearFocus();
        InputMethodManager inputManager =
                (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public interface NetWorkListener {
        void isWifi();

        void isCellar();

        void isOffline();
    }

    class NetChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                ConnectivityManager mConnectivityManager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        /////WiFi网络
                        if (netWorkListener != null) {
                            if (netWorkState != NetWorkUtil.NETWORN_WIFI) {
                                if (!isFragmentPause)
                                    netWorkListener.isWifi();
                            }
                        }
                        netWorkState = NetWorkUtil.getActiveNetwork(MyApplication.getInstance());
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        /////////3g网络
                        if (netWorkListener != null) {
                            if (!(netWorkState != NetWorkUtil.NETWORN_NONE && netWorkState != NetWorkUtil.NETWORN_WIFI)) {
                                if (!isFragmentPause)
                                    netWorkListener.isCellar();
                            }
                        }
                        netWorkState = NetWorkUtil.getActiveNetwork(MyApplication.getInstance());
                    }
                } else {
                    ////////网络断开
                    if (netWorkListener != null) {
                        if (netWorkState != NetWorkUtil.NETWORN_NONE)
                            if (!isFragmentPause)
                                netWorkListener.isOffline();
                    }
                    netWorkState = NetWorkUtil.getActiveNetwork(MyApplication.getInstance());
                }
            }
        }
    }

    public void setNetWorkListener(NetWorkListener listener) {
        netWorkListener = listener;
    }

    public void showViewAnim(final View view, int animId, int duration) {
        Animation animation = AnimationUtils.loadAnimation(MyApplication.getInstance(), animId);
        animation.setDuration(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (view != null)
                    view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);
    }

    public void showFlipViewAnim(final View view, int duration) {
        view.setVisibility(View.VISIBLE);
        ScaleAnimation sato0 = new ScaleAnimation(0, 1, 1, 1,
                Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        sato0.setDuration(duration);


//         ScaleAnimation sato0 = new ScaleAnimation(1,1,1,0,
//                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        sato0.setDuration(duration);
        final ScaleAnimation sato1 = new ScaleAnimation(1, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sato1.setDuration(duration);
        sato0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                if(view!=null){
//                    view.setVisibility(View.VISIBLE);
////                    view.startAnimation(sato1);
//                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                view.setAnimation(null);
//                view.startAnimation(sato1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(sato0);
    }

    public void hiddenViewAnim(final View view, int animId, int duration) {
        Animation animation = AnimationUtils.loadAnimation(MyApplication.getInstance(), animId);
        animation.setDuration(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (view != null)
                    view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    protected abstract void initUI();

    protected abstract void initData();


    public <V extends View> V findView(int viewId, View parent) {
        if (parent == null) return null;
        return (V) parent.findViewById(viewId);
    }

    protected void setText(TextView tv, String text) {
        if (tv != null) {
            if (text == null) {
                text = "";
            }
            tv.setText(text);
        }
    }

    private ImageView iv_bg;
    private Space space_status;
    private boolean tintStatusBarByColor = true;//or tintStatusBarByImage
    private boolean tintStatusBarByImage = true;//or tintStatusBarByImage

    public ImageView getBackgroundImageView() {
        return iv_bg;
    }

    public View createContentView(@LayoutRes int layoutResID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //activity根布局
            AutoRelativeLayout dectorView = new AutoRelativeLayout(getActivity());

            //添加背景图
            iv_bg = new ImageView(getActivity());
            iv_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            RelativeLayout.LayoutParams lp_bg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            iv_bg.setId(R.id.id_statusbar_tint_bg);
            iv_bg.setBackgroundResource(R.color.white);
            dectorView.addView(iv_bg, lp_bg);

            //标题栏占位布局
            space_status = new Space(getActivity());
            space_status.setId(R.id.id_statusbar_tint_space);
            RelativeLayout.LayoutParams lp_space = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.getStatusBarHeight(getActivity()));
            dectorView.addView(space_status, lp_space);

            //contentView
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.BELOW, R.id.id_statusbar_tint_space);
            View subContentView = View.inflate(getActivity(), layoutResID, null);
            dectorView.addView(subContentView, lp);

            //将背景色转移到dectorView
            dectorView.setBackground(subContentView.getBackground());
            subContentView.setBackground(null);

            return dectorView;
        } else {
            return View.inflate(getActivity(), layoutResID, null);
        }
    }

    public void setStatusBarTintEnabled(boolean enabled) {
        if (iv_bg == null || space_status == null) return;

        if (enabled) {
            if (tintStatusBarByColor) {
                space_status.setVisibility(View.VISIBLE);
            } else {
                space_status.setVisibility(View.GONE);
            }

            if (tintStatusBarByImage) {
                iv_bg.setVisibility(View.VISIBLE);
            } else {
                iv_bg.setVisibility(View.GONE);
            }
        } else {
            iv_bg.setVisibility(View.GONE);
            space_status.setVisibility(View.GONE);
        }
    }

    public void setStatusBarColor(@DrawableRes int resId) {
        tintStatusBarByColor = true;
        if (space_status == null) return;

        space_status.setBackgroundResource(resId);
        space_status.setVisibility(View.VISIBLE);
    }

}
