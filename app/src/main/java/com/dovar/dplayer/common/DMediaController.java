package com.dovar.dplayer.common;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dovar.dplayer.R;
import com.dovar.dplayer.common.utils.LogUtil;
import com.dovar.dplayer.common.utils.NetWorkUtil;
import com.dovar.dplayer.common.utils.ToastUtil;
import com.dovar.pili.AVOptions;
import com.dovar.pili.IMediaController;
import com.dovar.pili.PLMediaPlayer;
import com.dovar.pili.widget.PLVideoTextureView;
import com.dovar.pili.widget.PLVideoView;

import java.lang.ref.WeakReference;
import java.util.Locale;


/**
 * You can write mSurface custom mediaController instead of this class
 * A mediaController widget must implement all the interface defined by com.pili.pldroid.player.IMediaController
 * <p>
 * 使用PLVideoTextureView进行视频渲染
 * <p>
 *
 * @see PLVideoTextureView
 */
public class DMediaController implements IMediaController {
    private static final String TAG = "PLMediaController";

    private Context mContext;
    private MediaPlayerControl mPlayer;
    private PLMediaPlayer plMediaPlayer;
    private PLVideoTextureView mVideoView;//不允许对外部提供，播放器逻辑应该全部由DMediaController对象管理
    private ViewGroup mAnchor;//videoView的锚点
    private SeekBar mProgress;//进度条
    private TextView mEndTime, mCurrentTime;//视频时长、当前播放时间
    private CheckBox mPauseCheckbox;//播放暂停按钮

    private View popupView;//mVideoView的controllerView，包含播放按钮、暂停按钮等UI
    private long mDuration;
    private boolean mShowing;
    private boolean mDragging;
    private boolean mInstantSeeking = true;

    private static int sDefaultTimeout = 3000;
    private static final int SEEK_TO_POST_DELAY_MILLIS = 200;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;

    private AudioManager mAM;
    private TelephonyManager mTM;
    private Runnable mLastSeekBarRunnable;
    private boolean mDisableProgress = false;

    private boolean isLive = true;//当前播放是否为在线直播
    private boolean isFullScreen;//当前是否横屏
    private boolean alwaysShow = false;//值为true时，controllerView总是可见
    private boolean isVideoSizeAlreadyGot = false;//是否已经获取到当前播放视频的videoSize
    private boolean isFirstAfterResize = true;//获取到视频尺寸后的第一次调用show()方法
    private int defaultHeight = 500 * MyApplication.getInstance().getHeight() / 1334;//获取不到视频流尺寸时采用的默认高度


    private ControlListener mListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangeListener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnPreparedListener mOnPreparedListener;

    private StateController mStateController;

    public enum STATE {
        idle, isPlaying, isPaused, isEnded
    }

    private String url_latest = "";
    private STATE playState = STATE.idle;//播放器当前状态

    private boolean isMute;//当前是否静音
    private PhoneStateListener mPhoneStateListener;

    private Handler mHandler = new ControllerHandler(this);


    public void setControlListener(ControlListener mListener) {
        this.mListener = mListener;
    }

    public interface ControlListener {
        void onPauseOrPlay(boolean isPause);

        void initPortControllerView(View v);

        void initLandControllerView(View v);

        /**
         * Create the view that holds the widgets that control playback.
         *
         * @return The PortController view.
         */
        View makeControllerView();

        /**
         * Create the view that holds the widgets that control playback.
         *
         * @return The LandController view.
         */
        View makeLandControllerView();

    }

    public void setStateController(StateController stateController) {
        this.mStateController = stateController;
    }

    public interface StateController {
        void showNoNetWorkView();

        void showErrorView();

        void showLoadingView();

        void showEndView();

        void showPlayView();
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(PLMediaPlayer mp, int width, int height);
    }

    public interface OnCompletionListener {
        void onCompletion(PLMediaPlayer plMediaPlayer);
    }

    public interface OnErrorListener {
        boolean onError(PLMediaPlayer mp, int errorCode);
    }

