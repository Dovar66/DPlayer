package com.dovar.pili;

import android.view.View;

/**
 * Created by heweizong on 2017/9/29.
 */

public interface IMediaController {
    void setMediaPlayer(IMediaController.MediaPlayerControl var1);

    void show();

    void show(int var1);

    void hide();

    boolean isShowing();

    void setEnabled(boolean var1);

    void setAnchorView(View var1);

    public interface MediaPlayerControl {
        void start();

        void pause();

        long getDuration();

        long getCurrentPosition();

        void seekTo(long var1);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();
    }
}
