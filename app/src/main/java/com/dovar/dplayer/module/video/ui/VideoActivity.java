package com.dovar.dplayer.module.video.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.dovar.dplayer.R;
import com.dovar.dplayer.common.DMediaController;
import com.dovar.dplayer.common.base.StatusBarTintActivity;

public class VideoActivity extends StatusBarTintActivity implements DMediaController.ControlListener {

    private DMediaController mDMediaController;
    private static final String Key_url = "key_url";
    private String video_url;
    private ImageView iv_start;

    public static void jump(Context mContext, String url) {
        Intent mIntent = new Intent(mContext, VideoActivity.class);
        if (!TextUtils.isEmpty(url)) {
            mIntent.putExtra(Key_url, url);
        }
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        parseIntent();
        initUI();
        initData();
        setupVideoPlayer();
    }

    @Override
    protected void onDestroy() {
        if (mDMediaController != null) {
            mDMediaController.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {

    }

    private void parseIntent() {
        Intent mIntent = getIntent();
        if (mIntent != null) {
            video_url = mIntent.getStringExtra(Key_url);
        }
    }

    private void setupVideoPlayer() {
        mDMediaController = new DMediaController(this);
        mDMediaController.setControlListener(this);
        mDMediaController.setAnchorView(findView(R.id.video_container));
        mDMediaController.startPlay(video_url);

//        TextureView mTextureView=new TextureView(this);
//        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//            @Override
//            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //TextureView成功绘制之后才会有回调，不可见时不会回调
//                ToastUtil.show("available");
//            }
//
//            @Override
//            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//                ToastUtil.show("available");
//            }
//
//            @Override
//            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//                ToastUtil.show("available");
//                return false;
//            }
//
//            @Override
//            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//                ToastUtil.show("available");
//            }
//        });
//
//        FrameLayout container= (FrameLayout) findViewById(R.id.video_frame);
//        container.addView(mTextureView, ViewGroup.LayoutParams.MATCH_PARENT,-1);
    }

    @Override
    public void onPauseOrPlay(boolean isPause) {
        if (iv_start != null) {
            if (isPause) {
                iv_start.setVisibility(View.VISIBLE);
            } else {
                iv_start.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initPortControllerView(View v) {
        View ll_space = v.findViewById(R.id.ll_space);
        View ll_playback = v.findViewById(R.id.ll_playback);
        if (mDMediaController.isLive()) {
            ll_space.setVisibility(View.VISIBLE);
            ll_playback.setVisibility(View.GONE);
        } else {
            ll_space.setVisibility(View.GONE);
            ll_playback.setVisibility(View.VISIBLE);
        }

        iv_start = (ImageView) v.findViewById(R.id.iv_start);
        if (iv_start != null) {
            iv_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDMediaController.start();
                    iv_start.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void initLandControllerView(View v) {
        View ll_space = v.findViewById(R.id.ll_space);
        View ll_playback = v.findViewById(R.id.ll_playback);
        if (mDMediaController.isLive()) {
            ll_space.setVisibility(View.VISIBLE);
            ll_playback.setVisibility(View.GONE);
        } else {
            ll_space.setVisibility(View.GONE);
            ll_playback.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View makeControllerView() {
        return View.inflate(mContext, R.layout.media_controll_view_tv, null);
    }

    @Override
    public View makeLandControllerView() {
        return View.inflate(mContext, R.layout.media_controll_view_big_tv, null);
    }

}
