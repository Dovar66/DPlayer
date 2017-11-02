package com.dovar.dplayer.module.video.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.dovar.dplayer.R;
import com.dovar.dplayer.common.mediaControl.DMediaController;
import com.dovar.dplayer.common.base.StatusBarTintActivity;
import com.dovar.dplayer.common.utils.LogUtil;
import com.dovar.pili.PLMediaPlayer;

public class VideoActivity extends StatusBarTintActivity implements DMediaController.ControlListener {

    private DMediaController mDMediaController;
    private static final String Key_url = "key_url";
    private String video_url;

    public static void jump(Context mContext, String url) {
        Intent mIntent = new Intent(mContext, VideoActivity.class);
        if (!TextUtils.isEmpty(url)) {
            mIntent.putExtra(Key_url, url);
        }
        mContext.startActivity(mIntent);
    }

    public static void jump(Context mContext, String url,Bundle options) {
        Intent mIntent = new Intent(mContext, VideoActivity.class);
        if (!TextUtils.isEmpty(url)) {
            mIntent.putExtra(Key_url, url);
        }
        mContext.startActivity(mIntent,options);
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
    protected void onPause() {
        super.onPause();
        if (mDMediaController!=null){
            mDMediaController.pause();
        }
    }

    @Override
    protected void onDestroy() {
        if (mDMediaController != null) {
            mDMediaController.destroy(); //销毁视频播放相关
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
        mDMediaController.startPlay(video_url); //开始播放
        initMediaPlayerListener();//初始化播放的监听
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

    //初始化播放事件监听
    private  void initMediaPlayerListener(){
        mDMediaController.setOnCompletionListener(new DMediaController.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer plMediaPlayer) {
                //播放完变回竖屏
                mDMediaController.changeToFullScreen(false);
            }
        });

        mDMediaController.setOnInfoListener(new DMediaController.OnInfoListener() {
            @Override
            public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
                LogUtil.d("tag","播放信息="+what);
                if (what==PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                    mDMediaController.show();
                }
                return false;
            }
        });
    }

    @Override
    public void onPauseOrPlay(boolean isPause) {

    }

    /**首先在dmediacontroller中的setanchorview中会调用下面的makecontrollerview方法生成了控制view，这个控制view由我们来定义，然后还会调用这里的
     * initcontrollerview方法，用来对控制view进行初始化操作。v参数就是makecontrollerview中返回的view对象*/
    @Override
    public void initPortControllerView(View v) {

    }

    @Override
    public void initLandControllerView(View v) {
    }

    @Override
    public View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflate.inflate(R.layout.layout_media_controller, null);
    }

    @Override
    public View makeLandControllerView() {
        return null;
    }

    @Override
    public void onBackPressed() {
        if (getRequestedOrientation()== ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE||getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE){
            mDMediaController.changeToFullScreen(false);
        } else{
            super.onBackPressed();
        }
    }
}
