package com.dovar.pili.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.dovar.pili.PLMediaPlayer;
import com.dovar.pili.common.CommonA;

/**
 * Created by heweizong on 2017/9/29.
 */

public class PLVideoTextureView extends IVideoView {
    private View videoView;
    private PLVideoTextureView.PlTextureView mPlTextureView;
    private int displayOrientation = 0;
    private SurfaceTexture mSurfaceTexture;

    public PLVideoTextureView(Context var1) {
        super(var1);
    }

    public PLVideoTextureView(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public PLVideoTextureView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    @TargetApi(21)
    public PLVideoTextureView(Context var1, AttributeSet var2, int var3, int var4) {
        super(var1, var2, var3, var4);
    }

    public TextureView getTextureView() {
        return this.mPlTextureView;
    }

    public void setMirror(boolean var1) {
        this.setScaleX(var1 ? -1.0F : 1.0F);
    }

    public boolean setDisplayOrientation(int var1) {
        if (var1 != 0 && var1 != 90 && var1 != 180 && var1 != 270) {
            return false;
        } else {
            var1 %= 360;
            if (this.displayOrientation != var1) {
                this.displayOrientation = var1;
                this.requestLayout();
            }

            return true;
        }
    }

    public int getDisplayOrientation() {
        return this.displayOrientation;
    }

    protected void onFinishInflate() {
        this.videoView = this.getChildAt(0);
        this.videoView.setPivotX(0.0F);
        this.videoView.setPivotY(0.0F);
        super.onFinishInflate();
    }

    protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
        if (this.videoView == null) {
            super.onLayout(var1, var2, var3, var4, var5);
        } else {
            int var6 = var4 - var2;
            int var7 = var5 - var3;
            switch (this.displayOrientation) {
                case 0:
                case 180:
                    this.videoView.layout(0, 0, var6, var7);
                    break;
                case 90:
                case 270:
                    this.videoView.layout(0, 0, var7, var6);
            }

        }
    }

    protected void onMeasure(int var1, int var2) {
        if (this.videoView == null) {
            super.onMeasure(var1, var2);
        } else {
            int var3 = 0;
            int var4 = 0;
            switch (this.displayOrientation) {
                case 0:
                case 180:
                    this.measureChild(this.videoView, var1, var2);
                    var3 = this.videoView.getMeasuredWidth();
                    var4 = this.videoView.getMeasuredHeight();
                    break;
                case 90:
                case 270:
                    this.measureChild(this.videoView, var2, var1);
                    var3 = this.videoView.getMeasuredHeight();
                    var4 = this.videoView.getMeasuredWidth();
            }

            this.setMeasuredDimension(var3, var4);
            switch (this.displayOrientation) {
                case 0:
                    this.videoView.setTranslationX(0.0F);
                    this.videoView.setTranslationY(0.0F);
                    break;
                case 90:
                    this.videoView.setTranslationX(0.0F);
                    this.videoView.setTranslationY((float) var4);
                    break;
                case 180:
                    this.videoView.setTranslationX((float) var3);
                    this.videoView.setTranslationY((float) var4);
                    break;
                case 270:
                    this.videoView.setTranslationX((float) var3);
                    this.videoView.setTranslationY(0.0F);
            }

            this.videoView.setRotation((float) (-this.displayOrientation));
        }
    }

    @Override
    protected void init(Context var1) {
        this.mPlTextureView = new PlTextureView(var1);
        super.init(var1);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.releaseSurfactexture();
    }

    @TargetApi(16)
    protected void init(PLMediaPlayer mp, Surface var2) {
        if (mp != null && !this.b && this.mSurfaceTexture != null && Build.VERSION.SDK_INT >= 16) {
            this.mPlTextureView.setSurfaceTexture(this.mSurfaceTexture);
        } else {
            super.init(mp, var2);
            this.b = false;
        }

    }

    public void releaseSurfactexture() {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
            mSurface = null;
        }

    }

    protected void init() {
        if (this.mSurfaceTexture == null && Build.VERSION.SDK_INT < 16) {
            super.init();
        }

    }

    protected IRenderView getRenderView() {
        return this.mPlTextureView;
    }

    public class PlTextureView extends TextureView implements IRenderView {
        private RenderCallback mRenderCallback;
        private int c = 0;
        private int d = 0;

        public PlTextureView(Context var2) {
            super(var2);
            setSurfaceTextureListener(new SurfaceTextureListener() {
                public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
                    if (mRenderCallback != null) {
                        mRenderCallback.a(new Surface(var1), var2, var3);
                    }

                    if (mSurfaceTexture == null) {
                        mSurfaceTexture = var1;
                    }

                }

                public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
                    if (mRenderCallback != null) {
                        mRenderCallback.b(new Surface(var1), var2, var3);
                    }

                }

                public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
                    if (mRenderCallback != null) {
                        mRenderCallback.a(new Surface(var1));
                    }

                    return false;
                }

                public void onSurfaceTextureUpdated(SurfaceTexture var1) {
                }
            });
        }

        protected void onMeasure(int var1, int var2) {
            CommonA.classB var3 = CommonA.a(getDisplayAspectRatio(), var1, var2, this.c, this.d);
            this.setMeasuredDimension(var3.a, var3.b);
        }

        public View getView() {
            return this;
        }

        public void resize(int var1, int var2) {
            this.c = var1;
            this.d = var2;
            this.requestLayout();
        }

        public void setRenderCallback(RenderCallback mVar1) {
            this.mRenderCallback = mVar1;
        }
    }
}
