package com.dovar.dplayer.contract;

/**
 * Created by Administrator on 2017-09-13.
 */

public interface VideoContract {
    interface IView<T> extends BaseContract.IView<T> {

    }

    interface IPresenter extends BaseContract.IPresenter {

        void getVideo();
    }
}
