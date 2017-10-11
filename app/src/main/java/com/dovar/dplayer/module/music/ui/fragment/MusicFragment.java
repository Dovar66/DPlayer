package com.dovar.dplayer.module.music.ui.fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.Music;
import com.dovar.dplayer.bean.MusicByIdBean;
import com.dovar.dplayer.common.base.BaseFragment;
import com.dovar.dplayer.common.customview.lyric.LyricView;
import com.dovar.dplayer.module.music.contract.MusicContract;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by heweizong on 2017/10/11.
 */

public class MusicFragment extends BaseFragment implements MusicContract.IView{

    @BindView(R.id.title_text)
    TextView title_text;//顶部显示的歌曲名
    @BindView(R.id.seekbar)
    SeekBar mSeekBar;//进度条
    @BindView(R.id.time_progress)
    TextView time_progress;//当前播放时间
    @BindView(R.id.music_duration)
    TextView music_duration;//歌曲时长
    @BindView(R.id.music_name)
    TextView music_name;//歌曲名
    @BindView(R.id.play_pause)
    ImageView play_pause;//播放暂停按钮
    @BindView(R.id.music_list)
    ImageView music_list;//点击打开歌曲列表
    @BindView(R.id.play_mode)
    ImageView iv_playMode;//播放模式
    @BindView(R.id.tv_collect)
    TextView tv_collect;//收藏
    @BindView(R.id.lyric)
    LyricView mLyricView;//歌词

    private final int INTERVAL = 45;// 歌词每行的间隔
    private String lrc;//歌词保存路径


    private ArrayList<Music> musics;//当前播放歌单


    static HashMap<String, Boolean> collectMap;//记录当前歌单中被修改过收藏状态的歌曲的收藏状态，更新歌单时清空

    public static MusicFragment newInstance(){
        MusicFragment fragment=new MusicFragment();
        Bundle mBundle=new Bundle();
        fragment.setArguments(mBundle);
        return fragment;
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
