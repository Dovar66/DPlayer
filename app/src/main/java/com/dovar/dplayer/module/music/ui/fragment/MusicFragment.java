package com.dovar.dplayer.module.music.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.Music;
import com.dovar.dplayer.bean.MusicByIdBean;
import com.dovar.dplayer.common.base.BaseFragment;
import com.dovar.dplayer.common.customview.lyric.LyricView;
import com.dovar.dplayer.common.utils.Constants;
import com.dovar.dplayer.common.utils.PhotoUtil;
import com.dovar.dplayer.common.utils.ToastUtil;
import com.dovar.dplayer.module.music.MusicService;
import com.dovar.dplayer.module.music.contract.MusicContract;
import com.dovar.dplayer.module.music.presenter.MusicPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by heweizong on 2017/10/11.
 */

public class MusicFragment extends BaseFragment implements MusicContract.IView {

    public static final String KEY_MUSIC = "key_music";
    public static final String KEY_MUSICLIST = "key_music_list";
    private Callback mCallback;
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

    private ArrayList<Music> musicList;//当前播放歌单
    private int curIndex;//当前歌曲在列表中的位置
    private int duration=1;//当前播放歌曲总时长

    private MusicPresenter mPresenter;

    public static MusicFragment newInstance(int index, ArrayList<Music> mMusicList) {
        MusicFragment fragment = new MusicFragment();
        Bundle mBundle = new Bundle();
        mBundle.putInt(KEY_MUSIC, index);
        mBundle.putSerializable(KEY_MUSICLIST, mMusicList);
        fragment.setArguments(mBundle);
        return fragment;
    }

