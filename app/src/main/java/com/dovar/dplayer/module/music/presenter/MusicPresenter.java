package com.dovar.dplayer.module.music.presenter;

import com.dovar.dplayer.bean.MusicByIdBean;
import com.dovar.dplayer.common.base.DPresenter;
import com.dovar.dplayer.http.Api;
import com.dovar.dplayer.http.RetrofitUtil;
import com.dovar.dplayer.module.music.contract.MusicContract;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heweizong on 2017/10/11.
 */

public class MusicPresenter extends DPresenter<MusicContract.IView> implements MusicContract.IPresenter {
    public MusicPresenter(MusicContract.IView viewImp) {
        super(viewImp);
    }

    @Override
    public void getMusic(String music_id) {
        RetrofitUtil.getInstance().create(Api.class)
                .getMusicById(music_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicByIdBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MusicByIdBean value) {
                        if (isViewAttached()) {
                            getView().onSuccess(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
