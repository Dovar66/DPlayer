package com.dovar.dplayer.http;

import io.reactivex.Observable;
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
}
