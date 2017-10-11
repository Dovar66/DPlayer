package com.dovar.dplayer.module.music.contract;

import com.dovar.dplayer.bean.MusicByIdBean;
import com.dovar.dplayer.common.base.BaseContract;

/**
 * Created by heweizong on 2017/10/11.
 */

public interface MusicContract {
    interface IView extends BaseContract.IView {
        void onSuccess(MusicByIdBean music);

        void onFail();
    }

    interface IPresenter extends BaseContract.IPresenter {

        void getMusic(String music_id);
    }
}