    public void updateMusics(int index, ArrayList<Music> mMusicList) {
        if (mMusicList != null && index >= 0 && mMusicList.size() > index) {
            curIndex = index;
            musicList = mMusicList;
            playMusic(curIndex);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
        if (mCallback != null) {
            mCallback.onPlayerShowOrHide(true);
        }
    }

    @Override
    public void onDestroy() {
        if (mCallback != null) {
            mCallback.onPlayerShowOrHide(false);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = createContentView(R.layout.fragment_music_play);
        setStatusBarTintEnabled(true);

        ButterKnife.bind(this, mainView);
        MusicService.startMusicService(getActivity());
        initUI();
        initData();

        playMusic(curIndex);
        return mainView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCallback != null) {
            mCallback = null;
        }
    }

    private void parseIntent() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            try {
                curIndex = mBundle.getInt(KEY_MUSIC);
                musicList = (ArrayList<Music>) mBundle.getSerializable(KEY_MUSICLIST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSuccess(MusicByIdBean music) {
        if (music == null) return;
        try {
            String url = music.getData().getSongList().get(0).getSongLink();
            if (!TextUtils.isEmpty(url)) {
                musicList.get(curIndex).setUrl(url);
                MusicService.startPlay(getActivity(), url);
            }

        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    @Override
    public void onFail() {

    }

    @Override
    protected void initUI() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //判断是否是用户触摸改变，代码中改变progress时为false
                if (fromUser) {
                    if (judgeError()) {
                        return;
                    }
//                    Intent mIntent = new Intent();
//                    mIntent.setAction(Constant.ACTION_CHANGE);
//                    mIntent.putExtra("progress", progress);
//                    sendBroadcast(mIntent);
                    //调整歌词显示
                    mLyricView.setOffsetY(220 - mLyricView.SelectIndex(progress / 100 * duration) * (mLyricView.getSIZEWORD() + INTERVAL));
                    mLyricView.invalidate();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void initData() {
        mPresenter = new MusicPresenter(this);
    }

    //上一首
    @OnClick(R.id.iv_previous)
    void onClickPrevious(View v) {
        playNextOrPrevious(false);
    }

    //下一首
    @OnClick(R.id.iv_next)
    void onClickNext(View v) {
        playNextOrPrevious(true);
    }

    //播放&暂停
    @OnClick(R.id.iv_play_pause)
    void playAndPause(View v) {
        getActivity().sendBroadcast(new Intent(Constants.ACTION_PLAY));
    }

    //播放模式
    @OnClick(R.id.iv_play_mode)
    void onClickPlayMode(View v) {
        switch (playMode) {
            case Constants.MODE_INORDER:
                playMode = Constants.MODE_RANDOM;
                iv_playMode.setImageResource(R.drawable.ic_order_random);
                break;
            case Constants.MODE_RANDOM:
                playMode = Constants.MODE_SINGLE;
                iv_playMode.setImageResource(R.drawable.ic_order_single);
                break;
            case Constants.MODE_SINGLE:
                playMode = Constants.MODE_INORDER;
                iv_playMode.setImageResource(R.drawable.ic_play_order);
                break;
        }
    }

    @OnClick(R.id.ll_back)
    void onClickBack(View v) {
        getActivity().onBackPressed();
    }

    private int playMode = Constants.MODE_INORDER;//歌曲播放模式<顺序播放、随机播放、单曲循环>,默认顺序播放

    /**
     * 播放上一首或下一首
     *
     * @param isNext 为true时播放下一首 false时播放上一首
     */
    private void playNextOrPrevious(boolean isNext) {
        if (musicList == null || musicList.size() == 0) {
            ToastUtil.show("当前播放列表为空");
            return;
        }
        switch (playMode) {
            case Constants.MODE_INORDER:
                if (isNext) {
                    if (curIndex == musicList.size() - 1) {
                        curIndex = 0;
                    } else {
                        curIndex++;
                    }
                } else {
                    if (curIndex == 0) {
                        curIndex = musicList.size() - 1;
                    } else {
                        curIndex--;
                    }
                }
                break;
            case Constants.MODE_RANDOM:
                int lastIndex = curIndex;
                do {
                    curIndex = (int) (Math.random() * musicList.size());
                } while (lastIndex == curIndex);
                break;
            case Constants.MODE_SINGLE:
                break;
        }

        playMusic(curIndex);
    }

    private void playMusic(int index) {
        if (index >= 0 && musicList != null && index < musicList.size()) {
            Music playingMusic = musicList.get(index);
            if (playingMusic != null) {
                if (TextUtils.isEmpty(playingMusic.getUrl())) {
                    mPresenter.getMusic(playingMusic.getSong_id());
                } else {
                    MusicService.startPlay(getActivity(), playingMusic.getUrl());
                }

                setText(tv_title, playingMusic.getName());
                //背景高斯模糊
                Glide.with(this).load(PhotoUtil.bigPhoto(playingMusic.getPic_big())).bitmapTransform(new BlurTransformation(getActivity())).into(getBackgroundImageView());
            }

            searchLrc();
        }
    }

    /**
     * 加载歌词
     */
    private void searchLrc() {
        if (mLyricView != null) {
            if (musicList != null && musicList.size() > 0 && musicList.size() > curIndex) {
                String lrc = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LyricSync/" + musicList.get(curIndex).getName().trim() + ".lrc";
                if (!new File(lrc).exists()) {
                    writeMedia(lrc);
                }
                LyricView.read(lrc);
                mLyricView.SetTextSize();
                mLyricView.setOffsetY(320);
            }
        }
    }

    // 缓存歌词到本地
    private void writeMedia(final String lrcPath) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                FileOutputStream out = null;
                InputStream is = null;

                String savePath = lrcPath;
                try {
                    URL url = new URL(musicList.get(curIndex).getLrclink());
                    HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                    if (savePath == null) {
                        savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LyricSync/" + musicList.get(curIndex).getName() + ".lrc";
                    }

                    File cacheFile = new File(savePath);

                    if (!cacheFile.exists()) {
                        cacheFile.getParentFile().mkdirs();
                        cacheFile.createNewFile();
                    }

                    long readSize = cacheFile.length();
                    out = new FileOutputStream(cacheFile, true);

                    httpConnection.setRequestProperty("User-Agent", "NetFox");
                    httpConnection.setRequestProperty("RANGE", "bytes=" + readSize + "-");

                    is = httpConnection.getInputStream();

                    long mediaLength = httpConnection.getContentLength();
                    if (mediaLength == -1) {
                        return;
                    }

                    mediaLength += readSize;

                    byte buf[] = new byte[4 * 1024];
                    int size = 0;

                    while ((size = is.read(buf)) != -1) {
                        try {
                            out.write(buf, 0, size);
                            readSize += size;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error", e.toString());
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            //
                        }
                    }

                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            //
                        }
                    }
                }

                LyricView.read(savePath);
                mLyricView.SetTextSize();
                mLyricView.setOffsetY(320);
            }
        }).start();

    }

    private boolean judgeError() {
        return musicList == null || musicList.size() == 0;
    }


    public interface Callback {

        void onPlayerShowOrHide(boolean isShow);
    }

    //调整歌词显示
//            mLyricView.setOffsetY(220-mLyricView.SelectIndex(progress/100*duration)*(mLyricView.getSIZEWORD()+INTERVAL));
//            mLyricView.invalidate();
}
