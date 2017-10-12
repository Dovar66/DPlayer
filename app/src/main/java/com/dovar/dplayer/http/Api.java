package com.dovar.dplayer.http;

import com.dovar.dplayer.bean.MusicBean;
import com.dovar.dplayer.bean.MusicByIdBean;
import com.dovar.dplayer.bean.VideoBean;
import com.dovar.dplayer.bean.VideoListBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017-09-11.
 */

public interface Api {

    @GET("v4/tabs/selected")
    Observable<VideoBean> getVideoBean();

    @GET("v4/tabs/selected")
    Observable<ResponseBody> getVideoResponse();

//    @GET("channel/music/more/0?channel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android")
//    Observable<MusicListBean> getMusicList();

    //获取首页第一页数据
    @GET(NetConfig.BaseUrl_kaiyan+"v2/feed?num=2&udid=26868b32e808498db32fd51fb422d00175e179df&vc=83")
    Observable<VideoListBean> getVideoList();

    //获取首页第一页之后的数据  ?date=1499043600000&num=2
    @GET(NetConfig.BaseUrl_kaiyan+"v2/feed")
    Observable<VideoListBean> getMoreVideoList(@Query("date") String date,@Query("num") String num);

    //获取歌曲列表
//    "ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=1&offset=0&size=20"
    @GET("ting")
    Observable<MusicBean> getMusicList(@Query("from") String from, @Query("version") String version, @Query("method") String method, @Query("format") String format, @Query("type") String type, @Query("offset") int offset, @Query("size") int size, @Header("User-Agent") String agent);

    //根据歌曲ID获取歌曲播放信息
    @GET("http://ting.baidu.com/data/music/links?songIds={musicId}")
    Observable<MusicByIdBean> getMusicById(@Path("musicId") String musicId);

}
