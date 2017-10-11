package com.dovar.dplayer.module.music.contract;

import com.dovar.dplayer.bean.MusicBean;
import com.dovar.dplayer.common.base.BaseContract;

/**
 * Created by heweizong on 2017/10/11.
 */

public interface MusicListContract {
    interface IView extends BaseContract.IView {
        void onSuccess(MusicBean data);

        void onFail();
    }

    interface IPresenter extends BaseContract.IPresenter {

        void getMusicList();
    }
}
