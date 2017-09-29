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

public class PLVideoTextureView extends WidgetA{
    private View c;
    private PLVideoTextureView.ClassInterfaceA d;
    private int e = 0;
    private SurfaceTexture f;

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
        return this.d;
    }

    public void setMirror(boolean var1) {
        this.setScaleX(var1?-1.0F:1.0F);
    }

    public boolean setDisplayOrientation(int var1) {
        if(var1 != 0 && var1 != 90 && var1 != 180 && var1 != 270) {
            return false;
        } else {
            var1 %= 360;
            if(this.e != var1) {
                this.e = var1;
                this.requestLayout();
            }

            return true;
        }
    }

    public int getDisplayOrientation() {
        return this.e;
    }

    protected void onFinishInflate() {
        this.c = this.getChildAt(0);
        this.c.setPivotX(0.0F);
        this.c.setPivotY(0.0F);
        super.onFinishInflate();
    }

    protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
        if(this.c == null) {
            super.onLayout(var1, var2, var3, var4, var5);
        } else {
            int var6 = var4 - var2;
            int var7 = var5 - var3;
            switch(this.e) {
                case 0:
                case 180:
                    this.c.layout(0, 0, var6, var7);
                    break;
                case 90:
                case 270:
                    this.c.layout(0, 0, var7, var6);
            }

        }
    }

    protected void onMeasure(int var1, int var2) {
        if(this.c == null) {
            super.onMeasure(var1, var2);
        } else {
            int var3 = 0;
            int var4 = 0;
            switch(this.e) {
                case 0:
                case 180:
                    this.measureChild(this.c, var1, var2);
                    var3 = this.c.getMeasuredWidth();
                    var4 = this.c.getMeasuredHeight();
                    break;
                case 90:
                case 270:
                    this.measureChild(this.c, var2, var1);
                    var3 = this.c.getMeasuredHeight();
                    var4 = this.c.getMeasuredWidth();
            }

            this.setMeasuredDimension(var3, var4);
            switch(this.e) {
                case 0:
                    this.c.setTranslationX(0.0F);
                    this.c.setTranslationY(0.0F);
                    break;
                case 90:
                    this.c.setTranslationX(0.0F);
                    this.c.setTranslationY((float)var4);
                    break;
                case 180:
                    this.c.setTranslationX((float)var3);
                    this.c.setTranslationY((float)var4);
                    break;
                case 270:
                    this.c.setTranslationX((float)var3);
                    this.c.setTranslationY(0.0F);
            }

            this.c.setRotation((float)(-this.e));
        }
    }

    protected void a(Context var1) {
        this.d = new ClassInterfaceA(var1);
        super.a(var1);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.releaseSurfactexture();
    }

    @TargetApi(16)
    protected void a(PLMediaPlayer var1, Surface var2) {
        if(var1 != null && !this.b && this.f != null && Build.VERSION.SDK_INT >= 16) {
            this.d.setSurfaceTexture(this.f);
        } else {
            super.a(var1, var2);
            this.b = false;
        }

    }

    public void releaseSurfactexture() {
        if(this.f != null) {
            this.f.release();
            this.f = null;
            this.a = null;
        }

    }

    protected void a() {
        if(this.f == null && Build.VERSION.SDK_INT < 16) {
            super.a();
        }

    }

    protected InterfaceA getRenderView() {
        return this.d;
    }

    protected class ClassInterfaceA extends TextureView implements InterfaceA {
        private InterfaceA.a b;
        private int c = 0;
        private int d = 0;
        private SurfaceTextureListener e = new SurfaceTextureListener() {
            public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
                if(ClassInterfaceA.this.b != null) {
                    ClassInterfaceA.this.b.a(new Surface(var1), var2, var3);
                }

                if(PLVideoTextureView.this.f == null) {
                    PLVideoTextureView.this.f = var1;
                }

            }

            public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
                if(ClassInterfaceA.this.b != null) {
                    ClassInterfaceA.this.b.b(new Surface(var1), var2, var3);
                }

            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
                if(ClassInterfaceA.this.b != null) {
                    ClassInterfaceA.this.b.a(new Surface(var1));
                }

                return false;
            }

            public void onSurfaceTextureUpdated(SurfaceTexture var1) {
            }
        };

        public ClassInterfaceA(Context var2) {
            super(var2);
            this.setSurfaceTextureListener(this.e);
        }

        protected void onMeasure(int var1, int var2) {
            CommonA.classB var3 = CommonA.a(PLVideoTextureView.this.getDisplayAspectRatio(), var1, var2, this.c, this.d);
            this.setMeasuredDimension(var3.a, var3.b);
        }

        public View getView() {
            return this;
        }

        public void a(int var1, int var2) {
            this.c = var1;
            this.d = var2;
            this.requestLayout();
        }

        public void setRenderCallback(InterfaceA.a var1) {
            this.b = var1;
        }
    }
}
