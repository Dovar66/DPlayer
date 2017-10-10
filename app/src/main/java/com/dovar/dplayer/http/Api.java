package com.dovar.dplayer.http;

import com.dovar.dplayer.bean.MusicListBean;
import com.dovar.dplayer.bean.VideoBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017-09-11.
 */

public interface Api {

    @GET("www.baidu.com")
    Call<Reception> getMusic();

    @GET("www.baidu.com")
    Observable<Reception> getMusics();

    @GET("v4/tabs/selected")
    Observable<VideoBean> getVideoBean();

    @GET("v4/tabs/selected")
    Observable<ResponseBody> getVideoResponse();

    @GET("v4/tabs/selected")
    Observable<String> getVideoResponseStr();

    @GET("channel/music/more/0?channel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android")
    Observable<MusicListBean> getMusicList();

}
