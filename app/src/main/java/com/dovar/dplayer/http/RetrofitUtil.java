package com.dovar.dplayer.http;

import com.dovar.dplayer.NetConfig;
import com.dovar.dplayer.bean.VideoBean;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017-09-12.
 */

public class RetrofitUtil {
    private static Retrofit mRetrofit;

    public static Retrofit getInstance() {
        if (mRetrofit == null) {
            synchronized (RetrofitUtil.class) {
                if (mRetrofit == null) {
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(NetConfig.BaseUrl_kaiyan)
//                            .client()
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return mRetrofit;
    }

    public Observable<VideoBean> getVideo() {
        return getInstance().create(Api.class).getVideoBean();
    }
}
