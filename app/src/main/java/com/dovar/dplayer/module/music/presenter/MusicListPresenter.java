package com.dovar.dplayer.module.music.presenter;

import com.dovar.dplayer.bean.MusicBean;
import com.dovar.dplayer.common.base.DPresenter;
import com.dovar.dplayer.http.Api;
import com.dovar.dplayer.http.RetrofitUtil;
import com.dovar.dplayer.module.music.contract.MusicListContract;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heweizong on 2017/10/11.
 */

public class MusicListPresenter extends DPresenter<MusicListContract.IView> implements MusicListContract.IPresenter {

    private int offset;

    public MusicListPresenter(MusicListContract.IView viewImp) {
        super(viewImp);
    }

    @Override
    public void getMusicList() {
//        RetrofitUtil.testUrlByOkhttp(NetConfig.BAIDU_HOT + "&offset=0&size=20", new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String str = response.body().string();
//                MusicBean bean=new Gson().fromJson(str,MusicBean.class);
//            }
//        });
        RetrofitUtil.getInstance().create(Api.class)
                .getMusicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MusicBean value) {
                        if (isViewAttached()) {
                            getView().onSuccess(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace(); //请求过程中发生错误
                        if (isViewAttached()) {
                            getView().onFail();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
