package com.dovar.dplayer;

import com.dovar.dplayer.bean.Music;
import com.dovar.dplayer.bean.MusicBean;
import com.dovar.dplayer.bean.VideoListBean;
import com.dovar.dplayer.common.base.DPresenter;
import com.dovar.dplayer.model.MusicModel;
import com.dovar.dplayer.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017-10-15.
 */

public class HomePresenter extends DPresenter<HomeContract.IView> implements HomeContract.IPresenter {
    private MusicModel mMusicModel;
    private VideoModel mVideoModel;

    public HomePresenter(HomeContract.IView viewImp) {
        super(viewImp);
        mMusicModel = new MusicModel();
        mVideoModel = new VideoModel();
    }


    @Override
    public void getMusicList() {
        mMusicModel.getMusicList(0, 20, new Observer<MusicBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(MusicBean value) {
                if (isViewAttached()) {
                    List<Music> mMusicList = new ArrayList<>();
                    for (MusicBean.SongList song : value.getSong_list()
                            ) {
                        final Music music = new Music();
                        music.setName(song.getTitle());//歌曲名
                        music.setSinger(formatSingers(song.getArtist_name()));//歌手名
                        music.setSong_id(song.getSong_id());//歌曲id
                        music.setLrclink(song.getLrclink());//歌词链接
                        music.setPic_big(song.getPic_big());//大图标
                        music.setPic_small(song.getPic_small());//小图标
                        mMusicList.add(music);
                    }
                    getView().getMusicListSuccess(mMusicList, false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().getMusicListFail(false);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getVideoList() {
        mVideoModel.getVideoList(new Observer<VideoListBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(VideoListBean value) {
                if (isViewAttached()) {
                    ArrayList<VideoListBean.IssueListBean.ItemListBean> videos = new ArrayList<>();
                    List<VideoListBean.IssueListBean> list = value.getIssueList();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) != null) {
                            for (VideoListBean.IssueListBean.ItemListBean data : list.get(i).getItemList()
                                    ) {
                                if (data.getType().equals("video")) {
                                    videos.add(data);
                                }
                            }
                        }
                    }
                    getView().getVideoListSuccess(videos, false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().getVideoListFail(false);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 合唱歌曲时最多只显示两个歌手名
     *
     * @param singers 歌手名
     */
    public static String formatSingers(String singers) {
        String[] singerArray = singers.split(",");
        if (singerArray.length > 2) {
            return singerArray[0] + "," + singerArray[1];
        }
        return singers;
    }
}
