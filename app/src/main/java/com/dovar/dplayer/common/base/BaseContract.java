package com.dovar.dplayer.common.base;

/**
 * Created by Administrator on 2017-09-13.
 */

public interface BaseContract {
    interface IView<T> {

    }

    interface IPresenter {
        void onDestory();
    }
}
