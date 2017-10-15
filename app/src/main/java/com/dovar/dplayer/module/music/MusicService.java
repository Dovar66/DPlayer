package com.dovar.dplayer.module.music;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.dovar.dplayer.bean.Music;
import com.dovar.dplayer.common.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicService extends Service {

    private static final String KEY_MUSICLIST="key_music_list";
    private final String TAG = "DPlayer";
    private MyMediaPlayer mPlayer;//播放器实例
    private MyMusicPlayReceiver mReceiver;//广播
    private ExecutorService mExecutorService;// 可缓存线程池
    private int seektoTime;// 当前播放进度

    private ArrayList<Music> musics;//播放歌单
    public static int curPosition;//当前播放歌曲下标
    public static int playMode = 1;//歌曲播放模式<顺序播放、随机播放、单曲循环>,默认顺序播放
    private String current_musicId;//播放器当前播放歌曲的id

    public static void startMusicService(Activity mActivity) {
        Intent mIntent = new Intent(mActivity, MusicService.class);
        mActivity.startService(mIntent);
    }

    public static void startPlay(Context mContext,ArrayList<Music> mMusicList){
        Intent mIntent=new Intent(Constants.ACTION_NEWPLAYLIST);
        mIntent.putExtra(KEY_MUSICLIST,mMusicList);
        mIntent.putExtra("position",0);
        mContext.sendBroadcast(mIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // 初始化播放器
        mPlayer = MyMediaPlayer.getMyMediaPlayer();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                //更新记录当前播放歌曲id
                current_musicId = musics.get(curPosition).getSong_id();
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(MusicService.this, "播放器发生错误" + what, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: " + what);
                return true;
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextIndex();
                loadMusic();
                mOnSongCutListener.onSongCut();
            }
        });
        // 注册广播接收器
        registPlayerReceiver();

        // 创建工作线程用于实时更新播放进度
        mExecutorService = Executors.newCachedThreadPool();
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        if (mPlayer.isPlaying()) {
                            int currentPosition = mPlayer.getCurrentPosition();// 当前播放进度
                            int duration = mPlayer.getDuration();// 歌曲总时长
                            Intent intent = new Intent();
                            intent.setAction(Constants.ACTION_UPDATEPROGRESS);
                            intent.putExtra("curPosition", currentPosition);
                            intent.putExtra("duration", duration);
                            sendBroadcast(intent);
                        }
                        Thread.sleep(300);// 每0.3秒发送一次广播，更新播放器界面
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        super.onCreate();
    }

    // 注册广播
    private void registPlayerReceiver() {
        mReceiver = new MyMusicPlayReceiver();
        IntentFilter filter = new IntentFilter();

        filter.addAction(Constants.ACTION_NEWPLAY);//切歌
        filter.addAction(Constants.ACTION_NEXT);//非播放页面发来的下一曲操作
        filter.addAction(Constants.ACTION_PLAY);//播放
        filter.addAction(Constants.ACTION_PAUSE);//暂停
        filter.addAction(Constants.ACTION_CHANGE);//改变播放进度
        filter.addAction(Constants.ACTION_TRANSFERDATA);
        filter.addAction(Constants.ACTION_ADD);//增加到歌单
        filter.addAction(Constants.ACTION_NEWPLAYLIST);//创建新歌单
        filter.addAction(Constants.ACTION_DELETE);//删除歌单中一首歌曲
        registerReceiver(mReceiver, filter);
    }

    public static OnResumeDataListener mOnResumeDataListener;
    public static OnSongCutListener mOnSongCutListener;

    public interface OnSongCutListener {
        void onSongCut();
    }


    public interface OnResumeDataListener {
        void onResumeData(int curDuration, int duration, ArrayList<Music> musics, boolean isPlaying);
    }

    private class MyMusicPlayReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.ACTION_NEWPLAYLIST:
                    //创建新歌单，开始播放
                    if (musics == null) {
                        musics = new ArrayList<>();
                    }
                    musics.clear();
                    musics.addAll((ArrayList<Music>) intent.getSerializableExtra(KEY_MUSICLIST));
                    curPosition = intent.getIntExtra("position", -1);
                    if (curPosition != -1 && musics.size() > 0 && musics.size() > curPosition) {
                        if (!(current_musicId != null && current_musicId.equals(musics.get(curPosition).getSong_id()))) {
                            //当前播放歌曲与需要播放歌曲不相同才切歌
                            loadMusic();
                        }
                    }
                case Constants.ACTION_PLAY:
                    //继续播放
                    mPlayer.start();
                    break;
                case Constants.ACTION_PAUSE:
                    //暂停播放
                    mPlayer.pause();
                    break;
                case Constants.ACTION_NEWPLAY:
                    //切歌
                    if (musics == null) return;
                    loadMusic();
                    break;
                case Constants.ACTION_CHANGE:
                    int progress = intent.getIntExtra("progress", 0);
                    seekTo(progress);
                    mPlayer.start();
                    break;
                case Constants.ACTION_TRANSFERDATA:
                    if (mOnResumeDataListener != null) {
                        Log.d(TAG, "onReceive: !=null");
                        mOnResumeDataListener.onResumeData(mPlayer.getCurrentPosition(), mPlayer.getDuration(), musics, mPlayer.isPlaying());
                    }
                    break;
                case Constants.ACTION_NEXT://下一曲播放
                    if (musics != null && musics.size() > 0) {
                        nextIndex();
                        loadMusic();
                    } else {
                        Toast.makeText(context, "当前播放列表为空", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.ACTION_ADD:
                    ArrayList<Music> insert = (ArrayList<Music>) intent.getSerializableExtra("insert");
                    if (insert == null) {
                        Log.d(TAG, "onReceive: insert is null!");
                        return;
                    }
                    if (musics != null) {
                        int pos = curPosition + 1;
                        for (Music item : insert
                                ) {
                            musics.add(pos, item);
                            pos += 1;
                        }
                    } else {
                        musics = new ArrayList<>();
                        musics.addAll(insert);
                        curPosition = 0;
                        loadMusic();
                    }
                    break;
                case Constants.ACTION_DELETE:
                    int pos = intent.getIntExtra("position", -1);
                    if (pos != -1) {
                        musics.remove(pos);
                    }
                    if (musics.size() == 0) {
                        mPlayer.reset();
                        return;
                    }
                    String needCut = intent.getStringExtra("needCut");
                    if (needCut != null && needCut.equals("yes")) {
                        //需要切歌
                        loadMusic();
                    }
                    break;
            }
        }

    }

    /**
     * 加载音乐
     */
    private void loadMusic() {
        mPlayer.reset();
        if (musics.size() > curPosition) {
            String url = musics.get(curPosition).getUrl();
            try {
                mPlayer.setDataSource(url);
                mPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自动播放下一首歌曲的下标
     */
    private void nextIndex() {
        switch (playMode) {
            case Constants.MODE_INORDER:
                if (curPosition == musics.size() - 1) {
                    curPosition = 0;
                } else {
                    curPosition += 1;
                }
                break;
            case Constants.MODE_RANDOM:
                curPosition = (int) (Math.random() * musics.size());
                break;
            case Constants.MODE_SINGLE:
                break;
        }
    }

    // 更新播放器的当前播放时间
    public void seekTo(int progress) {
        seektoTime = mPlayer.getDuration() * progress / 100;
        mPlayer.seekTo(seektoTime);
    }

    @Override
    // 释放所有占用资源
    public void onDestroy() {
        mPlayer.release();
        mPlayer = null;
        mExecutorService = null;
        unregisterReceiver(mReceiver);
        mReceiver = null;
        super.onDestroy();
    }
}
