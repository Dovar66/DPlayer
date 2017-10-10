package com.dovar.dplayer.http;

import com.dovar.dplayer.bean.MusicListBean;
import com.dovar.dplayer.bean.VideoBean;
import com.dovar.dplayer.bean.VideoListBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-09-11.
 */

public interface Api {

    @GET("v4/tabs/selected")
    Observable<VideoBean> getVideoBean();

    @GET("v4/tabs/selected")
    Observable<ResponseBody> getVideoResponse();

    @GET("channel/music/more/0?channel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android")
    Observable<MusicListBean> getMusicList();

    //获取首页第一页数据
    @GET("v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83")
    Observable<VideoListBean> getVideoList();

    //获取首页第一页之后的数据  ?date=1499043600000&num=2
    @GET("v2/feed")
    Observable<VideoListBean> getMoreVideoList(@Query("date") String date,@Query("num") String num);
}
