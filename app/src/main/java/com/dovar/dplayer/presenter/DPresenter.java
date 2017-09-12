package com.dovar.dplayer.presenter;

import android.support.annotation.NonNull;

import com.dovar.dplayer.contract.BaseContract;
import com.example.touchtv.R;
import com.gdtv.common.application.MyApplication;
import com.gdtv.common.base.presenter.MyBasePresenter;
import com.gdtv.common.network.ErrorResponeBean;
import com.gdtv.common.network.RequestListener;
import com.gdtv.common.util.NetWorkUtil;
import com.gdtv.common.util.ToastUtil;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by heweizong on 2017/5/5.
 */

public abstract class DPresenter<T> implements BaseContract.IPresenter {
    private WeakReference<T> view;

    public DPresenter(T viewImp) {
        attachView(viewImp);
    }

    private void attachView(T viewImp) {
        view = new WeakReference<>(viewImp);
    }

    protected T getView() {
        return view.get();
    }

    protected boolean isViewAttached() {
        return view != null && view.get() != null;
    }

    private void detachView() {
        if (view != null) {
            view.clear();
            view = null;
        }
    }

    @Override
    public void onDestory() {
        detachView();
    }


    /**
     * 限制只在BaseLivePresenter的子类中使用，所以定义为内部类
     * 1.统一捕获子类onLiveSuccess()、onLiveFailure()抛出的异常
     * 2.在子类中只需要处理isViewAttached（）==true时的逻辑
     * 3.子类onLiveSuccess()中jsonObject一定不为空
     */
    public abstract class LiveRequestListener implements RequestListener {

        protected abstract void onSucceed(@NonNull T attachView, @NonNull JSONObject json);

        protected abstract void onFailed(@NonNull T attachView, boolean hasNetWork, ErrorResponeBean error);

        @Override
        public void onSuccess(JSONObject jsonObject) {
            try {
                if (isViewAttached()) {
                    if (jsonObject != null) {
                        onSucceed(getView(), jsonObject);
                    } else {
                        onFailure(null);
                    }
                }
            } catch (Exception mE) {
                mE.printStackTrace();
                onFailure(null);
            }
        }

        @Override
        public void onFailure(ErrorResponeBean error) {
            try {
                if (isViewAttached()) {
                    boolean noNetWork = NetWorkUtil.NETWORN_NONE == NetWorkUtil.getActiveNetwork(MyApplication.getInstance());
                    if (noNetWork && !MyApplication.getInstance().appInBackground) {
                        ToastUtil.show(MyApplication.getInstance(), R.string.error_network);
                    }
                    onFailed(getView(), !noNetWork, error);
                }
            } catch (Exception mE) {
                mE.printStackTrace();
            }
        }
    }
}
