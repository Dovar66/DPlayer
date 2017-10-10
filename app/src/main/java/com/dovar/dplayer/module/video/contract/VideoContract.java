package com.dovar.dplayer.module.video.contract;

import com.dovar.dplayer.common.base.BaseContract;

/**
 * Created by Administrator on 2017-09-13.
 */

public interface VideoContract {
    interface IView<T> extends BaseContract.IView<T> {
        void onSuccess();

        void onFail();
    }

    interface IPresenter extends BaseContract.IPresenter {

        void getVideo();

        void getMoreVideo();
    }
}
