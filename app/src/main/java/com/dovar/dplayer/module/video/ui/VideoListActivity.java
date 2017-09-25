package com.dovar.dplayer.module.video.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dovar.dplayer.R;
import com.dovar.dplayer.commen.adapter.RCommenAdapter;
import com.dovar.dplayer.commen.adapter.RCommenViewHolder;
import com.dovar.dplayer.bean.VideoBean;
import com.dovar.dplayer.bean.VideoDataBean;
import com.dovar.dplayer.commen.base.StatusBarTintActivity;
import com.dovar.dplayer.module.video.contract.VideoContract;
import com.dovar.dplayer.module.video.presenter.VideoPresenter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class VideoListActivity extends StatusBarTintActivity implements VideoContract.IView<VideoBean> {

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
        mAdapter = new RCommenAdapter<VideoDataBean>(this, R.layout.item_video, null) {
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
}
