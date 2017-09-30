package com.dovar.pili;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.dovar.pili.qos.StreamParam;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkLibLoader;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaInfo;

/**
 * Created by heweizong on 2017/9/29.
 */

public class PLMediaPlayer {
    private Context mContext;
    private IjkMediaPlayer mIjkMediaPlayer;
    private long startTimeMilli;//用于计算preparedTime
    private static volatile boolean d = false;
    private StreamParam mStreamParam;
    private PrepareAsyncHandler prepareAsyncHandler;
    private long bufferingTotalCount;
    private long bufferingTotalTimes;
    private long i;
    private int j;
    private boolean k;
    private boolean l;
    private boolean m;
    private boolean n;
    private PlayerState mPlayerState;
    private String video_url;
    private Map<String, String> uri_headers;
    private SurfaceHolder mSurfaceHolder;
    private Surface mSurface;
    private AVOptions mAVOptions;
    private boolean isLiveStream;
    private long v;
    private long w;
    private tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener mIjkOnVideoSizeChangeListener;
    private tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener mIjkOnPreparedListener;
    private tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener mIjkOnSeekCompleteListener;
    private tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener mIjkOnInfoListener;
    private tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener mIjkOnBufferingUpdateListener;
    private tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener mIjkOnCompletionListener;
    private tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener mIjkOnErrorListener;
    private IjkMediaPlayer.OnNativeInvokeListener mOnNativeInvokeListener;
    public static final int MEDIA_INFO_UNKNOWN = 1;
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    public static final int MEDIA_INFO_BUFFERING_BYTES_UPDATE = 503;
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
    public static final int MEDIA_INFO_SWITCHING_SW_DECODE = 802;
    public static final int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;
    public static final int MEDIA_INFO_AUDIO_RENDERING_START = 10002;
    public static final int MEDIA_INFO_VIDEO_GOP_TIME = 10003;
    public static final int MEDIA_INFO_VIDEO_FRAME_RENDERING = 10004;
    public static final int MEDIA_INFO_AUDIO_FRAME_RENDERING = 10005;
    private PLMediaPlayer.OnInfoListener mOnInfoListener;
    private PLMediaPlayer.OnPreparedListener mOnPreparedListener;
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener;
    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private PLMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    public static final int MEDIA_ERROR_UNKNOWN = -1;
    public static final int ERROR_CODE_INVALID_URI = -2;
    public static final int ERROR_CODE_IO_ERROR = -5;
    public static final int ERROR_CODE_STREAM_DISCONNECTED = -11;
    public static final int ERROR_CODE_EMPTY_PLAYLIST = -541478725;
    public static final int ERROR_CODE_404_NOT_FOUND = -875574520;
    public static final int ERROR_CODE_CONNECTION_REFUSED = -111;
    public static final int ERROR_CODE_CONNECTION_TIMEOUT = -110;
    public static final int ERROR_CODE_UNAUTHORIZED = -825242872;
    public static final int ERROR_CODE_PREPARE_TIMEOUT = -2001;
    public static final int ERROR_CODE_READ_FRAME_TIMEOUT = -2002;
    public static final int ERROR_CODE_HW_DECODE_FAILURE = -2003;
    private PLMediaPlayer.OnErrorListener mOnErrorListener;
    private OnMediaDataListener mOnMediaDataListener;

    public PLMediaPlayer(Context var1) {
        this(var1, null);
    }

