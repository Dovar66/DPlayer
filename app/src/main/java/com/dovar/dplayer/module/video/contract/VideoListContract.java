package com.dovar.dplayer.module.video.contract;

import com.dovar.dplayer.commen.base.BaseContract;

/**
 * Created by Administrator on 2017-09-13.
 */

public interface VideoListContract {
    interface IView<T> extends BaseContract.IView<T> {

    }

    interface IPresenter extends BaseContract.IPresenter {

        void getVideo();
    }
}
