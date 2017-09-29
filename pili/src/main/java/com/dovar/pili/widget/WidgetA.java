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

public abstract class WidgetA extends FrameLayout implements IMediaController.MediaPlayerControl {
    public static final int ASPECT_RATIO_ORIGIN = 0;
    public static final int ASPECT_RATIO_FIT_PARENT = 1;
    public static final int ASPECT_RATIO_PAVED_PARENT = 2;
    public static final int ASPECT_RATIO_16_9 = 3;
    public static final int ASPECT_RATIO_4_3 = 4;
    private int c = 0;
    private int d = 0;
    private long e = 0L;
    private int f = 0;
    protected Surface a;
    private Uri g;
    private AVOptions h;
    private int i = 0;
    private int j = 0;
    private View k;
    private InterfaceA l;
    private PLMediaPlayer m;
    private IMediaController n;
    private View o = null;
    private int p = 1;
    private boolean q = false;
    private boolean r = true;
    private int s = 1;
    private float t = -1.0F;
    private float u = -1.0F;
    private boolean v = false;
    protected boolean b = true;
    private PLMediaPlayer.OnCompletionListener w;
    private PLMediaPlayer.OnPreparedListener x;
    private PLMediaPlayer.OnErrorListener y;
    private PLMediaPlayer.OnInfoListener z;
    private PLMediaPlayer.OnBufferingUpdateListener A;
    private PLMediaPlayer.OnSeekCompleteListener B;
    private PLMediaPlayer.OnVideoSizeChangedListener C;
    private PLMediaPlayer.a D;
    private PLMediaPlayer.OnPreparedListener E = new PLMediaPlayer.OnPreparedListener() {
        public void onPrepared(PLMediaPlayer var1, int var2) {
            WidgetA.this.i = 2;
            WidgetA.this.c = var1.getVideoWidth();
            WidgetA.this.d = var1.getVideoHeight();
            if(WidgetA.this.x != null) {
                WidgetA.this.x.onPrepared(var1, var2);
            }

            if(WidgetA.this.n != null) {
                WidgetA.this.n.setEnabled(true);
            }

            if(WidgetA.this.e != 0L) {
                WidgetA.this.seekTo(WidgetA.this.e);
            }

            if(WidgetA.this.j == 3) {
                WidgetA.this.start();
                if(WidgetA.this.n != null) {
                    WidgetA.this.n.show();
                }
            }

        }
    };
    private PLMediaPlayer.OnVideoSizeChangedListener F = new PLMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(PLMediaPlayer var1, int var2, int var3, int var4, int var5) {
            if(WidgetA.this.C != null) {
                WidgetA.this.C.onVideoSizeChanged(var1, var2, var3, var4, var5);
            }

            WidgetA.this.c = var1.getVideoWidth();
            WidgetA.this.d = var1.getVideoHeight();
            if(WidgetA.this.c != 0 && WidgetA.this.d != 0) {
                WidgetA.this.l.a(WidgetA.this.c, WidgetA.this.d);
                WidgetA.this.requestLayout();
            }

        }
    };
    private PLMediaPlayer.OnSeekCompleteListener G = new PLMediaPlayer.OnSeekCompleteListener() {
        public void onSeekComplete(PLMediaPlayer var1) {
            if(WidgetA.this.B != null) {
                WidgetA.this.B.onSeekComplete(var1);
            }

        }
    };
    private PLMediaPlayer.OnInfoListener H = new PLMediaPlayer.OnInfoListener() {
        public boolean onInfo(PLMediaPlayer var1, int var2, int var3) {
            if(WidgetA.this.z != null) {
                WidgetA.this.z.onInfo(var1, var2, var3);
            }

            if(WidgetA.this.k != null) {
                if(var2 == 701 && !var1.a()) {
                    WidgetA.this.k.setVisibility(VISIBLE);
                } else if(var2 == 702 || var2 == 10002 || var2 == 3) {
                    WidgetA.this.k.setVisibility(GONE);
                }
            }

            if(var2 == 3 && WidgetA.this.o != null) {
                WidgetA.this.o.setVisibility(GONE);
            }

            return true;
        }
    };
    private PLMediaPlayer.OnBufferingUpdateListener I = new PLMediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(PLMediaPlayer var1, int var2) {
            WidgetA.this.f = var2;
            if(WidgetA.this.A != null) {
                WidgetA.this.A.onBufferingUpdate(var1, var2);
            }

        }
    };
    private PLMediaPlayer.OnCompletionListener J = new PLMediaPlayer.OnCompletionListener() {
        public void onCompletion(PLMediaPlayer var1) {
            if(WidgetA.this.n != null) {
                WidgetA.this.n.hide();
            }

            if(WidgetA.this.k != null) {
                WidgetA.this.k.setVisibility(GONE);
            }

            if(WidgetA.this.w != null) {
                WidgetA.this.w.onCompletion(var1);
            }

            WidgetA.this.i = 5;
            WidgetA.this.j = 5;
        }
    };
    private PLMediaPlayer.OnErrorListener K = new PLMediaPlayer.OnErrorListener() {
        public boolean onError(PLMediaPlayer var1, int var2) {
            WidgetA.this.i = -1;
            WidgetA.this.j = -1;
            if(WidgetA.this.n != null) {
                WidgetA.this.n.hide();
            }

            if(WidgetA.this.k != null) {
                WidgetA.this.k.setVisibility(GONE);
            }

            return WidgetA.this.y != null?WidgetA.this.y.onError(var1, var2):true;
        }
    };
    private WidgetA.InterfaceA.a L = new WidgetA.InterfaceA.a() {
        public void a(Surface var1, int var2, int var3) {
            if(WidgetA.this.a == null) {
                WidgetA.this.a = var1;
            }

            if(WidgetA.this.m != null) {
                WidgetA.this.a(WidgetA.this.m, var1);
            } else {
                WidgetA.this.c();
            }

        }

        public void b(Surface var1, int var2, int var3) {
            boolean var4 = WidgetA.this.j == 3;
            boolean var5 = WidgetA.this.c == var2 && WidgetA.this.d == var3;
            if(WidgetA.this.m != null && var4 && var5) {
                if(WidgetA.this.e != 0L) {
                    WidgetA.this.seekTo(WidgetA.this.e);
                }

                WidgetA.this.start();
            }

        }

        public void a(Surface var1) {
            if(WidgetA.this.n != null) {
                WidgetA.this.n.hide();
            }

            WidgetA.this.a();
        }
    };

    protected abstract InterfaceA getRenderView();

    public WidgetA(Context var1) {
        super(var1);
        this.a(var1);
    }

    public WidgetA(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.a(var1);
    }

    public WidgetA(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.a(var1);
    }

    @TargetApi(21)
    public WidgetA(Context var1, AttributeSet var2, int var3, int var4) {
        super(var1, var2, var3, var4);
        this.a(var1);
    }

    public void setDebugLoggingEnabled(boolean var1) {
        this.v = var1;
        if(this.m != null) {
            this.m.setDebugLoggingEnabled(var1);
        }

    }

    protected void a(Context var1) {
        this.l = this.getRenderView();
        this.l.setRenderCallback(this.L);
        FrameLayout.LayoutParams var2 = new LayoutParams(-1, -1, 17);
        this.l.getView().setLayoutParams(var2);
        this.addView(this.l.getView());
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.requestFocus();
        this.i = 0;
        this.j = 0;
    }

    public void setDisplayAspectRatio(int var1) {
        this.p = var1;
        View var2 = this.getChildAt(0);
        if(var2 != null) {
            var2.requestLayout();
        }

    }

    public int getDisplayAspectRatio() {
        return this.p;
    }

    public void stopPlayback() {
        this.a(true);
    }

    protected void a() {
        if(this.m != null) {
            this.m.setDisplay((SurfaceHolder)null);
        }

    }

    public void setAVOptions(AVOptions var1) {
        this.h = var1;
    }

    public void setVideoPath(String var1) {
        if(var1 != null) {
            this.g = Uri.parse(var1);
            this.setVideoURI(this.g);
        } else {
            this.g = null;
        }

    }

    public void setVideoPath(String var1, Map<String, String> var2) {
        if(var1 != null) {
            this.setVideoURI(Uri.parse(var1), var2);
        } else {
            this.g = null;
        }

    }

    public void setVideoURI(Uri var1) {
        this.setVideoURI(var1, (Map)null);
    }

    public void setVideoURI(Uri var1, Map<String, String> var2) {
        this.g = var1;
        if(var1 != null) {
            this.e = 0L;
            this.b = true;
            this.a(var2);
            this.requestLayout();
            this.invalidate();
        }

    }

    public void setBufferingIndicator(View var1) {
        if(this.k != null) {
            this.k.setVisibility(GONE);
        }

        this.k = var1;
    }

    public void setCoverView(View var1) {
        this.o = var1;
    }

    public void setMediaController(IMediaController var1) {
        if(this.n != null) {
            this.n.hide();
        }

        this.n = var1;
        this.b();
    }

    public void setVolume(float var1, float var2) {
        this.t = var1;
        this.u = var2;
        if(this.m != null) {
            this.m.setVolume(var1, var2);
        }

    }

    public void setWakeMode(Context var1, int var2) {
        this.s = var2;
        if(this.m != null) {
            this.m.setWakeMode(var1.getApplicationContext(), var2);
        }

    }

    public void setScreenOnWhilePlaying(boolean var1) {
        this.r = var1;
        if(this.m != null) {
            this.m.setScreenOnWhilePlaying(var1);
        }

    }

    public void setLooping(boolean var1) {
        this.q = var1;
        if(this.m != null) {
            this.m.setLooping(var1);
        }

    }

    public boolean isLooping() {
        return this.q;
    }

    protected void b() {
        if(this.m != null && this.n != null) {
            this.n.setMediaPlayer(this);
            Object var1 = this.getParent() instanceof View?(View)this.getParent():this;
            this.n.setAnchorView((View)var1);
            this.n.setEnabled(this.e());
        }

    }

    protected void c() {
        this.a((Map)null);
    }

    protected void a(Map<String, String> var1) {
        if(this.g != null && this.a != null) {
            this.f = 0;
            this.a(false);
            AudioManager var2 = (AudioManager)this.getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            if(var2 != null) {
                var2.requestAudioFocus((AudioManager.OnAudioFocusChangeListener)null, 3, 1);
            }

            try {
                this.m = new PLMediaPlayer(this.getContext(), this.h);
            } catch (UnsatisfiedLinkError var7) {
                var7.printStackTrace();
                return;
            }

            this.m.setDebugLoggingEnabled(this.v);
            this.m.setLooping(this.q);
            this.m.setScreenOnWhilePlaying(this.r);
            if(this.s != -1) {
                this.m.setWakeMode(this.getContext().getApplicationContext(), this.s);
            }

            if(this.t != -1.0F && this.u != -1.0F) {
                this.m.setVolume(this.t, this.u);
            }

            this.m.setOnPreparedListener(this.E);
            this.m.setOnVideoSizeChangedListener(this.F);
            this.m.setOnCompletionListener(this.J);
            this.m.setOnErrorListener(this.K);
            this.m.setOnInfoListener(this.H);
            this.m.setOnBufferingUpdateListener(this.I);
            this.m.setOnSeekCompleteListener(this.G);
            this.m.setOnMediaDataListener(this.D);

            try {
                if(var1 != null) {
                    this.m.setDataSource(this.getContext(), this.g, var1);
                } else {
                    this.m.setDataSource(this.g.toString());
                }

                this.a(this.m, this.a);
                this.m.prepareAsync();
                this.b();
                this.i = 1;
                return;
            } catch (IllegalArgumentException var4) {
                var4.printStackTrace();
            } catch (IllegalStateException var5) {
                var5.printStackTrace();
            } catch (IOException var6) {
                var6.printStackTrace();
            }

            if(this.y != null) {
                this.y.onError(this.m, -1);
            }

            this.i = -1;
            this.j = -1;
        }
    }

    protected void a(boolean var1) {
        if(this.m != null) {
            if(var1) {
                this.j = 0;
                this.g = null;
            }

            this.m.stop();
            this.m.release();
            this.m = null;
            this.i = 0;
            AudioManager var2 = (AudioManager)this.getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            var2.abandonAudioFocus((AudioManager.OnAudioFocusChangeListener)null);
        }

    }

    public void start() {
        if(this.i == 5) {
            this.setVideoURI(this.g);
            this.j = 3;
        } else {
            if(this.e()) {
                this.m.start();
                this.i = 3;
            }

            this.j = 3;
        }
    }

    public void pause() {
        if(this.e() && this.m.isPlaying()) {
            this.m.pause();
            this.i = 4;
        }

        this.j = 4;
    }

    public long getDuration() {
        return this.e()?this.m.getDuration():-1L;
    }

    public long getCurrentPosition() {
        return this.e()?this.m.getCurrentPosition():0L;
    }

    public void seekTo(long var1) {
        if(this.e()) {
            this.m.seekTo(var1);
            this.e = 0L;
        } else {
            this.e = var1;
        }

    }

    public boolean isPlaying() {
        return this.e() && this.m.isPlaying();
    }

    public int getBufferPercentage() {
        return this.f;
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
        return this.m != null?this.m.getPlayerState():PlayerState.IDLE;
    }

    public HashMap<String, String> getMetadata() {
        return this.m != null?this.m.getMetadata():null;
    }

    public long getVideoBitrate() {
        return this.m != null?this.m.getVideoBitrate():0L;
    }

    public int getVideoFps() {
        return this.m != null?this.m.getVideoFps():0;
    }

    public String getResolutionInline() {
        return this.m != null?this.m.getResolutionInline():null;
    }

    private boolean e() {
        return this.m != null && this.i != -1 && this.i != 0 && this.i != 1;
    }

    public void setOnInfoListener(PLMediaPlayer.OnInfoListener var1) {
        this.z = var1;
    }

    public void setOnErrorListener(PLMediaPlayer.OnErrorListener var1) {
        this.y = var1;
    }

    public void setOnPreparedListener(PLMediaPlayer.OnPreparedListener var1) {
        this.x = var1;
    }

    public void setOnCompletionListener(PLMediaPlayer.OnCompletionListener var1) {
        this.w = var1;
    }

    public void setOnBufferingUpdateListener(PLMediaPlayer.OnBufferingUpdateListener var1) {
        this.A = var1;
    }

    public void setOnSeekCompleteListener(PLMediaPlayer.OnSeekCompleteListener var1) {
        this.B = var1;
    }

    public void setOnVideoSizeChangedListener(PLMediaPlayer.OnVideoSizeChangedListener var1) {
        this.C = var1;
    }

    public void setOnMediaDataListener(PLMediaPlayer.a var1) {
        this.D = var1;
        if(this.m != null) {
            this.m.setOnMediaDataListener(this.D);
        }

    }

    public boolean onTouchEvent(MotionEvent var1) {
        if(this.e() && this.n != null) {
            this.d();
        }

        return false;
    }

    public boolean onTrackballEvent(MotionEvent var1) {
        if(this.e() && this.n != null) {
            this.d();
        }

        return false;
    }

    public boolean onKeyDown(int var1, KeyEvent var2) {
        boolean var3 = var1 != 4 && var1 != 24 && var1 != 25 && var1 != 164 && var1 != 82 && var1 != 5 && var1 != 6;
        if(this.e() && var3 && this.n != null) {
            if(var1 == 79 || var1 == 85) {
                if(this.m.isPlaying()) {
                    this.pause();
                    this.n.show();
                } else {
                    this.start();
                    this.n.hide();
                }

                return true;
            }

            if(var1 == 126) {
                if(!this.m.isPlaying()) {
                    this.start();
                    this.n.hide();
                }

                return true;
            }

            if(var1 == 86 || var1 == 127) {
                if(this.m.isPlaying()) {
                    this.pause();
                    this.n.show();
                }

                return true;
            }

            this.d();
        }

        return super.onKeyDown(var1, var2);
    }

    protected void d() {
        if(this.n.isShowing()) {
            this.n.hide();
        } else {
            this.n.show();
        }

    }

    protected void a(PLMediaPlayer var1, Surface var2) {
        if(var1 != null && var2 != null) {
            var1.setSurface(var2);
        }
    }

    protected interface InterfaceA {
        View getView();

        void a(int var1, int var2);

        void setRenderCallback(WidgetA.InterfaceA.a var1);

        public interface a {
            void a(Surface var1, int var2, int var3);

            void b(Surface var1, int var2, int var3);

            void a(Surface var1);
        }
    }
}
