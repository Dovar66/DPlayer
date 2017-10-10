package com.dovar.dplayer.module.video.presenter;

import com.dovar.dplayer.bean.VideoDataBean;
import com.dovar.dplayer.common.base.DPresenter;
import com.dovar.dplayer.http.Api;
import com.dovar.dplayer.http.RetrofitUtil;
import com.dovar.dplayer.module.video.contract.VideoContract;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017-09-13.
 */

public class VideoPresenter extends DPresenter<VideoContract.IView<ArrayList<VideoDataBean>>> implements VideoContract.IPresenter {
    public VideoPresenter(VideoContract.IView<ArrayList<VideoDataBean>> viewImp) {
        super(viewImp);
    }

    @Override
    public void getVideo() {
        //by okHttp
//        OkHttpClient mClient = new OkHttpClient();
//        Request mRequest = new Request.Builder()
//                .url(NetConfig.BaseUrl_kaiyan + "v4/tabs/selected")
//                .get()
//                .build();
//        mClient.newCall(mRequest).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String str = response.body().string();
//                try {
//                    JSONObject jso = new JSONObject(str);
//                    JSONArray jsa = jso.optJSONArray("itemList");
//                    ArrayList<VideoDataBean> datas = new ArrayList<>();
//                    for (int i = 0; i < jsa.length(); i++) {
//                        JSONObject obj = jsa.optJSONObject(i);
//                        if (obj.optString("type").equals("video")) {
//                            VideoDataBean data = new Gson().fromJson(obj.optString("data"), VideoDataBean.class);
//                            datas.add(data);
//                        }
//                    }
//
//                } catch (JSONException mE) {
//                    mE.printStackTrace();
//                }
//
//            }
//        });

        //by retrofit
        RetrofitUtil.getInstance().create(Api.class)
                .getVideoResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable showHideController) {

                    }

                    @Override
                    public void onNext(ResponseBody value) {
                        try {
                            String str = value.string();
                            JSONObject jso = new JSONObject(str);
                            JSONArray jsa = jso.optJSONArray("itemList");
                            ArrayList<VideoDataBean> datas = new ArrayList<>();
                            for (int i = 0; i < jsa.length(); i++) {
                                JSONObject obj = jsa.optJSONObject(i);
                                if (obj.optString("type").equals("video")) {
                                    VideoDataBean data = new Gson().fromJson(obj.optString("data"), VideoDataBean.class);
                                    datas.add(data);
                                }
                            }

                            getView().onSuccess(datas);
                        } catch (JSONException | IOException mE) {
                            mE.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable dropVideoFrames) {
                        dropVideoFrames.printStackTrace(); //请求过程中发生错误
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
