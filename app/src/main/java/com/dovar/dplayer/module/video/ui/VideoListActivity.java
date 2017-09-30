package com.dovar.dplayer.module.video.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.VideoBean;
import com.dovar.dplayer.bean.VideoDataBean;
import com.dovar.dplayer.common.DMediaController;
import com.dovar.dplayer.common.adapter.RCommenAdapter;
import com.dovar.dplayer.common.adapter.RCommenViewHolder;
import com.dovar.dplayer.common.base.StatusBarTintActivity;
import com.dovar.dplayer.common.utils.ToastUtil;
import com.dovar.dplayer.module.video.contract.VideoContract;
import com.dovar.dplayer.module.video.presenter.VideoPresenter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class VideoListActivity extends StatusBarTintActivity implements VideoContract.IView<VideoBean>,DMediaController.ControlListener {

    private VideoPresenter mPresenter;
    private RecyclerView mVideoList;
    private RCommenAdapter<VideoDataBean> mAdapter;
    private RefreshLayout refreshLayout;

    public static void jump(Context mContext){
        Intent mIntent=new Intent(mContext,VideoListActivity.class);
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
                vh.setImageUrl(R.id.thumnails, getItem(position).getCover().getDetail());
            }
        };
        mVideoList.setLayoutManager(new LinearLayoutManager(this));
        mVideoList.setAdapter(mAdapter);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onSuccess(VideoBean bean) {
        refreshLayout.finishRefresh();
        if (bean == null) return;
        ArrayList<VideoBean.ItemListBean> list = (ArrayList<VideoBean.ItemListBean>) bean.getItemList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("video")) {
                VideoDataBean dataBean = new Gson().fromJson(list.get(i).getData(), VideoDataBean.class);
                mAdapter.addData(dataBean, true);
            }
        }
    }

    @Override
    public void onFail() {

    }



    private void setupVideoPlayer(){
//        DMediaController mDMediaController=new DMediaController(this);
//        mDMediaController.setControlListener(this);
//        mDMediaController.setAnchorView(findViewById(R.id.video_frame));
//        mDMediaController.startPlay("");

        TextureView mTextureView=new TextureView(this);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                ToastUtil.show("available");
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                ToastUtil.show("available");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                ToastUtil.show("available");
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                ToastUtil.show("available");
            }
        });

        FrameLayout container= (FrameLayout) findViewById(R.id.video_frame);
        container.addView(mTextureView, ViewGroup.LayoutParams.MATCH_PARENT,-1);
    }

    @Override
    public void onClickClose() {

    }

    @Override
    public void onScreenPortOrLand() {

    }

    @Override
    public void onClickMore() {

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
}
