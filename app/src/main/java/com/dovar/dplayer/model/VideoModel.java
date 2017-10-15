package com.dovar.dplayer.model;

import com.dovar.dplayer.bean.VideoListBean;
import com.dovar.dplayer.http.Api;
import com.dovar.dplayer.http.RetrofitUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-10-14.
 */

public class VideoModel {

    /**
     * 获取第一页视频列表
     */
    public void getVideoList(Observer<VideoListBean> mObserver) {
        RetrofitUtil.getInstance().create(Api.class)
                .getVideoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }
}
