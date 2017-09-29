package com.dovar.pili.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.dovar.pili.PLMediaPlayer;

/**
 * Created by heweizong on 2017/9/29.
 */

public class PLVideoView extends WidgetA {
    private PLVideoView.InterfaceA c;

    public PLVideoView(Context var1) {
        super(var1);
    }

    public PLVideoView(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public PLVideoView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    @TargetApi(21)
    public PLVideoView(Context var1, AttributeSet var2, int var3, int var4) {
        super(var1, var2, var3, var4);
    }

    public SurfaceView getSurfaceView() {
        return this.c;
    }

    protected void a(Context var1) {
        this.c = new PLVideoView.InterfaceA(var1);
        super.a(var1);
    }

    protected void a(PLMediaPlayer var1, Surface var2) {
        super.a(var1, var2);
        if (this.isPlaying() && !var1.a()) {
            var1.seekTo(var1.getCurrentPosition());
        }

    }

    protected InterfaceA getRenderView() {
        return this.c;
    }

    private class InterfaceA extends SurfaceView implements WidgetA.InterfaceA {
        private WidgetA.InterfaceA.a b;
        private int c = 0;
        private int d = 0;
        private SurfaceHolder.Callback e = new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder var1) {
                if (PLVideoView.InterfaceA.this.b != null) {
                    PLVideoView.InterfaceA.this.b.a(var1.getSurface(), 0, 0);
                }

            }

            public void surfaceChanged(SurfaceHolder var1, int var2, int var3, int var4) {
                if (PLVideoView.InterfaceA.this.b != null) {
                    PLVideoView.InterfaceA.this.b.b(var1.getSurface(), var3, var4);
                }

            }

            public void surfaceDestroyed(SurfaceHolder var1) {
                if (PLVideoView.InterfaceA.this.b != null) {
                    PLVideoView.InterfaceA.this.b.a(var1.getSurface());
                    PLVideoView.this.a = null;
                }

            }
        };

        public InterfaceA(Context var2) {
            super(var2);
            this.getHolder().addCallback(this.e);
        }

        protected void onMeasure(int var1, int var2) {
            com.dovar.pili.common.CommonA.classB var3 = com.dovar.pili.common.CommonA.a(PLVideoView.this.getDisplayAspectRatio(), var1, var2, this.c, this.d);
            this.setMeasuredDimension(var3.a, var3.b);
        }

        public View getView() {
            return this;
        }

        public void a(int var1, int var2) {
            this.c = var1;
            this.d = var2;
            this.getHolder().setFixedSize(var1, var2);
            this.requestLayout();
        }

        public void setRenderCallback(WidgetA.InterfaceA.a var1) {
            this.b = var1;
        }
    }
}
