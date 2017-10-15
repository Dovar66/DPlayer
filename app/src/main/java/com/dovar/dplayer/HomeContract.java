package com.dovar.dplayer;

import com.dovar.dplayer.bean.Music;
import com.dovar.dplayer.bean.VideoListBean;

import java.util.List;

/**
 * Created by Administrator on 2017-10-15.
 */

public interface HomeContract {
    interface IView {
        void getMusicListSuccess(List<Music> mMusicList,boolean isLoadMore);

        void getMusicListFail(boolean isLoadMore);

        void getVideoListSuccess(List<VideoListBean.IssueListBean.ItemListBean> mVideoList,boolean isLoadMore);

        void getVideoListFail(boolean isLoadMore);
    }

    interface IPresenter {
        void getMusicList();

        void getVideoList();
    }
}
