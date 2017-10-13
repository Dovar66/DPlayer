package com.dovar.pili.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;

import com.dovar.pili.AVOptions;
import com.dovar.pili.IMediaController;
import com.dovar.pili.PLMediaPlayer;
import com.dovar.pili.PlayerState;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by heweizong on 2017/9/29.
 */

public abstract class IVideoView extends FrameLayout implements IMediaController.MediaPlayerControl {
    public static final int ASPECT_RATIO_ORIGIN = 0;
    public static final int ASPECT_RATIO_FIT_PARENT = 1;
    public static final int ASPECT_RATIO_PAVED_PARENT = 2;
    public static final int ASPECT_RATIO_16_9 = 3;
    public static final int ASPECT_RATIO_4_3 = 4;
    private int videoWidth = 0;
    private int videoHeight = 0;
    private long seekPercent = 0L;
    private int curPercent = 0;//percent
    protected Surface mSurface;
    private Uri mUri;
    private AVOptions mAVOptions;
    private static final int status_error = -1;
    private static final int status_idle = 0;
    private static final int status_preparing = 1;
    private static final int status_prepared = 2;
    private static final int status_playing = 3;
    private static final int status_paused = 4;
    private static final int status_completed = 5;
    private int i = status_idle;//播放器状态（0 idle /1 preparing/-1 error/2 prepared/3 playing/4 paused/5 complete）
    private int j = status_idle;// 0 idle/-1 error/3 playing/4 paused/5 complete
    private View bufferingIndicator;
    private View coverView = null;
    private IRenderView mRenderView;
    private PLMediaPlayer mPLMediaPlayer;
    private IMediaController mIMediaController;
    private int displayAspectRatio = ASPECT_RATIO_FIT_PARENT;
    private boolean looping = false;//循环播放
    private boolean screenOnWhilePlaying = true;//播放时保持屏幕常亮
    private int wakeMode = 1;
    private float leftVolume = -1.0F;//左声道音量
    private float rightVolume = -1.0F;//右声道音量
    private boolean debugLoggingEnabled = false;
    protected boolean b = true;
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener;
    private PLMediaPlayer.OnPreparedListener mOnPreparedListener;
    private PLMediaPlayer.OnErrorListener mOnErrorListener;
    private PLMediaPlayer.OnInfoListener mOnInfoListener;
    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private PLMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private PLMediaPlayer.OnMediaDataListener mOnMediaDataListener;
    private PLMediaPlayer.OnPreparedListener E = new PLMediaPlayer.OnPreparedListener() {
        public void onPrepared(PLMediaPlayer var1, int var2) {
            i = status_prepared;
            videoWidth = var1.getVideoWidth();
            videoHeight = var1.getVideoHeight();
            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(var1, var2);
            }

            if (mIMediaController != null) {
                mIMediaController.setEnabled(true);
            }

            if (seekPercent != 0L) {
                seekTo(seekPercent);
            }

            if (j == status_playing) {
                start();
                if (mIMediaController != null) {
                    mIMediaController.show();
                }
            }

        }
    };
    private PLMediaPlayer.OnVideoSizeChangedListener F = new PLMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(PLMediaPlayer var1, int var2, int var3, int var4, int var5) {
            if (mOnVideoSizeChangedListener != null) {
                mOnVideoSizeChangedListener.onVideoSizeChanged(var1, var2, var3, var4, var5);
            }

            videoWidth = var1.getVideoWidth();
            videoHeight = var1.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                mRenderView.resize(videoWidth, videoHeight);
                requestLayout();
            }

        }
    };
    private PLMediaPlayer.OnSeekCompleteListener G = new PLMediaPlayer.OnSeekCompleteListener() {
        public void onSeekComplete(PLMediaPlayer var1) {
            if (mOnSeekCompleteListener != null) {
                mOnSeekCompleteListener.onSeekComplete(var1);
            }

        }
    };
    private PLMediaPlayer.OnInfoListener H = new PLMediaPlayer.OnInfoListener() {
        public boolean onInfo(PLMediaPlayer var1, int what, int extra) {//what 定义了消息类型，extra 是附加参数
            if (mOnInfoListener != null) {
                mOnInfoListener.onInfo(var1, what, extra);
            }

            if (bufferingIndicator != null) {
                if (what == 701 && !var1.optimizeLiveStream()) {
                    bufferingIndicator.setVisibility(VISIBLE);
                } else if (what == 702 || what == 10002 || what == 3) {
                    bufferingIndicator.setVisibility(GONE);
                }
            }

            if (what == 3 && coverView != null) {
                coverView.setVisibility(GONE);
            }

            return true;
        }
    };
    private PLMediaPlayer.OnBufferingUpdateListener I = new PLMediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(PLMediaPlayer var1, int percent) {
            curPercent = percent;
            if (mOnBufferingUpdateListener != null) {
                mOnBufferingUpdateListener.onBufferingUpdate(var1, percent);
            }

        }
    };
    private PLMediaPlayer.OnCompletionListener J = new PLMediaPlayer.OnCompletionListener() {
        public void onCompletion(PLMediaPlayer var1) {
            if (mIMediaController != null) {
                mIMediaController.hide();
            }

            if (bufferingIndicator != null) {
                bufferingIndicator.setVisibility(GONE);
            }

            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion(var1);
            }

            i = status_completed;
            j = status_completed;
        }
    };
    private PLMediaPlayer.OnErrorListener K = new PLMediaPlayer.OnErrorListener() {
        public boolean onError(PLMediaPlayer var1, int var2) {
            i = status_error;
            j = status_error;
            if (mIMediaController != null) {
                mIMediaController.hide();
            }

            if (bufferingIndicator != null) {
                bufferingIndicator.setVisibility(GONE);
            }

            return mOnErrorListener != null ? mOnErrorListener.onError(var1, var2) : true;
        }
    };
    private IRenderView.RenderCallback L = new IRenderView.RenderCallback() {
        public void a(Surface var1, int var2, int var3) {
            if (mSurface == null) {
                mSurface = var1;
            }

            if (mPLMediaPlayer != null) {
                init(mPLMediaPlayer, var1);
            } else {
                init((Map) null);
            }

        }

        public void b(Surface var1, int var2, int var3) {
            boolean var4 = j == status_playing;
            boolean var5 = videoWidth == var2 && videoHeight == var3;
            if (mPLMediaPlayer != null && var4 && var5) {
                if (seekPercent != 0L) {
                    seekTo(seekPercent);
                }

                start();
            }

        }

        public void a(Surface var1) {
            if (mIMediaController != null) {
                mIMediaController.hide();
            }

            init();
        }
    };

    protected abstract IRenderView getRenderView();

    public IVideoView(Context var1) {
        super(var1);
        init(var1);
    }

    public IVideoView(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.init(var1);
    }

    public IVideoView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.init(var1);
    }

    @TargetApi(21)
    public IVideoView(Context var1, AttributeSet var2, int var3, int var4) {
        super(var1, var2, var3, var4);
        this.init(var1);
    }

    public void setDebugLoggingEnabled(boolean var1) {
        this.debugLoggingEnabled = var1;
        if (this.mPLMediaPlayer != null) {
            this.mPLMediaPlayer.setDebugLoggingEnabled(var1);
        }

    }

    protected void init(Context var1) {
        this.mRenderView = this.getRenderView();
        this.mRenderView.setRenderCallback(this.L);
        FrameLayout.LayoutParams var2 = new LayoutParams(-1, -1, 17);
        this.mRenderView.getView().setLayoutParams(var2);
        this.addView(this.mRenderView.getView());
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.requestFocus();
        this.i = status_idle;
        this.j = status_idle;
    }

    public void setDisplayAspectRatio(int var1) {
        this.displayAspectRatio = var1;
        View var2 = this.getChildAt(0);
        if (var2 != null) {
            var2.requestLayout();
        }

    }

    public int getDisplayAspectRatio() {
        return this.displayAspectRatio;
    }

    public void stopPlayback() {
        this.init(true);
    }

    protected void init() {
        if (this.mPLMediaPlayer != null) {
            this.mPLMediaPlayer.setDisplay((SurfaceHolder) null);
        }

    }

    public void setAVOptions(AVOptions var1) {
        this.mAVOptions = var1;
    }

    public void setVideoPath(String var1) {
        if (var1 != null) {
            this.mUri = Uri.parse(var1);
            this.setVideoURI(this.mUri);
        } else {
            this.mUri = null;
        }

    }

    public void setVideoPath(String var1, Map<String, String> headers) {
        if (var1 != null) {
            this.setVideoURI(Uri.parse(var1), headers);
        } else {
            this.mUri = null;
        }

    }

    public void setVideoURI(Uri var1) {
        this.setVideoURI(var1, null);
    }

    public void setVideoURI(Uri var1, Map<String, String> headers) {
        this.mUri = var1;
        if (var1 != null) {
            this.seekPercent = 0L;
            this.b = true;
            init(headers);
            this.requestLayout();
            this.invalidate();
        }

    }

    public void setBufferingIndicator(View var1) {
        if (this.bufferingIndicator != null) {
            this.bufferingIndicator.setVisibility(GONE);
        }

        this.bufferingIndicator = var1;
    }

    public void setCoverView(View var1) {
        this.coverView = var1;
    }

    public void setMediaController(IMediaController var1) {
        if (this.mIMediaController != null) {
            this.mIMediaController.hide();
        }

        this.mIMediaController = var1;
        this.b();
    }

    public void setVolume(float leftVolume, float rightVolume) {
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        if (this.mPLMediaPlayer != null) {
            this.mPLMediaPlayer.setVolume(leftVolume, rightVolume);
        }

    }

    public void setWakeMode(Context var1, int var2) {
        this.wakeMode = var2;
        if (this.mPLMediaPlayer != null) {
            this.mPLMediaPlayer.setWakeMode(var1.getApplicationContext(), var2);
        }

    }

    public void setScreenOnWhilePlaying(boolean var1) {
        this.screenOnWhilePlaying = var1;
        if (this.mPLMediaPlayer != null) {
            this.mPLMediaPlayer.setScreenOnWhilePlaying(var1);
        }

    }

    public void setLooping(boolean var1) {
        this.looping = var1;
        if (this.mPLMediaPlayer != null) {
            this.mPLMediaPlayer.setLooping(var1);
        }

    }

    public boolean isLooping() {
        return this.looping;
    }

    protected void b() {
        if (mPLMediaPlayer != null && mIMediaController != null) {
            mIMediaController.setMediaPlayer(this);
            Object var1 = this.getParent() instanceof View ? (View) this.getParent() : this;
            mIMediaController.setAnchorView((View) var1);
            mIMediaController.setEnabled(isReadyForPlayPause());
        }

    }

    protected void init(Map<String, String> var1) {
        if (this.mUri != null && this.mSurface != null) {
            this.curPercent = 0;
            this.init(false);
            AudioManager var2 = (AudioManager) this.getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            if (var2 != null) {
                var2.requestAudioFocus(null, 3, 1);
            }

            try {
                this.mPLMediaPlayer = new PLMediaPlayer(this.getContext(), this.mAVOptions);
            } catch (UnsatisfiedLinkError var7) {
                var7.printStackTrace();
                return;
            }

            this.mPLMediaPlayer.setDebugLoggingEnabled(this.debugLoggingEnabled);
            this.mPLMediaPlayer.setLooping(this.looping);
            this.mPLMediaPlayer.setScreenOnWhilePlaying(this.screenOnWhilePlaying);
            if (this.wakeMode != -1) {
                this.mPLMediaPlayer.setWakeMode(this.getContext().getApplicationContext(), this.wakeMode);
            }

            if (this.leftVolume != -1.0F && this.rightVolume != -1.0F) {
                this.mPLMediaPlayer.setVolume(this.leftVolume, this.rightVolume);
            }

            this.mPLMediaPlayer.setOnPreparedListener(this.E);
            this.mPLMediaPlayer.setOnVideoSizeChangedListener(this.F);
            this.mPLMediaPlayer.setOnCompletionListener(this.J);
            this.mPLMediaPlayer.setIjkOnErrorListener(this.K);
            this.mPLMediaPlayer.setOnInfoListener(this.H);
            this.mPLMediaPlayer.setOnBufferingUpdateListener(this.I);
            this.mPLMediaPlayer.setOnSeekCompleteListener(this.G);
            this.mPLMediaPlayer.setOnMediaDataListener(this.mOnMediaDataListener);

            try {
                if (var1 != null) {
                    this.mPLMediaPlayer.setDataSource(this.getContext(), this.mUri, var1);
                } else {
                    this.mPLMediaPlayer.setDataSource(this.mUri.toString());
                }

                this.init(this.mPLMediaPlayer, this.mSurface);
                this.mPLMediaPlayer.prepareAsync();
                this.b();
                this.i = status_preparing;
                return;
            } catch (IllegalArgumentException var4) {
                var4.printStackTrace();
            } catch (IllegalStateException var5) {
                var5.printStackTrace();
            } catch (IOException var6) {
                var6.printStackTrace();
            }

            if (this.mOnErrorListener != null) {
                this.mOnErrorListener.onError(this.mPLMediaPlayer, -1);
            }

            this.i = status_error;
            this.j = status_error;
        }
    }

    protected void init(boolean var1) {
        if (this.mPLMediaPlayer != null) {
            if (var1) {
                this.j = status_idle;
                this.mUri = null;
            }

            this.mPLMediaPlayer.stop();
            this.mPLMediaPlayer.release();
            this.mPLMediaPlayer = null;
            this.i = status_idle;
            AudioManager var2 = (AudioManager) this.getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            var2.abandonAudioFocus(null);
        }

    }

    public void start() {
        if (this.i == status_completed) {
            this.setVideoURI(this.mUri);
            this.j = status_playing;
        } else {
            if (isReadyForPlayPause()) {
                this.mPLMediaPlayer.start();
                this.i = status_playing;
            }

            this.j = status_playing;
        }
    }

    public void pause() {
        if (this.isReadyForPlayPause() && this.mPLMediaPlayer.isPlaying()) {
            this.mPLMediaPlayer.pause();
            this.i = status_paused;
        }

        this.j = status_paused;
    }

    public long getDuration() {
        return this.isReadyForPlayPause() ? this.mPLMediaPlayer.getDuration() : -1L;
    }

    public long getCurrentPosition() {
        return this.isReadyForPlayPause() ? this.mPLMediaPlayer.getCurrentPosition() : 0L;
    }

    public void seekTo(long msec) {
        if (this.isReadyForPlayPause()) {
            this.mPLMediaPlayer.seekTo(msec);
            this.seekPercent = 0L;
        } else {
            this.seekPercent = msec;
        }

    }

    public boolean isPlaying() {
        return this.isReadyForPlayPause() && this.mPLMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return this.curPercent;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public PlayerState getPlayerState() {
        return this.mPLMediaPlayer != null ? this.mPLMediaPlayer.getPlayerState() : PlayerState.IDLE;
    }

    public HashMap<String, String> getMetadata() {
        return this.mPLMediaPlayer != null ? this.mPLMediaPlayer.getMetadata() : null;
    }

    public long getVideoBitrate() {
        return this.mPLMediaPlayer != null ? this.mPLMediaPlayer.getVideoBitrate() : 0L;
    }

    public int getVideoFps() {
        return this.mPLMediaPlayer != null ? this.mPLMediaPlayer.getVideoFps() : 0;
    }

    public String getResolutionInline() {
        return this.mPLMediaPlayer != null ? this.mPLMediaPlayer.getResolutionInline() : null;
    }

    //播放器处于正常状态，包括此四种：status_prepared、status_playing、status_paused 、status_completed
    private boolean isReadyForPlayPause() {
        return this.mPLMediaPlayer != null && this.i != status_error && this.i != status_idle && this.i != status_preparing;
    }

    public void setOnInfoListener(PLMediaPlayer.OnInfoListener var1) {
        this.mOnInfoListener = var1;
    }

    public void setOnErrorListener(PLMediaPlayer.OnErrorListener var1) {
        this.mOnErrorListener = var1;
    }

    public void setOnPreparedListener(PLMediaPlayer.OnPreparedListener var1) {
        this.mOnPreparedListener = var1;
    }

    public void setOnCompletionListener(PLMediaPlayer.OnCompletionListener var1) {
        this.mOnCompletionListener = var1;
    }

    public void setOnBufferingUpdateListener(PLMediaPlayer.OnBufferingUpdateListener var1) {
        this.mOnBufferingUpdateListener = var1;
    }

    public void setOnSeekCompleteListener(PLMediaPlayer.OnSeekCompleteListener var1) {
        this.mOnSeekCompleteListener = var1;
    }

    public void setOnVideoSizeChangedListener(PLMediaPlayer.OnVideoSizeChangedListener var1) {
        this.mOnVideoSizeChangedListener = var1;
    }

    public void setOnMediaDataListener(PLMediaPlayer.OnMediaDataListener mVar1) {
        this.mOnMediaDataListener = mVar1;
        if (this.mPLMediaPlayer != null) {
            this.mPLMediaPlayer.setOnMediaDataListener(this.mOnMediaDataListener);
        }

    }

    public boolean onTouchEvent(MotionEvent var1) {
        if (this.isReadyForPlayPause() && this.mIMediaController != null) {
            this.showHideController();
        }

        return false;
    }

    public boolean onTrackballEvent(MotionEvent var1) {
        if (this.isReadyForPlayPause() && this.mIMediaController != null) {
            this.showHideController();
        }

        return false;
    }

    public boolean onKeyDown(int var1, KeyEvent var2) {
        boolean var3 = var1 != 4 && var1 != 24 && var1 != 25 && var1 != 164 && var1 != 82 && var1 != 5 && var1 != 6;
        if (this.isReadyForPlayPause() && var3 && this.mIMediaController != null) {
            if (var1 == 79 || var1 == 85) {
                if (this.mPLMediaPlayer.isPlaying()) {
                    this.pause();
                    mIMediaController.show();
                } else {
                    this.start();
                    mIMediaController.hide();
                }

                return true;
            }

            if (var1 == 126) {
                if (!this.mPLMediaPlayer.isPlaying()) {
                    this.start();
                    this.mIMediaController.hide();
                }

                return true;
            }

            if (var1 == 86 || var1 == 127) {
                if (this.mPLMediaPlayer.isPlaying()) {
                    this.pause();
                    mIMediaController.show();
                }

                return true;
            }

            this.showHideController();
        }

        return super.onKeyDown(var1, var2);
    }

    protected void showHideController() {
        if (mIMediaController.isShowing()) {
            mIMediaController.hide();
        } else {
            mIMediaController.show();
        }

    }

    protected void init(PLMediaPlayer mp, Surface var2) {
        if (mp != null && var2 != null) {
            mp.setSurface(var2);
        }
    }

    protected interface IRenderView {
        View getView();

        void resize(int var1, int var2);

        void setRenderCallback(RenderCallback mVar1);

        public interface RenderCallback {
            void a(Surface var1, int var2, int var3);

            void b(Surface var1, int var2, int var3);

            void a(Surface var1);
        }
    }
}
