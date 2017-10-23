package com.dovar.dplayer.http;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dovar.dplayer.bean.VideoBean;
import com.dovar.dplayer.common.MyApplication;

import io.reactivex.Observable;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017-09-12.
 */

/**
 * Retrofit简单使用步骤：
 * <p>
 * 1.创建retrofit实例
 * Retrofit retrofit = new Retrofit.Builder()
 * .baseUrl("http://fanyi.youdao.com/") // 设置网络请求的Url地址
 * .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
 * .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava平台
 * .build();
 * 2.创建 网络请求接口 的实例
 * Api call = retrofit.create(Api.class);
 * 3.对 发送请求 进行封装
 * Call<Reception> mCall = call.getMusic();
 * 4.1发送网络请求(异步)
 * mCall.enqueue(new Callback<Reception>() {
 *
 * @Override public void onResponse(@NonNull Call<Reception> call, @NonNull Response<Reception> response) {
 * <p>
 * }
 * @Override public void onFailure(Call<Reception> call, Throwable t) {
 * <p>
 * }
 * });
 * 4.2发送网络请求(同步)
 * Response<Reception> reception=mCall.execute();
 * 5. 取消
 * mCall.cancel();
 *
 * <p>
 * 常见的使用方式：
 * call.getMusics().subscribeOn(Schedulers.io())
 * .subscribe(new Observer<Reception>() {
 * @Override public void onSubscribe(Disposable d) {
 * <p>
 * }
 * @Override public void onNext(Reception value) {
 * <p>
 * }
 * @Override public void onError(Throwable e) {
 * <p>
 * }
 * @Override public void onComplete() {
 * <p>
 * }
 * });
 */
public class RetrofitUtil {
    private static Retrofit mRetrofit;

    public static Retrofit getInstance() {
        if (mRetrofit == null) {
            synchronized (RetrofitUtil.class) {
                if (mRetrofit == null) {
                    //获取Retrofit实例
                    mRetrofit = new Retrofit.Builder()
                            //设置OKHttpClient,如果不设置会提供一个默认的
                            .client(new OkHttpClient())
                            //设置baseUrl
                            .baseUrl(NetConfig.BAIDU_MUSIC)
                            //添加Gson转换器
                            .addConverterFactory(GsonConverterFactory.create())
                            // 支持RxJava
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

    //用okhttp
    public static void testUrlByOkhttp(String url, Callback mCallback) {
        OkHttpClient mClient = new OkHttpClient();
        Request mRequest = new Request.Builder()
                .url(url)
                .get()
                .build();
        mClient.newCall(mRequest).enqueue(mCallback);
    }

    public static void testUrlByVolley(int mMethod, String url, Response.Listener<String> mListener) {
        RequestQueue mQueue = Volley.newRequestQueue(MyApplication.getInstance());
        StringRequest mStringRequest = new StringRequest(mMethod, url, mListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getNetworkTimeMs();
            }
        });
        mQueue.add(mStringRequest);
    }


    /*
    okHttp() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                //打印日志
                .addInterceptor(interceptor)

                //设置Cache目录
                .cache(CacheUtil.getCache(UIUtil.getContext()))

                //设置缓存
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheInterceptor)

                //失败重连
                .retryOnConnectionFailure(true)

                //time out
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)

                .build()

        ;
    }
     */
}


