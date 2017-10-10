package com.dovar.dplayer.module.video.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.VideoListBean;
import com.dovar.dplayer.common.adapter.RCommenAdapter;
import com.dovar.dplayer.common.adapter.RCommenViewHolder;
import com.dovar.dplayer.common.base.StatusBarTintActivity;
import com.dovar.dplayer.module.video.contract.VideoListContract;
import com.dovar.dplayer.module.video.presenter.VideoListPresenter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends StatusBarTintActivity implements VideoListContract.IView<VideoListBean> {

    private VideoListPresenter mPresenter;
    private RecyclerView mVideoList;
    private RCommenAdapter<VideoListBean.IssueListBean.ItemListBean> mAdapter;
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
    }


    @Override
    protected void initUI() {
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
        mAdapter = new RCommenAdapter<VideoListBean.IssueListBean.ItemListBean>(this, R.layout.item_video) {
            @Override
            public void convert(RCommenViewHolder vh, int position) {
                VideoListBean.IssueListBean.ItemListBean bean = getItem(position);
                if (bean == null) return;
//                vh.setImageUrl(R.id.thumnails, bean.getCover().getDetail());
//                vh.setText(R.id.nickName, bean.getProvider().getName());
//                vh.setText(R.id.title, bean.getTitle());
//                vh.setImageUrl(R.id.ic_head, bean.getProvider().getIcon());
                vh.setImageUrl(R.id.thumnails, bean.getData().getCover().getDetail());
                vh.setText(R.id.nickName, bean.getData().getCategory());
                vh.setText(R.id.title, bean.getData().getTitle());
                if (bean.getData().getAuthor() != null) {
                    vh.setImageUrl(R.id.ic_head, bean.getData().getAuthor().getIcon());
                }
            }
        };

        mAdapter.setOnItemClickListener(new RCommenAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                VideoListBean.IssueListBean.ItemListBean bean = mAdapter.getItem(position);

                VideoActivity.jump(mContext, bean.getData().getPlayUrl());
            }
        });
        mVideoList.setLayoutManager(new GridLayoutManager(this, 2));
        mVideoList.setAdapter(mAdapter);
        refreshLayout.autoRefresh();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
