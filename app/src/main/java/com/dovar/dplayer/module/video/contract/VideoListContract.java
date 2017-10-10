package com.dovar.dplayer.module.video.contract;

import com.dovar.dplayer.common.base.BaseContract;

/**
 * Created by Administrator on 2017-09-13.
 */

public interface VideoListContract {
    interface IView<T> extends BaseContract.IView<T> {
        void onSuccess(T bean,boolean isLoadMore);

        void onFail(boolean isLoadMore);
    }

    interface IPresenter extends BaseContract.IPresenter {

        void getVideo();

        void getMoreVideo();
    }
}
