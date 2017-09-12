package com.dovar.dplayer.ui;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.VideoBean;
import com.dovar.dplayer.http.Api;
import com.dovar.dplayer.http.Reception;
import com.dovar.dplayer.http.RetrofitUtil;
import com.dovar.dplayer.utils.DisplayUtil;
import com.zhy.autolayout.AutoRelativeLayout;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

        //创建retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") // 设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava平台
                .build();

        // 创建 网络请求接口 的实例
        Api call = retrofit.create(Api.class);
        //对 发送请求 进行封装
        Call<Reception> mCall = call.getMusic();
        //发送网络请求(异步)
        mCall.enqueue(new Callback<Reception>() {
            @Override
            public void onResponse(@NonNull Call<Reception> call, @NonNull Response<Reception> response) {

            }

            @Override
            public void onFailure(Call<Reception> call, Throwable t) {

            }
        });
        //发送网络请求(同步)
//        Response<Reception> reception=mCall.execute();

        call.getMusics().subscribeOn(Schedulers.io())
                .subscribe(new Observer<Reception>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Reception value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        RetrofitUtil.getInstance().create(Api.class)
                .getVideoBean()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoBean value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace(); //请求过程中发生错误
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
}
