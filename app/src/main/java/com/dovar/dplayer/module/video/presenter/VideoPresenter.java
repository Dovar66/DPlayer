package com.dovar.dplayer.module.video.presenter;

import com.dovar.dplayer.bean.VideoBean;
import com.dovar.dplayer.commen.base.DPresenter;
import com.dovar.dplayer.http.Api;
import com.dovar.dplayer.http.RetrofitUtil;
import com.dovar.dplayer.module.video.contract.VideoContract;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-09-13.
 */

public class VideoPresenter extends DPresenter<VideoContract.IView<VideoBean>> implements VideoContract.IPresenter {
    public VideoPresenter(VideoContract.IView<VideoBean> viewImp) {
        super(viewImp);
    }

    @Override
    public void getVideo() {
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
                        getView().onSuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace(); //请求过程中发生错误
                    }

                    @Override
                    public void onComplete() {
                    }
                });

//        RetrofitUtil.getInstance().create(Api.class)
//                .getVideoResponseStr()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable showHideController) {
//
//                    }
//
//                    @Override
//                    public void onNext(String value) {
//                        getView().onSuccess(null);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace(); //请求过程中发生错误
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                });
    }
}
