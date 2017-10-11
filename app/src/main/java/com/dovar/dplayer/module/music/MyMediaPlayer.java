package com.dovar.dplayer.module.music;

import android.media.MediaPlayer;

/**
 * 单例播放器
 */
public class MyMediaPlayer extends MediaPlayer {
    private static MyMediaPlayer mMediaPlayer = null;
    public static boolean isUsing=false;

    private MyMediaPlayer() {

    }

    public static MyMediaPlayer getMyMediaPlayer() {
        if (mMediaPlayer == null) {
            synchronized (MyMediaPlayer.class) {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MyMediaPlayer();
                }
            }
        }
        return mMediaPlayer;
    }


    public interface OnPlayingChangedListener {
        void OnPlayingChanged(boolean flag);
    }

    OnPlayingChangedListener mOnPlayingChangedListener;

    public void setOnPlayingChangedListener(OnPlayingChangedListener listener) {
        mOnPlayingChangedListener = listener;
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        isUsing=true;
        if (mOnPlayingChangedListener != null) {
            mOnPlayingChangedListener.OnPlayingChanged(true);
        }
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        if (mOnPlayingChangedListener != null) {
            mOnPlayingChangedListener.OnPlayingChanged(false);
        }
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        isUsing=false;
        if (mOnPlayingChangedListener != null) {
            mOnPlayingChangedListener.OnPlayingChanged(false);
        }
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        if (mOnPlayingChangedListener != null) {
            mOnPlayingChangedListener.OnPlayingChanged(true);
        }
        super.prepareAsync();
    }

    @Override
    public void release() {
        super.release();
        isUsing=false;
    }

}
