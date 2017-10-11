package com.dovar.dplayer.module.music.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.Music;
import com.dovar.dplayer.bean.MusicBean;
import com.dovar.dplayer.common.adapter.RCommonAdapter;
import com.dovar.dplayer.common.adapter.RCommonViewHolder;
import com.dovar.dplayer.common.base.BaseFragment;
import com.dovar.dplayer.module.music.contract.MusicListContract;
import com.dovar.dplayer.module.music.presenter.MusicListPresenter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by heweizong on 2017/10/11.
 */

public class MusicListFragment extends BaseFragment implements MusicListContract.IView {

    private RCommonAdapter<Music> mAdapter;
    private RecyclerView musicList;
    private MusicListPresenter mPresenter;

    public static MusicListFragment newInstance() {
        MusicListFragment fragment = new MusicListFragment();
        Bundle mBundle = new Bundle();
        fragment.setArguments(mBundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_local_music, container,false);

        initUI();
        initData();
        return mainView;
    }

    @Override
    protected void initUI() {
        musicList = (RecyclerView) mainView.findViewById(R.id.localMusicList);
        musicList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new RCommonAdapter<Music>(getActivity(), R.layout.list_item) {
            @Override
            public void convert(RCommonViewHolder vh, int position) {
                Music bean = getItem(position);
                vh.setText(R.id.title, bean.getName());
            }
        };

        musicList.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mPresenter = new MusicListPresenter(this);
        mPresenter.getMusicList();
    }

    @Override
    public void onSuccess(MusicBean data) {
        List<Music> mMusicList = new ArrayList<>();
        for (MusicBean.SongList song : data.getSong_list()
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

        mAdapter.addDatas(mMusicList, true);
    }

    @Override
    public void onFail() {

    }

    /**
     * 合唱歌曲时最多只显示两个歌手名
     *
     * @param singers 歌手名
     */
    private String formatSingers(String singers) {
        String[] singerArray = singers.split(",");
        if (singerArray.length > 2) {
            return singerArray[0] + "," + singerArray[1];
        }
        return singers;
    }
}
