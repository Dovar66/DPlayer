package com.dovar.dplayer.contract;

/**
 * Created by Administrator on 2017-09-13.
 */

public interface BaseContract {
    interface IView<T> {
        void onSuccess(T bean);

        void onFail();
    }

    interface IPresenter {
        void onDestory();
    }
}
