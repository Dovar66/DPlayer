package com.dovar.dplayer.module.video.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.VideoListBean;
import com.dovar.dplayer.common.MyApplication;
import com.dovar.dplayer.common.adapter.RCommonAdapter;
import com.dovar.dplayer.common.adapter.RCommonViewHolder;
import com.dovar.dplayer.common.base.StatusBarTintActivity;
import com.dovar.dplayer.common.customview.DisableRecyclerView;
import com.dovar.dplayer.common.mediaControl.DMediaController;
import com.dovar.dplayer.common.utils.LogUtil;
import com.dovar.dplayer.common.utils.TimeUtils;
import com.dovar.dplayer.module.video.contract.VideoListContract;
import com.dovar.dplayer.module.video.presenter.VideoListPresenter;
import com.dovar.pili.PLMediaPlayer;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends StatusBarTintActivity implements VideoListContract.IView<VideoListBean>, DMediaController.ControlListener, View.OnClickListener {

    private DMediaController mDMediaController;
    private int mCurrentBuffer;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    /**
     * Prevent stopPlaybackImmediately be called too many times
     */
    private boolean mCanTriggerStop = true;

    private FrameLayout mVideoFloatContainer;
    private View mVideoPlayerBg;
    private ImageView mVideoCoverMask;
    private View mVideoPlayerView;
    private View mVideoLoadingView;
    private ProgressBar mVideoProgressBar;
    private View mCurrentPlayArea; //当前播放区域
    private int mCurrentActiveVideoItem = -1;

    /**
     * Stop video have two scenes
     * 1.click to stop current video and start a new video
     * 2.when video item is dismiss or ViewPager changed ? tab changed ? ...
     */
    private boolean mIsClickToStop;

    private float mOriginalHeight;

    private float mMoveDeltaY;

    private VideoListPresenter mPresenter;
    private DisableRecyclerView mVideoList;
    private LinearLayoutManager mLayoutManager;
    private RCommonAdapter<VideoListBean.IssueListBean.ItemListBean> mAdapter;
    private RefreshLayout refreshLayout;

    public static void jump(Context mContext) {
        Intent mIntent = new Intent(mContext, VideoListActivity.class);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        parseIntent();
        initUI();
        initData();
        setupVideoPlayer();
    }

    //配置播放相关
    private void setupVideoPlayer() {
        mDMediaController = new DMediaController(this);
        mDMediaController.setControlListener(this);
        mDMediaController.setIsExactHeight(true);//设置竖屏时播放器高为一个精确值
        mDMediaController.setAnchorView(mVideoFloatContainer.findViewById(R.id.video_container));
        initMediaPlayerListener();//初始化播放的监听
    }

    //初始化播放事件监听
    private void initMediaPlayerListener() {

        mDMediaController.setOnPreparedListener(new DMediaController.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer plMediaPlayer, int preparedTime) {
                LogUtil.d("tag", "准备播放");
                mVideoFloatContainer.setVisibility(View.VISIBLE);
                mVideoLoadingView.setVisibility(View.VISIBLE);
                //for cover the pre Video frame
                mVideoCoverMask.setVisibility(View.VISIBLE);
            }
        });

        mDMediaController.setOnErrorListener(new DMediaController.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer mp, int errorCode) {
                if (mCurrentPlayArea != null) {
                    mCurrentPlayArea.setClickable(true);
                    mCurrentPlayArea.setVisibility(View.VISIBLE);
                }
                mVideoFloatContainer.setVisibility(View.INVISIBLE);
                mVideoProgressBar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mProgressRunnable);
                return false;
            }
        });

        mDMediaController.setOnCompletionListener(new DMediaController.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer plMediaPlayer) {
                LogUtil.d("tag","完成播放");
                if (mCurrentPlayArea != null) {
                    mCurrentPlayArea.setClickable(true);
                }

                mVideoFloatContainer.setVisibility(View.INVISIBLE);//隐藏播放窗口
                mCurrentPlayArea.setVisibility(View.VISIBLE);
                mCurrentPlayArea.findViewById(R.id.img_start_play).setVisibility(View.VISIBLE);
                //播放item的view重新回到屏幕顶部
                ViewCompat.setTranslationY(mVideoFloatContainer,0);
                //stop update progress
                mVideoProgressBar.setVisibility(View.GONE); //进度条消失
                mHandler.removeCallbacks(mProgressRunnable);
                //播放完变回竖屏
                mDMediaController.changeToFullScreen(false);
            }
        });

        mDMediaController.setOnInfoListener(new DMediaController.OnInfoListener() {
            @Override
            public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
                LogUtil.d("tag", "播放信息=" + what);
                if (what == PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    //start update progress
                    mVideoProgressBar.setVisibility(View.VISIBLE);
                    mHandler.post(mProgressRunnable);

                    mVideoLoadingView.setVisibility(View.GONE);//正在加载进度条消失
                    mVideoCoverMask.setVisibility(View.GONE);//视频封面消失
                    mVideoPlayerBg.setVisibility(View.VISIBLE);//显示视频背景
                    mDMediaController.show();
                }else if (what == PLMediaPlayer.MEDIA_INFO_BUFFERING_START) { //开始缓冲
                    mVideoLoadingView.setVisibility(View.VISIBLE);
                } else if (what == PLMediaPlayer.MEDIA_INFO_BUFFERING_END) { //缓冲完毕
                    mVideoLoadingView.setVisibility(View.GONE);
                }else if (what == PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START){
                    mVideoPlayerView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        mDMediaController.setOnBufferingUpdateListener(new DMediaController.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int percent) {
                mCurrentBuffer = percent; //用来更新缓冲进度条
            }
        });


    }

    @Override
    protected void initUI() {
        mVideoFloatContainer = (FrameLayout) findViewById(R.id.layout_float_video_container);
        mVideoPlayerBg = findViewById(R.id.video_player_bg);//背景
        mVideoCoverMask = (ImageView) findViewById(R.id.video_player_mask);//视频封面
        mVideoPlayerView =mVideoFloatContainer.findViewById(R.id.video_container);//播放view
        mVideoLoadingView = findViewById(R.id.video_progress_loading);//加载中view
        mVideoProgressBar = (ProgressBar) findViewById(R.id.video_progress_bar);//播放进度条
        //下拉刷新控件
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getVideo();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPresenter.getMoreVideo();
            }
        });

        mVideoList = findView(R.id.recyclerview);
        mAdapter = new RCommonAdapter<VideoListBean.IssueListBean.ItemListBean>(this, R.layout.item_list_video) {
            @Override
            public void convert(RCommonViewHolder vh, int position) {
                VideoListBean.IssueListBean.ItemListBean bean = getItem(position);
                bean.getData().setId(position);//保存被点击itemview的pos
                if (bean == null) return;

                vh.setImageUrl(R.id.img_cover, bean.getData().getCover().getDetail());
                vh.setText(R.id.nickName, bean.getData().getCategory());
                vh.setText(R.id.title, bean.getData().getTitle());
                vh.setText(R.id.video_duration, TimeUtils.generateTime(bean.getData().getDuration()*1000));
                if (bean.getData().getAuthor() != null) {
                    vh.setImageUrl(R.id.ic_head, bean.getData().getAuthor().getIcon());
                }
                //itemView的点击
                vh.getView(R.id.layout_play_area).setOnClickListener(VideoListActivity.this);
                vh.getView(R.id.layout_play_area).setTag(bean);
            }
        };

        mAdapter.setOnItemClickListener(new RCommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });
        mLayoutManager=new LinearLayoutManager(this);
        mVideoList.setLayoutManager(mLayoutManager);
        mVideoList.addOnScrollListener(mOnScrollListener);
        mVideoList.setAdapter(mAdapter);
        refreshLayout.autoRefresh();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_play_area) { //点击的是视频的播放区域
            mIsClickToStop = true;
            v.setClickable(false);
            if (mCurrentPlayArea != null) { //当前播放的item区域是否为null
                if (mCurrentPlayArea != v) { //点击了另外的item进行播放
                    mCurrentPlayArea.setClickable(true);
                    mCurrentPlayArea.setVisibility(View.VISIBLE);
                    mCurrentPlayArea.findViewById(R.id.img_start_play).setVisibility(View.VISIBLE);
                    mCurrentPlayArea = v; //将被点击的列表item赋值给当前播放区域view
                } else { //click same area
                    if (mVideoFloatContainer.getVisibility() == View.VISIBLE) return;
                }
            } else {
                mCurrentPlayArea = v;
            }

            //invisible self ,and make visible when video play completely
            //v.setVisibility(View.INVISIBLE);//隐藏itemView
            if (mDMediaController != null)
                mDMediaController.hide();

            mVideoFloatContainer.setVisibility(View.VISIBLE);
            //mVideoPlayerView.setVisibility(View.VISIBLE);
            mVideoCoverMask.setVisibility(View.GONE);
            mVideoPlayerBg.setVisibility(View.GONE);
            v.findViewById(R.id.img_start_play).setVisibility(View.INVISIBLE); //隐藏当前播放区域的开始播放图标
            VideoListBean.IssueListBean.ItemListBean bean= (VideoListBean.IssueListBean.ItemListBean) v.getTag();
            mCurrentActiveVideoItem =bean.getData().getId();
            mDMediaController.setVideoViewTitle(bean.getData().getTitle());
            mCanTriggerStop = true;

            //move container view
            startMoveFloatContainer(true);

            mVideoLoadingView.setVisibility(View.VISIBLE);
            if (mDMediaController.getPlayState()!= DMediaController.STATE.idle ){
                mVideoPlayerView.setVisibility(View.INVISIBLE);//如果一开始就隐藏这个view，会造成播放失败
            }
            //播放视频
            mDMediaController.startPlay(bean.getData().getPlayUrl()); //开始播放

        }
    }

    @Override
    protected void initData() {
        mPresenter = new VideoListPresenter(this);
    }

    private void parseIntent() {

    }

    @Override
    public void onSuccess(VideoListBean mVideoListBean, boolean isLoadMore) {
        if (isLoadMore) {
            refreshLayout.finishLoadmore();
        } else {
            refreshLayout.finishRefresh();
        }
        if (mVideoListBean == null) return;

        ArrayList<VideoListBean.IssueListBean.ItemListBean> videos = new ArrayList<>();
        List<VideoListBean.IssueListBean> list = mVideoListBean.getIssueList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                for (VideoListBean.IssueListBean.ItemListBean data : list.get(i).getItemList()
                        ) {
                    if (data.getType().equals("video")) {
                        videos.add(data);
                    }
                }
            }
        }

        mAdapter.addDatas(videos, !isLoadMore);
    }

    @Override
    public void onFail(boolean isLoadMore) {
        if (isLoadMore) {
            refreshLayout.finishLoadmore(false);
        } else {
            refreshLayout.finishRefresh(false);
        }
    }

    /**
     * Runnable for update current video progress
     */
    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mDMediaController != null) {

                if (mDMediaController.isShowing()) {
                    mVideoProgressBar.setVisibility(View.GONE);
                } else {
                    mVideoProgressBar.setVisibility(View.VISIBLE);
                }

                long position = mDMediaController.getCurrentPosition();
                long duration = mDMediaController.getDuration();
                if (duration != 0) {
                    long pos = 1000L * position / duration;
                    int percent = mCurrentBuffer * 10;
                    mVideoProgressBar.setProgress((int) pos);//播放进度
                    mVideoProgressBar.setSecondaryProgress(percent);//显示缓冲进度
                    mHandler.postDelayed(this, 1000);//一秒钟更新一次进度条
                }
            }
        }
    };

    //开始移动播放item视频的那个view，当我们滑动recylerview和点击某个item的时候，会调用此方法
    private void startMoveFloatContainer(boolean click) {

        if (mVideoFloatContainer.getVisibility() != View.VISIBLE) return;
        final float moveDelta;

        if (click) {
            ViewCompat.setTranslationY(mVideoFloatContainer, 0);//首先移动到顶部
            //ViewAnimator.putOn(mVideoFloatContainer).translationY(0);

            int[] playAreaPos = new int[2];
            int[] floatContainerPos = new int[2];
            mCurrentPlayArea.getLocationOnScreen(playAreaPos);//获取当前view的x和y值保存到参数中
            mVideoFloatContainer.getLocationOnScreen(floatContainerPos);
            mOriginalHeight = moveDelta = playAreaPos[1] - floatContainerPos[1];//计算item播放view在y轴上的移动距离

        } else { //如果是滑动调用此方法
            moveDelta = mMoveDeltaY;
        }
        float translationY = moveDelta + (!click ? mOriginalHeight : 0);//滑动情况下调用的会再加上mOriginalHeight
        ViewCompat.setTranslationY(mVideoFloatContainer, translationY);//对mVideoFloatContainer在y轴上进行移动

    }

    @Override
    public void onPauseOrPlay(boolean isPause) {}

    @Override
    public void initPortControllerView(View v) {}

    @Override
    public void initLandControllerView(View v) {}

    @Override
    public View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflate.inflate(R.layout.layout_media_controller, null);
    }

    @Override
    public View makeLandControllerView() {
        return null;
    }

    //recyclerview的滑动监听
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        int totalDy;
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            SCROLL_STATE_TOUCH_SCROLL#手接触ScrollView触发一次
//            SCROLL_STATE_FLING#正在滚动
//            SCROLL_STATE_IDLE#滑动停止
            super.onScrollStateChanged(recyclerView, newState);
            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                //滑动结束时，保存y轴上的距离
                mOriginalHeight = mVideoFloatContainer.getTranslationY();
                totalDy = 0;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if(mDMediaController.isFullScreen()) return;

            //Calculate the total scroll distance of RecyclerView
            totalDy += dy;//向上滑动的时候，dy是正数，向下滑动是负数
            mMoveDeltaY = -totalDy;
            startMoveFloatContainer(false);

            //如果当前播放item被上拉或者下拉到屏幕之外去了，那么就停止播放
            if (mCurrentActiveVideoItem < mLayoutManager.findFirstVisibleItemPosition()
                    || mCurrentActiveVideoItem > mLayoutManager.findLastVisibleItemPosition()) {
                if (mCanTriggerStop&&mCurrentActiveVideoItem!=-1) {
                    mCanTriggerStop = false;
                    stopPlaybackImmediately();
                }
            }
        }
    };

    //停止播放
    public void stopPlaybackImmediately() {
        mIsClickToStop = false;

        if (mCurrentPlayArea != null) {
            mCurrentPlayArea.setClickable(true);
            mCurrentPlayArea.setVisibility(View.VISIBLE);
            mCurrentPlayArea.findViewById(R.id.img_start_play).setVisibility(View.VISIBLE);
        }

        if (mDMediaController != null) {
            mVideoFloatContainer.setVisibility(View.INVISIBLE);
            mDMediaController.stopPlay();
            //stop update progress
            mVideoProgressBar.setVisibility(View.GONE);
            mHandler.removeCallbacks(mProgressRunnable);
        }
    }

    //横竖屏发生改变的监听
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mVideoFloatContainer == null) return;

        ViewGroup.LayoutParams layoutParams = mVideoFloatContainer.getLayoutParams();

        mDMediaController.hide();

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { //竖屏
            //250 indicate the height of video play area
            layoutParams.height = (int) getResources().getDimension(R.dimen.video_item_portrait_height);
            layoutParams.width = MyApplication.getInstance().getWidth();
            ViewCompat.setTranslationY(mVideoFloatContainer, mOriginalHeight);

            // Show status bar
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            refreshLayout.setEnableRefresh(true);
            mVideoList.setEnableScroll(true); //可以滑动列表

        } else { //横屏

            layoutParams.height = MyApplication.getInstance().getHeight();
            layoutParams.width = MyApplication.getInstance().getWidth();
            ViewCompat.setTranslationY(mVideoFloatContainer, 0);
            refreshLayout.setEnableRefresh(false);
            // Hide status
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            mVideoList.setEnableScroll(false); //横屏时不能滑动recylerview
        }
        //给悬浮播放器容器设置宽高参数
        mVideoFloatContainer.setLayoutParams(layoutParams);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDMediaController.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDMediaController.destroy();
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
