package com.dovar.dplayer.module.music.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.Music;
import com.dovar.dplayer.bean.MusicByIdBean;
import com.dovar.dplayer.common.base.BaseFragment;
import com.dovar.dplayer.common.customview.lyric.LyricView;
import com.dovar.dplayer.module.music.MusicService;
import com.dovar.dplayer.module.music.contract.MusicContract;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heweizong on 2017/10/11.
 */

public class MusicFragment extends BaseFragment implements MusicContract.IView {

    public static final String KEY_MUSIC = "key_music";
    @BindView(R.id.tv_title)
    TextView tv_title;//顶部显示的歌曲名
    @BindView(R.id.seekbar)
    SeekBar mSeekBar;//进度条
    @BindView(R.id.music_progress)
    TextView tv_music_progress;//当前播放时间
    @BindView(R.id.music_duration)
    TextView tv_music_duration;//歌曲时长
    @BindView(R.id.iv_play_pause)
    ImageView iv_play_pause;//播放暂停按钮
    @BindView(R.id.iv_music_list)
    ImageView iv_music_list;//点击打开歌曲列表
    @BindView(R.id.iv_play_mode)
    ImageView iv_playMode;//播放模式
    @BindView(R.id.tv_collect)
    TextView tv_collect;//收藏
    @BindView(R.id.lyric)
    LyricView mLyricView;//歌词

    private final int INTERVAL = 45;// 歌词每行的间隔
    private String lrc;//歌词保存路径


    private ArrayList<Music> musics;//当前播放歌单


    static HashMap<String, Boolean> collectMap;//记录当前歌单中被修改过收藏状态的歌曲的收藏状态，更新歌单时清空

    private Music playingMusic;//当前歌曲

    public static MusicFragment newInstance(Music mMusic) {
        MusicFragment fragment = new MusicFragment();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(KEY_MUSIC, mMusic);
        fragment.setArguments(mBundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_music_play, container, false);

        ButterKnife.bind(mainView);
        MusicService.startMusicService(getActivity());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (playingMusic != null) {
                    ArrayList<Music> mMusics = new ArrayList<>();
                    mMusics.add(playingMusic);
                    MusicService.startPlay(getActivity(), mMusics);
                }
            }
        }, 200);

        return mainView;
    }

    private void parseIntent() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            try {
                playingMusic = (Music) mBundle.getSerializable(KEY_MUSIC);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSuccess(MusicByIdBean music) {

    }

    @Override
    public void onFail() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {

    }


}