    public interface OnInfoListener {
        boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra);
    }

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int i);
    }

    public interface OnPreparedListener {
        void onPrepared(PLMediaPlayer plMediaPlayer, int preparedTime);
    }

    public DMediaController(Context context) {
        this(context, true);//默认为直播模式
    }

    public DMediaController(Context context, boolean isLive) {
        this.isLive = isLive;
        initController(context);
    }

    private void initController(Context context) {
        mContext = context;
        mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mTM = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        if (mTM != null) {
            mPhoneStateListener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE://挂断
                            onCallStateChange();
                            break;
                        case TelephonyManager.CALL_STATE_RINGING://来电响铃
                            onCallStateChange();
                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK://通话
                            onCallStateChange();
                            break;
                    }
                }
            };
            mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        initPlayer();
    }

    private void initPlayer() {
        mVideoView = new PLVideoTextureView(mContext);
        mVideoView.setMediaController(this);

        final AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        // Some optimization with buffering mechanism when be set to 1
        if (isLive) {
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        } else {
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
        }
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0);
        // whether start play automatically after prepared, default value is 1
        // 是否自动启动播放，如果设置为 1，则在调用 `prepareAsync` 或者 `setVideoPath` 之后自动启动播放，无需调用 `start()`
        // 默认值是：1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 1);
        // 播放前最大探测流的字节数，单位是 byte
        // 默认值是：128 * 1024
        options.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);
        mVideoView.setAVOptions(options);

        //设定画面比例
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);

        //每次加载视频流成功都会回调数次，目前测试是6次
        //回调第2次与第3次之间播放器还会自动调用{@link show()}方法，而此时videoView还在进行布局重绘，所以需要增加处理使此次show()不显示
        //{@link isFirstAfterResize}
        mVideoView.setOnVideoSizeChangedListener(new PLMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height, int videoSar, int videoDen) {
                if (mp == null) return;
                plMediaPlayer = mp;
                LogUtil.d(TAG, "onVideoSizeChanged: " + plMediaPlayer.getVideoBitrate() + "\n" + plMediaPlayer.getVideoFps());
                //高码率的直播流在启用直播模式时会出现卡顿，所以切换到点播模式
                if (!isLive || (isLive && plMediaPlayer.getVideoBitrate() > 2 * 1024)) {
                    options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
                    mVideoView.setAVOptions(options);
                } else {
                    options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
                    mVideoView.setAVOptions(options);
                }

                if (width == 0) return;
                isVideoSizeAlreadyGot = true;
                resizeVideoView();

                if (mOnVideoSizeChangeListener != null) {
                    mOnVideoSizeChangeListener.onVideoSizeChanged(mp, width, height);
                }
            }
        });

        mVideoView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer mPLMediaPlayer) {
                playState = STATE.isEnded;
                if (mOnCompletionListener != null) {
                    mOnCompletionListener.onCompletion(mPLMediaPlayer);
                }
            }
        });

        mVideoView.setOnErrorListener(new PLMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer mPLMediaPlayer, int mI) {
                if (mOnErrorListener != null) return mOnErrorListener.onError(mPLMediaPlayer, mI);
                return false;
            }
        });

        mVideoView.setOnInfoListener(new PLMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(PLMediaPlayer mPLMediaPlayer, int mI, int mI1) {
                if (mOnInfoListener != null) return mOnInfoListener.onInfo(mPLMediaPlayer, mI, mI1);
                return false;
            }
        });

        mVideoView.setOnBufferingUpdateListener(new PLMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(PLMediaPlayer mPLMediaPlayer, int mI) {
                if (mOnBufferingUpdateListener != null) {
                    mOnBufferingUpdateListener.onBufferingUpdate(mPLMediaPlayer, mI);
                }
            }
        });

        mVideoView.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer mPLMediaPlayer, int mI) {
                playState = STATE.isPlaying;
                if (mOnPreparedListener != null) {
                    mOnPreparedListener.onPrepared(mPLMediaPlayer, mI);
                }
            }
        });
    }

    protected void initControllerView(View v) {
        if (mListener != null) {
            if (isFullScreen) {
                mListener.initLandControllerView(v);
            } else {
                mListener.initPortControllerView(v);
            }
        }

        if (v == null) return;
        if (v.getParent() == null) {
            if (mAnchor == null) {
                throw new RuntimeException((getClass().getSimpleName() + "this.setAnchor(View) should be invoke before!"));
            }
            popupDismiss();
            mAnchor.addView(v);
        }
        //初始化播放器必不可少的播放相关的UI控件
        mPauseCheckbox = (CheckBox) v.findViewById(R.id.pause);
        View ll_mPause = v.findViewById(R.id.pause_ll);
        if (ll_mPause != null) {
            ll_mPause.setOnClickListener(mPauseListener);
        }
        if (!isLive) {
            View ll_seek = v.findViewById(R.id.ll_seek);
            if (ll_seek != null) {
                //扩大seekbar的事件响应区域
                ll_seek.setOnTouchListener(touchListener);
            }
            mProgress = (SeekBar) v.findViewById(R.id.progress);
            if (mProgress != null) {
                mProgress.setOnSeekBarChangeListener(mSeekListener);
                mProgress.setThumbOffset(1);
                mProgress.setMax(1000);
                mProgress.setEnabled(!mDisableProgress);
            }

            mEndTime = (TextView) v.findViewById(R.id.time);
            mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        }
        View iv_mute = v.findViewById(R.id.iv_mute);
        if (iv_mute != null) {
            if (isMute) {
                iv_mute.setBackgroundResource(R.drawable.video_btn_volume_off);
            } else {
                iv_mute.setBackgroundResource(R.drawable.video_btn_volume_on);
            }
            iv_mute.setOnClickListener(muteListener);
        }
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mProgress == null) return false;

            Rect seekRect = new Rect();
            mProgress.getHitRect(seekRect);
            if ((event.getY() >= (seekRect.top - 50)) && (event.getY() <= (seekRect.bottom + 50))) {

                float y = seekRect.top + seekRect.height() / 2;
                //seekBar only accept relative x
                float x = event.getX() - seekRect.left;
                if (x < 0) {
                    x = 0;
                } else if (x > seekRect.width()) {
                    x = seekRect.width();
                }
                MotionEvent me = MotionEvent.obtain(event.getDownTime(), event.getEventTime(),
                        event.getAction(), x, y, event.getMetaState());
                return mProgress.onTouchEvent(me);
            }
            return false;
        }
    };

    private View.OnClickListener muteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isMute) {
                v.setBackgroundResource(R.drawable.video_btn_volume_on);
                mVideoView.setVolume(1.0f, 1.0f);
                isMute = false;
            } else {
                mVideoView.setVolume(0.0f, 0.0f);
                v.setBackgroundResource(R.drawable.video_btn_volume_off);
                isMute = true;
            }
        }
    };

    /**
     * Control the action when the seekbar dragged by user
     *
     * @param seekWhenDragging True the media will seek periodically
     */
    public void setInstantSeeking(boolean seekWhenDragging) {
        mInstantSeeking = seekWhenDragging;
    }

    public interface OnShownListener {
        void onShown();
    }

    public interface OnHiddenListener {
        void onHidden();
    }

    private OnHiddenListener mHiddenListener;
    private OnShownListener mOnShownListener;

    public void setOnHiddenListener(OnHiddenListener l) {
        mHiddenListener = l;
    }

    public void setOnShownListener(OnShownListener mOnShownListener) {
        this.mOnShownListener = mOnShownListener;
    }

    private static class ControllerHandler extends Handler {
        private WeakReference<DMediaController> mWeakReference;

        ControllerHandler(DMediaController controller) {
            this.mWeakReference = new WeakReference<>(controller);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakReference == null) return;
            DMediaController controller = mWeakReference.get();
            if (controller == null) return;
            switch (msg.what) {
                case FADE_OUT:
                    controller.hide();
                    break;
                case SHOW_PROGRESS:
                    long pos = controller.setProgress();
                    if (!controller.isDragging() && controller.isShowing()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        controller.updatePausePlay();
                    }
                    break;
            }
        }
    }

    private long setProgress() {
        if (mPlayer == null || mDragging)
            return 0;

        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        mDuration = duration;

        if (mEndTime != null)
            mEndTime.setText(generateTime(mDuration));
        if (mCurrentTime != null)
            mCurrentTime.setText(generateTime(position));

        return position;
    }

    private static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
        }
    }

    private View.OnClickListener mPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            doPauseResume();
        }
    };

    private void updatePausePlay() {
        if (mPlayer != null) {
            if (mListener != null) {
                mListener.onPauseOrPlay(!mPlayer.isPlaying());
            }
            if (mPlayer.isPlaying()) {
                playState = STATE.isPlaying;
            }
            if (mPauseCheckbox != null) {
                mPauseCheckbox.setChecked(mPlayer.isPlaying());
            }
        }
    }

    private void doPauseResume() {
        if (mPlayer == null) return;
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            playState = STATE.isPaused;
        } else {
            mPlayer.start();
            playState = STATE.isPlaying;
        }
        updatePausePlay();
    }

    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {

        public void onStartTrackingTouch(SeekBar bar) {
            if (NetWorkUtil.NETWORN_NONE == NetWorkUtil.getActiveNetwork(mContext)) {
                ToastUtil.show("网络不可用");
                if (playState == STATE.isPlaying) pause();
                if (mStateController != null) mStateController.showNoNetWorkView();
                hide();
                return;
            }
            mDragging = true;
            show(sDefaultTimeout);
            mHandler.removeMessages(SHOW_PROGRESS);
            if (mInstantSeeking && mAM != null) {
                mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (NetWorkUtil.NETWORN_NONE == NetWorkUtil.getActiveNetwork(mContext)) {
                ToastUtil.show("网络不可用");
                if (playState == STATE.isPlaying) pause();
                if (mStateController != null) mStateController.showNoNetWorkView();
                hide();
                return;
            }

            if (!fromuser)
                return;
            if (progress > 999) progress = 999;
            final long newposition = (long) (mDuration * progress) / 1000;
            String time = generateTime(newposition);
            if (mInstantSeeking) {
                mHandler.removeCallbacks(mLastSeekBarRunnable);
                mLastSeekBarRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mPlayer.seekTo(newposition);
                    }
                };
                mHandler.postDelayed(mLastSeekBarRunnable, SEEK_TO_POST_DELAY_MILLIS);
            }
            if (mCurrentTime != null)
                mCurrentTime.setText(time);
        }

        public void onStopTrackingTouch(SeekBar bar) {
            if (!mInstantSeeking) {
                if (bar.getMax() == bar.getProgress()) {
                    mPlayer.pause();
                    ToastUtil.show("播放结束");
                } else {
                    mPlayer.seekTo(mDuration * bar.getProgress() / 1000);
                }
            }


            show(sDefaultTimeout);
            mHandler.removeMessages(SHOW_PROGRESS);
            if (!isMute && mAM != null) {
                mAM.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }
            mDragging = false;
            mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
        }
    };


    /**
     * Set the view that acts as the anchor for the control view.
     * <p>
     * - This can for example be mSurface VideoView, or your Activity'format2 main view.
     * - AudioPlayer has no anchor view, so the view parameter will be null.
     *
     * @param view The view to which to anchor the controller when it is visible.
     *             每次开始播放都会被七牛播放器回调
     * @deprecated by setAnchorView(ViewGroup container)
     */
    @Override
    public void setAnchorView(View view) {
//        if (mAnchor == null && mListener != null) {
//            if (view instanceof ViewGroup) {
//                mAnchor = (ViewGroup) view;
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                //将videoView放置在mAnchor的最底层，controllerView则放置在最上层，其余包括StateController在内的页面逻辑视图都在中间层
//                //mAnchor必须是RelativeLayout或FrameLayout
//                mAnchor.addView(mVideoView, 0, lp);
//            } else {
//                throw new RuntimeException((getClass().getSimpleName() + "Init Failed!"));
//            }
//            if (isFullScreen) {
//                popupView = mListener.makeLandControllerView();
//            } else {
//                popupView = mListener.makeControllerView();
//            }
//            initControllerView(popupView);
//        }
    }

    //不会被七牛播放器回调
     /*将videoView放置在mAnchor的最底层，controllerView则放置在最上层，其余包括StateController在内的页面逻辑视图都在中间层
    controllerView与中间层未消费的点击事件最终传递给videoView，videoView响应onTouchEvent()方法控制controllerView的展示
    videoView未获取到PLMediaPlayer时不会响应show()/hide()方法
    mAnchor必须是RelativeLayout或FrameLayout
    */
    public void setAnchorView(ViewGroup container) {
        if (mAnchor == null && mListener != null) {
            if (container instanceof RelativeLayout || container instanceof FrameLayout) {
                mAnchor = container;
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mAnchor.addView(mVideoView, 0, lp);
                if (isFullScreen) {
                    popupView = mListener.makeLandControllerView();
                } else {
                    popupView = mListener.makeControllerView();
                }
                initControllerView(popupView);
            } else {
                throw new RuntimeException((getClass().getSimpleName() + "Init Failed! require RelativeLayout||FrameLayout!"));
            }
        }
    }

    private void popupDismiss() {
        if (popupView == null) return;
        popupView.setVisibility(View.GONE);
        mShowing = false;
    }

    private void popupShow() {
        if (popupView == null) return;
        popupView.setVisibility(View.VISIBLE);
        mShowing = true;
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    @Override
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Show the controller on screen. It will go away automatically after
     * 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show the controller until hide() is called.
     */
    @Override
    public void show(int timeout) {
        if (isVideoSizeAlreadyGot && isFirstAfterResize) {
            isFirstAfterResize = false;
            return;
        }
        if (!mShowing) {
            popupShow();
            if (mOnShownListener != null) {
                mOnShownListener.onShown();
            }
        }
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT), timeout);
        }
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    public boolean isDragging() {
        return mDragging;
    }

    @Override
    public void hide() {
        if (alwaysShow) return;
        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                popupDismiss();
            } catch (Exception ex) {
                LogUtil.d(TAG, "MediaController already removed");
            }
            if (mHiddenListener != null)
                mHiddenListener.onHidden();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mProgress != null && !mDisableProgress)
            mProgress.setEnabled(enabled);
    }

    public void resizeVideoHeightByPiexl(int height) {
        //重新设置videoView的高度
        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();
        if (lp != null) {
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        if (isFullScreen) {
            lp.height = MyApplication.getInstance().getHeight();
        } else {
            defaultHeight = height;
            lp.height = height;
        }

        if (lp instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) lp).addRule(RelativeLayout.CENTER_IN_PARENT);
        }
        if (lp instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) lp).gravity = Gravity.CENTER;
        }
        mVideoView.setLayoutParams(lp);

        if (mAnchor != null) {
            ViewGroup.LayoutParams mAnchor_lp = mAnchor.getLayoutParams();
            mAnchor_lp.height = lp.height;
            mAnchor.setLayoutParams(mAnchor_lp);
        }
    }

    private void resizeVideoView() {
        int screenX = MyApplication.getInstance().getWidth();
        int screenY = MyApplication.getInstance().getHeight();
        //重新设置视频的宽高
        if (isVideoSizeAlreadyGot && screenX > 0 && screenY > 0 &&
                plMediaPlayer != null && plMediaPlayer.getVideoWidth() > 0 && plMediaPlayer.getVideoHeight() > 0) {
            ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();
            if (lp != null) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            if (isFullScreen) {
                long ratio = plMediaPlayer.getVideoHeight() / plMediaPlayer.getVideoWidth();//视频流高宽比，eg:10:16
                long ratio_phone = screenY / screenX;//屏幕宽高比,eg:9:16
                //自适应屏幕宽高
                if (ratio > ratio_phone) {
                    lp.width = screenX;
                    lp.height = screenX * plMediaPlayer.getVideoHeight() / plMediaPlayer.getVideoWidth();
                } else {
                    lp.width = screenY * plMediaPlayer.getVideoWidth() / plMediaPlayer.getVideoHeight();
                    lp.height = screenY;
                }
            } else {
                lp.height = screenX * plMediaPlayer.getVideoHeight() / plMediaPlayer.getVideoWidth();
            }

            LogUtil.d(TAG, "resizeVideoView: originWidth:" + plMediaPlayer.getVideoWidth() + "originHeight:" + plMediaPlayer.getVideoHeight() + "\nwidth:" + lp.width + "height:" + lp.height);
            if (lp instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) lp).addRule(RelativeLayout.CENTER_IN_PARENT);
            }
            if (lp instanceof FrameLayout.LayoutParams) {
                ((FrameLayout.LayoutParams) lp).gravity = Gravity.CENTER;
            }
            mVideoView.setLayoutParams(lp);

            if (mAnchor != null) {
                ViewGroup.LayoutParams mAnchor_lp = mAnchor.getLayoutParams();

                if (mAnchor_lp == null) {
                    mAnchor_lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }

                if (isFullScreen) {
                    /** 需注意与下面两行代码的差别
                     *  </mAnchor_lp.width = MyApplication.getInstance().getWidth();
                     *  mAnchor_lp.height = MyApplication.getInstance().getHeight();/>
                     *  使用ViewGroup.LayoutParams.MATCH_PARENT时，每当导航栏隐藏或者出现，mAnchor的布局边界会跟随改变，而上面两行代码则不会
                     */
                    mAnchor_lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    mAnchor_lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                } else {
                    mAnchor_lp.width = lp.width;
                    mAnchor_lp.height = lp.height;
                }
                mAnchor.setLayoutParams(mAnchor_lp);
            }
        } else {
            resizeVideoHeightByPiexl(defaultHeight);
        }
    }

    public void changeToFullScreen(boolean full) {
        isFullScreen = full;

        popupDismiss();

        if (full) {
            WindowManager.LayoutParams layoutParams1 = ((Activity) mContext).getWindow().getAttributes();
            layoutParams1.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            ((Activity) mContext).getWindow().setAttributes(layoutParams1);

            //自动隐藏导航栏
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//            }

            resizeVideoView();
            popupView = mListener.makeLandControllerView();
        } else {
            WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity) mContext).getWindow().setAttributes(params);

