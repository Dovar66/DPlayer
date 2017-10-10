package com.dovar.dplayer.module.video.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.VideoDataBean;
import com.dovar.dplayer.common.DMediaController;
import com.dovar.dplayer.common.adapter.RCommenAdapter;
import com.dovar.dplayer.common.adapter.RCommenViewHolder;
import com.dovar.dplayer.common.base.StatusBarTintActivity;
import com.dovar.dplayer.module.video.contract.VideoContract;
import com.dovar.dplayer.module.video.presenter.VideoPresenter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class VideoListActivity extends StatusBarTintActivity implements VideoContract.IView<ArrayList<VideoDataBean>>, DMediaController.ControlListener {

    private VideoPresenter mPresenter;
    private RecyclerView mVideoList;
    private RCommenAdapter<VideoDataBean> mAdapter;
    private RefreshLayout refreshLayout;
    private DMediaController mDMediaController;

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
//        setupVideoPlayer();
    }

    private void initData() {
        mPresenter = new VideoPresenter(this);
    }

    private void parseIntent() {

    }

    private void initUI() {
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
                refreshlayout.finishLoadmore(2000);
            }
        });

        mVideoList = findView(R.id.recyclerview);
        mAdapter = new RCommenAdapter<VideoDataBean>(this, R.layout.item_video) {
            @Override
            public void convert(RCommenViewHolder vh, int position) {
                VideoDataBean bean=getItem(position);
                if (bean==null) return;
                vh.setImageUrl(R.id.thumnails,bean .getCover().getDetail());
                vh.setText(R.id.nickName,bean.getProvider().getName());
                vh.setText(R.id.title,bean.getTitle());
                vh.setImageUrl(R.id.ic_head,bean.getProvider().getIcon());
            }
        };
        mVideoList.setLayoutManager(new GridLayoutManager(this, 2));
        mVideoList.setAdapter(mAdapter);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onSuccess(ArrayList<VideoDataBean> datas) {
        refreshLayout.finishRefresh();
        if (datas == null) return;
        mAdapter.addDatas(datas, true);
    }

    @Override
    public void onFail() {

    }


    private void setupVideoPlayer() {
//        mDMediaController = new DMediaController(this);
//        mDMediaController.setControlListener(this);
//        mDMediaController.setAnchorView(findViewById(R.id.video_frame));
//        mDMediaController.startPlay("");

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

    }

    @Override
    public void initPortControllerView(View v) {

    }

    @Override
    public void initLandControllerView(View v) {

    }

    @Override
    public View makeControllerView() {
        return null;
    }

    @Override
    public View makeLandControllerView() {
        return null;
    }

    @Override
    protected void onDestroy() {
        if (mDMediaController != null) {
            mDMediaController.destroy();
        }
        super.onDestroy();
    }
}
