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

    private int offset=0;
    private int pageSize=20;

    public MusicListPresenter(MusicListContract.IView viewImp) {
        super(viewImp);
    }

    @Override
    public void getMusicList() {
//        String url="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=1&offset=0&size=20";
//
//        RetrofitUtil.testUrlByVolley(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                LogUtil.d("hwz",response);
//            }
//        });

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
                .getMusicList("qianqian","2.1.0","baidu.ting.billboard.billList","json","1",offset,pageSize,"app)")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MusicBean value) {
                        if (isViewAttached()) {
                            offset+=pageSize;
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
