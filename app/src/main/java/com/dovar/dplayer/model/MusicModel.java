package com.dovar.dplayer.model;

import com.dovar.dplayer.bean.MusicBean;
import com.dovar.dplayer.http.Api;
import com.dovar.dplayer.http.RetrofitUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-10-14.
 */

public class MusicModel {

    /**
     * 获取音乐列表
     *
     * @param offset   从offset下一条数据开始返回
     * @param pageSize 每页返回个数
     */
    public void getMusicList(int offset, int pageSize, Observer<MusicBean> mObserver) {
        RetrofitUtil.getInstance().create(Api.class)
                .getMusicList("qianqian", "2.1.0", "baidu.ting.billboard.billList", "json", "1", offset, pageSize, "app")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }
}