    public PLMediaPlayer(Context mContext, AVOptions mAVOptions) {
        this.startTimeMilli = 0L;
        this.mStreamParam = new StreamParam();
        this.j = 0;
        this.k = false;
        this.l = false;
        this.m = false;
        this.n = true;
        this.mPlayerState = PlayerState.IDLE;
        this.video_url = null;
        this.uri_headers = null;
        this.mSurfaceHolder = null;
        this.mSurface = null;
        this.mAVOptions = null;
        this.isLiveStream = false;
        this.v = 0L;
        this.w = 0L;
        this.mIjkOnVideoSizeChangeListener = new tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(IMediaPlayer var1, int var2, int var3, int var4, int var5) {
                if (PLMediaPlayer.this.mOnVideoSizeChangedListener != null) {
                    PLMediaPlayer.this.mOnVideoSizeChangedListener.onVideoSizeChanged(PLMediaPlayer.this, var2, var3, var4, var5);
                }

            }
        };
        this.mIjkOnPreparedListener = new tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener() {
            public void onPrepared(IMediaPlayer var1) {
                int preparedTime = (int) (System.currentTimeMillis() - startTimeMilli);
                if (mOnPreparedListener != null) {
                    mOnPreparedListener.onPrepared(PLMediaPlayer.this, preparedTime);
                }

                Log.d("PLMediaPlayer", "on prepared: " + preparedTime + " ms");
                mPlayerState = PlayerState.PREPARED;
            }
        };
        this.mIjkOnSeekCompleteListener = new tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener() {
            public void onSeekComplete(IMediaPlayer var1) {
                if (mOnSeekCompleteListener != null) {
                    mOnSeekCompleteListener.onSeekComplete(PLMediaPlayer.this);
                }

            }
        };
        this.mIjkOnInfoListener = new tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener() {
            public boolean onInfo(IMediaPlayer var1, int var2, int var3) {
                switch (var2) {
                    case 3:
                        long var4 = System.currentTimeMillis() - PLMediaPlayer.this.startTimeMilli - PLMediaPlayer.this.w;
                        mStreamParam.firstVideoRenderedTime = var4;
                        Log.d("PLMediaPlayer", "first video rendered: " + var4 + " ms");
                        mPlayerState = PlayerState.PLAYING;
                        var3 = (int) var4;
                        if (prepareAsyncHandler != null) {
                            prepareAsyncHandler.sendMessage(PLMediaPlayer.this.prepareAsyncHandler.obtainMessage(0));
                        }
                        break;
                    case 701:
                        Log.d("PLMediaPlayer", "MEDIA_INFO_BUFFERING_START");
                        PLMediaPlayer.this.mPlayerState = PlayerState.BUFFERING;
                        PLMediaPlayer.this.mStreamParam.bufferingTimes = 1L;
                        PLMediaPlayer.this.i = System.currentTimeMillis();
                        break;
                    case 702:
                        Log.d("PLMediaPlayer", "MEDIA_INFO_BUFFERING_END");
                        PLMediaPlayer.this.mPlayerState = PlayerState.PLAYING;
                        PLMediaPlayer.this.mStreamParam.bufferingTimes = 1L;
                        PLMediaPlayer.this.bufferingTotalCount++;
                        PLMediaPlayer.this.bufferingTotalTimes = PLMediaPlayer.this.bufferingTotalTimes + (System.currentTimeMillis() - PLMediaPlayer.this.i);
                        PLMediaPlayer.this.i = 0L;
                        break;
                    case 10002:
                        long var6 = System.currentTimeMillis() - PLMediaPlayer.this.startTimeMilli - PLMediaPlayer.this.w;
                        PLMediaPlayer.this.mStreamParam.o = var6;
                        Log.d("PLMediaPlayer", "first audio rendered: " + var6 + " ms");
                        PLMediaPlayer.this.mPlayerState = PlayerState.PLAYING;
                        var3 = (int) var6;
                        break;
                    case 10003:
                        PLMediaPlayer.this.m = true;
                        PLMediaPlayer.this.mStreamParam.p = (long) var3;
                }

                if (!PLMediaPlayer.this.k && PLMediaPlayer.this.m && PLMediaPlayer.this.mStreamParam.firstVideoRenderedTime > 0L && PLMediaPlayer.this.mStreamParam.o > 0L) {
                    PLMediaPlayer.this.c();
                    PLMediaPlayer.this.d();
                }

                if (PLMediaPlayer.this.mOnInfoListener != null) {
                    PLMediaPlayer.this.mOnInfoListener.onInfo(PLMediaPlayer.this, var2, var3);
                }

                return true;
            }
        };
        this.mIjkOnBufferingUpdateListener = new tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(IMediaPlayer var1, int var2) {
                if (PLMediaPlayer.this.mOnBufferingUpdateListener != null) {
                    PLMediaPlayer.this.mOnBufferingUpdateListener.onBufferingUpdate(PLMediaPlayer.this, var2);
                }

            }
        };
        this.mIjkOnCompletionListener = new tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer var1) {
                if (PLMediaPlayer.this.mOnCompletionListener != null) {
                    PLMediaPlayer.this.mOnCompletionListener.onCompletion(PLMediaPlayer.this);
                }

                mPlayerState = PlayerState.COMPLETED;
                if (!PLMediaPlayer.this.l) {
                    PLMediaPlayer.this.optimizeLiveStream(0, 0);
                }

            }
        };
        this.mIjkOnErrorListener = new tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener() {
            public boolean onError(IMediaPlayer var1, int var2, int var3) {
                Log.e("PLMediaPlayer", "Error happened, what = " + var2 + ", errorCode = " + var3);
                if (var3 == 0) {
                    var3 = -1;
                } else if (var3 == -2003 && PLMediaPlayer.this.j == 2) {
                    j = 0;
                    PLMediaPlayer.this.mAVOptions.setInteger("mediacodec", PLMediaPlayer.this.j);
                    PLMediaPlayer.this.mAVOptions.setInteger("start-on-prepared", 1);
                    if (mOnInfoListener != null) {
                        mOnInfoListener.onInfo(PLMediaPlayer.this, 802, 0);
                    }

                    prepare();
                    return true;
                }

                PLMediaPlayer.this.mPlayerState = PlayerState.ERROR;
                if (!PLMediaPlayer.this.l) {
                    PLMediaPlayer.this.optimizeLiveStream(var3, var3);
                }

                return PLMediaPlayer.this.mOnErrorListener != null ? PLMediaPlayer.this.mOnErrorListener.onError(PLMediaPlayer.this, var3) : false;
            }
        };
        this.mOnNativeInvokeListener = new IjkMediaPlayer.OnNativeInvokeListener() {
            private final int b = 196609;
            private final int c = 196610;

            public boolean onNativeInvoke(int var1, Bundle var2) {
                if (PLMediaPlayer.this.mOnMediaDataListener != null) {
                    switch (var1) {
                        case 196609:
                            PLMediaPlayer.this.mOnMediaDataListener.a(PLMediaPlayer.this.mIjkMediaPlayer.getAudioData(), PLMediaPlayer.this.mIjkMediaPlayer.getAudioSize(), PLMediaPlayer.this.mIjkMediaPlayer.getAudioPts(), PLMediaPlayer.this.mIjkMediaPlayer.getAudioChannel(), PLMediaPlayer.this.mIjkMediaPlayer.getAudioChannelLayout(), PLMediaPlayer.this.mIjkMediaPlayer.getAudioSampleRate(), PLMediaPlayer.this.mIjkMediaPlayer.getAudioSampleFormat());
                            return true;
                        case 196610:
                            PLMediaPlayer.this.mOnMediaDataListener.a(PLMediaPlayer.this.mIjkMediaPlayer.getVideoData(), PLMediaPlayer.this.mIjkMediaPlayer.getVideoLinesize(), PLMediaPlayer.this.mIjkMediaPlayer.getVideoPts(), PLMediaPlayer.this.mIjkMediaPlayer.getVideoFormat(), PLMediaPlayer.this.mIjkMediaPlayer.getVideoPlane(), PLMediaPlayer.this.mIjkMediaPlayer.getVideoWidth(), PLMediaPlayer.this.mIjkMediaPlayer.getVideoHeight(), PLMediaPlayer.this.mIjkMediaPlayer.getVideoSarNum(), PLMediaPlayer.this.mIjkMediaPlayer.getVideoSarDen());
                            return true;
                    }
                }

                return false;
            }
        };
        this.mContext = mContext.getApplicationContext();
        this.mAVOptions = mAVOptions;
        com.dovar.pili.qos.b.a(this.mContext);
        this.optimizeLiveStream(mAVOptions);
        this.mStreamParam.reset();
    }

    private void optimizeLiveStream(AVOptions var1) {
        this.mPlayerState = PlayerState.IDLE;
        this.l = false;
        this.mIjkMediaPlayer = new IjkMediaPlayer(new IjkLibLoader() {
            public void loadLibrary(String var1) throws UnsatisfiedLinkError, SecurityException {
                if (!PLMediaPlayer.d) {
                    Log.i("PLMediaPlayer", "load shared lib:" + SharedLibraryNameHelper.getInstance().getSharedLibraryName());
                    PLMediaPlayer.d = true;
                    SharedLibraryNameHelper.getInstance().a();
                    IjkMediaPlayer.native_setLogLevel(6);
                }

            }
        });
        this.mIjkMediaPlayer.setOnPreparedListener(this.mIjkOnPreparedListener);
        this.mIjkMediaPlayer.setOnInfoListener(this.mIjkOnInfoListener);
        this.mIjkMediaPlayer.setOnErrorListener(this.mIjkOnErrorListener);
        this.mIjkMediaPlayer.setOnCompletionListener(this.mIjkOnCompletionListener);
        this.mIjkMediaPlayer.setOnBufferingUpdateListener(this.mIjkOnBufferingUpdateListener);
        this.mIjkMediaPlayer.setOnSeekCompleteListener(this.mIjkOnSeekCompleteListener);
        this.mIjkMediaPlayer.setOnVideoSizeChangedListener(this.mIjkOnVideoSizeChangeListener);
        this.mIjkMediaPlayer.setOnNativeInvokeListener(this.mOnNativeInvokeListener);
        this.setAVOptions(var1);
    }

    public void release() {
        if (!this.n) {
            this.stop();
        }

        this.mIjkMediaPlayer.release();
        this.mPlayerState = PlayerState.IDLE;
    }

    public void setDebugLoggingEnabled(boolean var1) {
        if (var1) {
            IjkMediaPlayer.native_setLogLevel(3);
        } else {
            IjkMediaPlayer.native_setLogLevel(6);
        }

    }

    public PlayerState getPlayerState() {
        return this.mPlayerState;
    }

    public HashMap<String, String> getMetadata() {
        HashMap var1 = new HashMap();
        MediaInfo var2 = this.mIjkMediaPlayer.getMediaInfo();
        Set var3 = var2.mMeta.mMediaMeta.keySet();
        Iterator var4 = var3.iterator();

        while (var4.hasNext()) {
            String var5 = (String) var4.next();
            IjkMediaMeta var10001 = var2.mMeta;
            if (var5.compareTo("streams") != 0) {
                try {
                    String var6 = var2.mMeta.mMediaMeta.getString(var5);
                    if (var6 != null) {
                        var1.put(var5, var6);
                    }
                } catch (ClassCastException var7) {
                    ;
                }
            }
        }

        return var1;
    }

    //如果是直播流，优化  ----》》》还没看优化了什么，反正七牛说是优化
    private void optimizeLiveStream(String var1, String var2) {
        if (this.isLiveStream) {
            try {
                this.k = false;
                this.m = false;
                this.l = false;
                URI var3 = new URI(var1);
                URI var4 = new URI(var2);
                Intent var5 = new Intent("pldroid-player-qos-filter");
                var5.putExtra("pldroid-qos-msg-type", 4);
                var5.putExtra("scheme", var3.getScheme());
                var5.putExtra("domain", var3.getHost());
                var5.putExtra("remoteIp", var4.getHost());
                var5.putExtra("path", var3.getPath());
                com.dovar.pili.qos.c.a().a(var5);
            } catch (URISyntaxException var6) {
                var6.printStackTrace();
            } catch (Exception var7) {
                var7.printStackTrace();
            }

        }
    }

    private void c() {
        try {
            HashMap var1 = this.getMetadata();
            if (var1.containsKey("remote_ip")) {
                String var2 = (String) var1.get("remote_ip");
                if (var2.length() > 0) {
                    URI var3 = new URI(this.video_url);
                    Intent var4 = new Intent("pldroid-player-qos-filter");
                    var4.putExtra("pldroid-qos-msg-type", 4);
                    var4.putExtra("scheme", var3.getScheme());
                    var4.putExtra("domain", var3.getHost());
                    var4.putExtra("remoteIp", var2);
                    var4.putExtra("path", var3.getPath());
                    com.dovar.pili.qos.c.a().a(var4);
                }
            }
        } catch (URISyntaxException var5) {
            var5.printStackTrace();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    private void d() {
        if (this.isLiveStream) {
            this.k = true;
            Intent var1 = new Intent("pldroid-player-qos-filter");
            var1.putExtra("pldroid-qos-msg-type", 195);
            var1.putExtra("firstVideoTime", this.mStreamParam.firstVideoRenderedTime);
            var1.putExtra("firstAudioTime", this.mStreamParam.o);
            var1.putExtra("gopTime", this.mStreamParam.p);
            if (this.j == 0) {
                this.mStreamParam.format1 = "ffmpeg";
                this.mStreamParam.format2 = "ffmpeg";
            } else {
                this.mStreamParam.format1 = "droid264";
                this.mStreamParam.format2 = "droidaac";
            }

            var1.putExtra("videoDecoderType", this.mStreamParam.format1);
            var1.putExtra("audioDecoderType", this.mStreamParam.format2);
            HashMap var2 = this.getMetadata();
            String var3;
            if (var2.containsKey("tcp_connect_time")) {
                var3 = (String) var2.get("tcp_connect_time");
                var1.putExtra("tcpConnectTime", Integer.parseInt(var3));
            }

            if (var2.containsKey("first_byte_time")) {
                var3 = (String) var2.get("first_byte_time");
                int var4 = (int) (Long.parseLong(var3) - this.startTimeMilli);
                var1.putExtra("firstByteTime", var4);
            }

            com.dovar.pili.qos.c.a().a(var1);
        }
    }

    private void optimizeLiveStream(int errorCode, int errorOSCode) {
        if (this.isLiveStream && (this.mSurface != null || this.mSurfaceHolder != null)) {
            this.l = true;
            Intent mIntent = new Intent("pldroid-player-qos-filter");
            mIntent.putExtra("pldroid-qos-msg-type", 196);
            mIntent.putExtra("beginAt", this.startTimeMilli);
            mIntent.putExtra("endAt", System.currentTimeMillis());
            mIntent.putExtra("bufferingTotalCount", this.bufferingTotalCount);
            mIntent.putExtra("bufferingTotalTimes", this.bufferingTotalTimes);
            mIntent.putExtra("totalRecvBytes", this.mStreamParam.totalRecvBytes);
            int var4 = (int) (this.i > 0L ? System.currentTimeMillis() - this.i : this.i);
            mIntent.putExtra("endBufferingTime", var4);
            mIntent.putExtra("gopTime", this.mStreamParam.p);
            mIntent.putExtra("errorCode", errorCode);
            mIntent.putExtra("errorOSCode", errorOSCode);
            HashMap var5 = this.getMetadata();
            String var6;
            if (var5.containsKey("tcp_connect_time")) {
                var6 = (String) var5.get("tcp_connect_time");
                mIntent.putExtra("tcpConnectTime", Integer.parseInt(var6));
            }

            if (var5.containsKey("rtmp_connect_time")) {
                var6 = (String) var5.get("rtmp_connect_time");
                mIntent.putExtra("rtmpConnectTime", Integer.parseInt(var6));
            }

            if (var5.containsKey("first_byte_time")) {
                var6 = (String) var5.get("first_byte_time");
                int var7 = (int) (Long.parseLong(var6) - this.startTimeMilli);
                mIntent.putExtra("firstByteTime", var7);
            }

            com.dovar.pili.qos.c.a().a(mIntent);
        }
    }

    private void setAVOptions(AVOptions var1) {
        if (var1 != null) {
            this.mIjkMediaPlayer.setOption(4, "framedrop", 12L);
            this.mIjkMediaPlayer.setOption(4, "start-on-prepared", 1L);
            this.mIjkMediaPlayer.setOption(1, "http-detect-range-support", 0L);
            this.mIjkMediaPlayer.setOption(2, "skip_loop_filter", 0L);
            this.mIjkMediaPlayer.setOption(4, "overlay-format", 842225234L);
            int var2 = var1.getInteger("start-on-prepared", 1);
            this.mIjkMediaPlayer.setOption(4, "start-on-prepared", (long) var2);
            this.isLiveStream = false;
            if (var1.containsKey("live-streaming") && var1.getInteger("live-streaming") != 0) {
                this.isLiveStream = true;
                if (!var1.containsKey("rtmp_live") || var1.getInteger("rtmp_live") == 1) {
                    this.mIjkMediaPlayer.setOption(1, "rtmp_live", 1L);
                }

                this.mIjkMediaPlayer.setOption(1, "rtmp_buffer", var1.containsKey("rtmp_buffer") ? (long) var1.getInteger("rtmp_buffer") : 1000L);
                if (var1.containsKey("timeout")) {
                    this.mIjkMediaPlayer.setOption(1, "timeout", (long) (var1.getInteger("timeout") * 1000));
                }

                this.mIjkMediaPlayer.setOption(2, "threads", "1");
            }

            String var3 = "analyzeduration";
            this.mIjkMediaPlayer.setOption(1, var3, var1.containsKey(var3) ? (long) var1.getInteger(var3) : 0L);
            this.mIjkMediaPlayer.setOption(1, "probesize", var1.containsKey("probesize") ? (long) var1.getInteger("probesize") : 131072L);
            this.mIjkMediaPlayer.setOption(4, "live-streaming", (long) (this.isLiveStream ? 1 : 0));
            this.mIjkMediaPlayer.setOption(4, "get-av-frame-timeout", var1.containsKey("get-av-frame-timeout") ? (long) (var1.getInteger("get-av-frame-timeout") * 1000) : 10000000L);
            this.j = var1.containsKey("mediacodec") ? var1.getInteger("mediacodec") : 0;
            this.mIjkMediaPlayer.setOption(4, "mediacodec", this.j == 0 ? 0L : 1L);
            String var4 = "mediacodec-handle-resolution-change";
            this.mIjkMediaPlayer.setOption(4, var4, var1.containsKey(var4) ? (long) var1.getInteger(var4) : 1L);
            this.mIjkMediaPlayer.setOption(4, "delay-optimization", var1.containsKey("delay-optimization") ? (long) var1.getInteger("delay-optimization") : 0L);
            this.mIjkMediaPlayer.setOption(4, "cache-buffer-duration", var1.containsKey("cache-buffer-duration") ? (long) var1.getInteger("cache-buffer-duration") : 2000L);
            this.mIjkMediaPlayer.setOption(4, "max-cache-buffer-duration", var1.containsKey("max-cache-buffer-duration") ? (long) var1.getInteger("max-cache-buffer-duration") : 4000L);
            this.mIjkMediaPlayer.setOption(1, "reconnect", var1.containsKey("reconnect") ? (long) var1.getInteger("reconnect") : 1L);
            this.mIjkMediaPlayer.setOption(4, "audio-render-msg-cb", var1.containsKey("audio-render-msg-cb") ? (long) var1.getInteger("audio-render-msg-cb") : 0L);
            this.mIjkMediaPlayer.setOption(4, "video-render-msg-cb", var1.containsKey("video-render-msg-cb") ? (long) var1.getInteger("video-render-msg-cb") : 0L);
            this.mIjkMediaPlayer.setOption(4, "nodisp", var1.containsKey("nodisp") ? (long) var1.getInteger("nodisp") : 0L);
        }
    }

    public boolean optimizeLiveStream() {
        return this.isLiveStream;
    }

    public void setDisplay(SurfaceHolder var1) {
        this.mIjkMediaPlayer.setDisplay(var1);
        this.mSurfaceHolder = var1;
    }

    public void setSurface(Surface var1) {
        this.mIjkMediaPlayer.setSurface(var1);
        this.mSurface = var1;
    }

    public void setWakeMode(Context var1, int var2) {
        this.mIjkMediaPlayer.setWakeMode(var1, var2);
    }

    public void setScreenOnWhilePlaying(boolean var1) {
        this.mIjkMediaPlayer.setScreenOnWhilePlaying(var1);
    }

    public void setDataSource(Context var1, Uri var2) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.setDataSource(var1, var2, null);
    }

    public void setDataSource(Context var1, Uri var2, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        Uri var4 = PLNetworkManager.getInstance().a(var2);
        optimizeLiveStream(var2.toString(), var4.toString());
        if (Build.VERSION.SDK_INT > 14) {
            this.mIjkMediaPlayer.setDataSource(var1, var4, headers);
            this.uri_headers = headers;
        } else {
            this.mIjkMediaPlayer.setDataSource(var4.toString());
        }

        this.video_url = var2.toString();
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        String var2 = PLNetworkManager.getInstance().a(path);
        optimizeLiveStream(path, var2);
        this.mIjkMediaPlayer.setDataSource(var2);
        this.video_url = path;
    }

    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        this.mIjkMediaPlayer.setDataSource(fd);
    }

    public String getDataSource() {
        return this.mIjkMediaPlayer.getDataSource();
    }

    public void prepareAsync() throws IllegalStateException {
        String var1 = this.getDataSource();
        if (var1 != null && var1.contains(".m3u8")) {
            this.mIjkMediaPlayer.setOption(4, "delay-optimization", 0L);
        }

        this.mPlayerState = PlayerState.PREPARING;
        this.mIjkMediaPlayer.prepareAsync();
        this.startTimeMilli = System.currentTimeMillis();
        this.v = 0L;
        this.w = 0L;
        this.bufferingTotalTimes = 0L;
        this.bufferingTotalCount = 0L;
        this.n = true;
        this.k = false;
        this.l = false;
        this.m = false;
        HandlerThread var2 = new HandlerThread("PlayerHt");
        var2.start();
        this.prepareAsyncHandler = new PrepareAsyncHandler(var2.getLooper(), this);
    }

    /**
     * Sets the volume on this player.
     * This API is recommended for balancing the output of audio streams within an application.
     * Unless you are writing an application to control user settings, this API should be used in preference to AudioManager.setStreamVolume(int, int, int) which sets the volume of ALL streams of resize particular type.
     * Note that the passed volume values are raw scalars in range 0.0 to 1.0. UI controls should be scaled logarithmically.
     */
    public void setVolume(float leftVolume, float rightVolume) {
        this.mIjkMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    public void start() throws IllegalStateException {
        this.mIjkMediaPlayer.start();
        if (this.v > this.startTimeMilli) {
            this.w += System.currentTimeMillis() - this.v;
        }

    }

    public void pause() throws IllegalStateException {
        this.mIjkMediaPlayer.pause();
        this.mPlayerState = PlayerState.PAUSED;
        this.v = System.currentTimeMillis();
    }

    public void stop() throws IllegalStateException {
        if (!this.l) {
            this.mStreamParam.totalRecvBytes = this.mIjkMediaPlayer.getPktTotalSize();
            this.optimizeLiveStream(0, 0);
        }

        this.mIjkMediaPlayer.stop();
        com.dovar.pili.qos.b.b(this.mContext);
        if (this.prepareAsyncHandler != null) {
            this.prepareAsyncHandler.removeCallbacksAndMessages((Object) null);
            this.prepareAsyncHandler.destory();
        }

        this.n = true;
    }

    private void prepare() {
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            public void run() {
                try {
                    mIjkMediaPlayer.stop();
                    mIjkMediaPlayer.release();
                    optimizeLiveStream(mAVOptions);
                    if (uri_headers == null) {
                        mIjkMediaPlayer.setDataSource(video_url);
                    } else {
                        mIjkMediaPlayer.setDataSource(video_url, uri_headers);
                    }

                    if (mSurfaceHolder != null) {
                        mIjkMediaPlayer.setDisplay(PLMediaPlayer.this.mSurfaceHolder);
                    } else if (PLMediaPlayer.this.mSurface != null) {
                        mIjkMediaPlayer.setSurface(PLMediaPlayer.this.mSurface);
                    }

                    mIjkMediaPlayer.prepareAsync();
                } catch (IOException var2) {
                    var2.printStackTrace();
                    onError();
                } catch (Exception var3) {
                    var3.printStackTrace();
                    onError();
                }

            }
        });
    }

    private void onError() {
        if (this.mOnErrorListener != null) {
            this.mOnErrorListener.onError(this, -1);
        }

    }

    public void seekTo(long msec) throws IllegalStateException {
        this.mIjkMediaPlayer.seekTo(msec);
    }

    public void reset() {
        this.mIjkMediaPlayer.reset();
    }

    public int getVideoWidth() {
        return this.mIjkMediaPlayer.getVideoWidth();
    }

    public String getResolutionInline() {
        String var1 = null;
        if (this.mIjkMediaPlayer != null) {
            try {
                MediaInfo var2 = this.mIjkMediaPlayer.getMediaInfo();
                var1 = var2.mMeta.mVideoStream.getResolutionInline();
            } catch (Exception var3) {

            }
        }

        return var1;
    }

    public int getVideoHeight() {
        return this.mIjkMediaPlayer.getVideoHeight();
    }

    public long getVideoBitrate() {
        return this.mIjkMediaPlayer.getBitrateVideo();
    }

    public int getVideoFps() {
        return (int) this.mIjkMediaPlayer.getVideoOutputFramesPerSecond();
    }

    public boolean isPlaying() {
        return this.mIjkMediaPlayer.isPlaying();
    }

    public long getCurrentPosition() {
        return this.mIjkMediaPlayer.getCurrentPosition();
    }

    public long getDuration() {
        return this.mIjkMediaPlayer.getDuration();
    }

    public void setLooping(boolean var1) {
        this.mIjkMediaPlayer.setLooping(var1);
    }

    public boolean isLooping() {
        return this.mIjkMediaPlayer.isLooping();
    }

    public void setOnInfoListener(PLMediaPlayer.OnInfoListener var1) {
        this.mOnInfoListener = var1;
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

    public void setIjkOnErrorListener(PLMediaPlayer.OnErrorListener var1) {
        this.mOnErrorListener = var1;
    }

    public void setOnMediaDataListener(OnMediaDataListener mVar1) {
        this.mOnMediaDataListener = mVar1;
    }

    private void setupStreamParam() {
        if (this.mStreamParam != null && this.mIjkMediaPlayer != null) {
            this.mStreamParam.videoSourceFps = (int) this.mIjkMediaPlayer.getSourcFpsVideo();
            this.mStreamParam.dropVideoFrames = (int) this.mIjkMediaPlayer.getFramesDroppedVideo();
            this.mStreamParam.audioSourceFps = (int) this.mIjkMediaPlayer.getSourcFpsAudio();
            this.mStreamParam.audioDropFrames = (int) this.mIjkMediaPlayer.getFramesDroppedAudio();
            this.mStreamParam.videoRenderFps = (int) this.mIjkMediaPlayer.getVideoOutputFramesPerSecond();
            this.mStreamParam.audioRenderFps = (int) this.mIjkMediaPlayer.getRenderFpsAudio();
            this.mStreamParam.videoBufferTime = (int) this.mIjkMediaPlayer.getBufferTimeVideo();
            this.mStreamParam.audioBufferTime = (int) this.mIjkMediaPlayer.getBufferTimeAudio();
            this.mStreamParam.videoBitrate = this.mIjkMediaPlayer.getBitrateVideo();
            this.mStreamParam.audioBitrate = this.mIjkMediaPlayer.getBitrateAudio();
        }
    }

    public static class PrepareAsyncHandler extends Handler {
        private WeakReference<PLMediaPlayer> mWeakReference;

        public PrepareAsyncHandler(Looper var1, PLMediaPlayer var2) {
            super(var1);
            this.mWeakReference = new WeakReference(var2);
        }

        public void destory() {
            this.getLooper().quit();
            this.mWeakReference.clear();
        }

        public void handleMessage(Message var1) {
            PLMediaPlayer mp = mWeakReference.get();
            if (mp != null && mp.mStreamParam != null) {
                switch (var1.what) {
                    case 0:
                        mp.setupStreamParam();
                        StreamParam params = mp.mStreamParam;
                        params.endAt = System.currentTimeMillis();
                        Intent var4 = new Intent("pldroid-player-qos-filter");
                        var4.putExtra("pldroid-qos-msg-type", 193);
                        var4.putExtra("beginAt", params.beginAt);
                        var4.putExtra("endAt", params.endAt);
                        var4.putExtra("bufferingTimes", params.bufferingTimes);
                        var4.putExtra("videoSourceFps", params.videoSourceFps);
                        var4.putExtra("dropVideoFrames", params.dropVideoFrames);
                        var4.putExtra("audioSourceFps", params.audioSourceFps);
                        var4.putExtra("audioDropFrames", params.audioDropFrames);
                        var4.putExtra("videoRenderFps", params.videoRenderFps);
                        var4.putExtra("audioRenderFps", params.audioRenderFps);
                        var4.putExtra("videoBufferTime", params.videoBufferTime);
                        var4.putExtra("audioBufferTime", params.audioBufferTime);
                        var4.putExtra("videoBitrate", params.videoBitrate);
                        var4.putExtra("audioBitrate", params.audioBitrate);
                        if (params.p > 0L && params.beginAt > 0L) {
                            com.dovar.pili.qos.c.a().a(var4);
                        }

                        params.beginAt = System.currentTimeMillis();
                        mp.mStreamParam.reset();
                        this.sendMessageDelayed(this.obtainMessage(0), (long) com.dovar.pili.qos.c.a().b());
                    default:
                }
            } else {
                Log.w("PLMediaPlayer", "MuxerHandler.handleMessage: muxer is null");
            }
        }
    }

    public interface OnMediaDataListener {
        void a(byte[] var1, int var2, double var3, int var5, int var6, int var7, int var8);

        void a(byte[] var1, int var2, double var3, int var5, int var6, int var7, int var8, int var9, int var10);
    }

    public interface OnErrorListener {
        boolean onError(PLMediaPlayer mp, int errorCode);
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(PLMediaPlayer var1, int width, int height, int videoSar, int videoDen);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete(PLMediaPlayer mp);
    }

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(PLMediaPlayer mp, int percent);
    }

    public interface OnCompletionListener {
        void onCompletion(PLMediaPlayer mp);
    }

    public interface OnPreparedListener {
        void onPrepared(PLMediaPlayer mp, int preparedTime);
    }

    public interface OnInfoListener {
        boolean onInfo(PLMediaPlayer mp, int what, int extra);
    }
}