//            mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            resizeVideoView();
            popupView = mListener.makeControllerView();
        }
        initControllerView(popupView);

        if (alwaysShow) {
            show();
        }
    }

    public void stopOrientationEventListener(boolean stop) {
        if (stop) {
            if (orientationEventListener != null)
                orientationEventListener.disable();
        } else {
            if (orientationEventListener != null)
                orientationEventListener.enable();
        }
    }

    private OrientationEventListener orientationEventListener;


    public void setAlwaysShow(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public boolean isVideoSizeAlreadyGot() {
        return isVideoSizeAlreadyGot;
    }

    //播放视频前检查手机是否处于通话状态，是则设置静音，但这里不需要修改isMute的值
    private void setMuteOnCalling() {
        if (mTM == null || mAM == null) return;
        if (mTM.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
            mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
    }

    private String url = "rtmp://live.hkstv.hk.lxdns.com/live/hks";

    //如果与正在播放的流相同，则会继续播放而不是从头播放
    public void startPlay(String url) {

        if (TextUtils.isEmpty(url)) {
            url = this.url;
        }

        if (mVideoView != null && !TextUtils.isEmpty(url)) {
            if (url.equals(url_latest)) {//如果与正在播放的是同一个流
                if (playState == STATE.isPlaying) {
                    if (mStateController != null) {
                        mStateController.showPlayView();
                    }
                    return;
                }
                if (playState == STATE.isEnded) {
                    startPlayForce(url);
                } else {
                    if (mStateController != null) {
                        mStateController.showPlayView();
                    }
                    start();
                }
            } else {
                startPlayForce(url);
            }
        }
    }

    //一定是从头播放这个流
    public void startPlayForce(String url) {
        setMuteOnCalling();

        if (mVideoView != null && !TextUtils.isEmpty(url)) {
            if (mStateController != null) {
                mStateController.showLoadingView();
            }

            mVideoView.setVideoPath(url);
            url_latest = url;
            mVideoView.start();
            isVideoSizeAlreadyGot = false;
            isFirstAfterResize = true;
        }
    }

    //暂停之后的继续播放
    public void start() {
        setMuteOnCalling();

        if (mVideoView != null) {
            mVideoView.start();
            playState = STATE.isPlaying;
        }
    }

    //暂停
    public void pause() {
        if (mVideoView != null) {
            if (playState == STATE.isPlaying) {
                mVideoView.pause();
                playState = STATE.isPaused;
            }
        }
    }

    //停止播放
    public void stopPlay() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();//同步执行，会造成线程阻塞
            playState = STATE.isEnded;
        }
    }


    public void setOnVideoSizeChangeListener(OnVideoSizeChangedListener mOnVideoSizeChangeListener) {
        this.mOnVideoSizeChangeListener = mOnVideoSizeChangeListener;
    }

    public void setOnCompletionListener(OnCompletionListener mOnCompletionListener) {
        this.mOnCompletionListener = mOnCompletionListener;
    }

    public void setOnErrorListener(OnErrorListener mOnErrorListener) {
        this.mOnErrorListener = mOnErrorListener;
    }

    public void setOnInfoListener(OnInfoListener mOnInfoListener) {
        this.mOnInfoListener = mOnInfoListener;
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener mOnBufferingUpdateListener) {
        this.mOnBufferingUpdateListener = mOnBufferingUpdateListener;
    }

    public void setOnPreparedListener(OnPreparedListener mOnPreparedListener) {
        this.mOnPreparedListener = mOnPreparedListener;
    }

    //获取当前播放器状态
    public STATE getPlayState() {
        if (mVideoView != null) {
            return playState;
        }
        return STATE.idle;
    }

    public void setLive(boolean mLive) {
        if (mLive != isLive) {
            isLive = mLive;
        }
        if (popupView != null) {
            initControllerView(popupView);
        }
    }

    public boolean isLive() {
        return isLive;
    }

    public void setDefaultHeight(int mDefaultHeight) {
        defaultHeight = mDefaultHeight;
    }

    public long getDuration() {
        if (mPlayer == null) return 0;

        return mPlayer.getDuration();
    }

    public long getCurrentPosition() {
        if (mPlayer == null) return 0;

        return mPlayer.getCurrentPosition();
    }

    //获取当前帧的bitmap
    public Bitmap getBitmap() {
        if (mVideoView == null) return null;
        return mVideoView.getTextureView().getBitmap();
    }

    //销毁界面时，必须调用
    public void destroy() {
        if (mVideoView != null) {
            mVideoView.releaseSurfactexture();
            mVideoView.stopPlayback();
            mVideoView = null;
        }
        if (mPlayer != null) {
            mPlayer = null;
        }

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);//移除所有任务
            mHandler = null;
        }

        setControlListener(null);
        if (mTM != null) {
            if (mPhoneStateListener != null) {
                mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);//注销监听
            }
            mTM = null;
        }
        if (mAM != null) {
            mAM = null;
        }
    }


    private void onCallStateChange() {
        if (mTM == null || mAM == null) return;

        if (isMute) {
            mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
        } else {
            if (mTM.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
                mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
            } else {
                mAM.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }
        }
    }
}
