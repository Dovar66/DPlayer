package com.dovar.dplayer.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017-09-13.
 */

public abstract class RCommenAdapter<T> extends RecyclerView.Adapter<RCommenViewHolder> {
    private static final String TAG = "adapter";
    protected Context mContext;
    private int mLayoutId;
    protected List<T> mDatas;

    private static final int TYPE_COMMON = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_HEADER = 2;
    private boolean hasFooter = false;//是否已添加尾布局
    private boolean hasHeader = false;//是否已添加头布局
    private View headerView;
    private View footerView;


    /**
     * 添加尾布局
     *
     * @param footerView
     */
    public void addFooterView(View footerView) {
        this.footerView = footerView;
        hasFooter = true;
    }

    /**
     * 添加头布局
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        this.headerView = headerView;
        hasHeader = true;
    }

    /**
     * 添加一条数据
     *
     * @param data
     * @param isNotify 是否需要在添加数据后立即刷新界面
     */
    public void addData(T data, boolean isNotify) {
        if (data != null) {
            Log.d(TAG, "addData: " + data);
            mDatas.add(data);
            if (isNotify) {
                notifyDataSetChanged();//可修改为局部刷新
            }
        } else {
            Log.d(TAG, "addData: data==null");
        }
    }

    /**
     * 添加数据集合
     *
     * @param datas   数据list
     * @param isClear 是否需要先清空原有数据再添加
     */
    public void addDatas(List<T> datas, boolean isClear) {
        if (isClear) {
            this.mDatas.clear();
        }
        if (datas != null) {
            this.mDatas.addAll(datas);
        } else {
            Log.d(TAG, "addDatas: datas==null");
        }
        notifyDataSetChanged();
    }

    /**
     * 添加数据数组
     *
     * @param datas   数据collection
     * @param isClear 是否需要先清空原有数据再添加
     */
    public void addDatas(T[] datas, boolean isClear) {
        if (isClear) {
            this.mDatas.clear();
        }
        if (datas != null) {
            Collections.addAll(this.mDatas, datas);
        } else {
            Log.d(TAG, "addDatas: datas==null");
        }
        notifyDataSetChanged();
    }

    /**
     * 获取mDatas中对应下标的item
     *
     * @param pos
     */
    public T getItem(int pos) {
        if (pos >= 0 && pos < mDatas.size()) {
            return mDatas.get(pos);
        }
        Log.d(TAG, "getItem: getItem==null");
        return null;
    }

    /**
     * 获取mDatas
     *
     * @return
     */
    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * 返回所有item数量，包括header、footer
     * 此方法的返回值直接决定recyclerView的child数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        //header与footer都有
        if (hasFooter && hasHeader) return getDataCount() + 2;
        //header与footer都没有
        if (!hasFooter && !hasHeader) return getDataCount();
        //header与footer只有一个
        return getDataCount() + 1;
    }

    /**
     * 返回bodyItem数量
     * mDatas.size()
     *
     * @return
     */
    private int getDataCount() {
        if (mDatas.size() == 0) {
            Log.d(TAG, "getItemCount: count==0");
        }
        return mDatas.size();
    }

    /**
     * 清空数据源
     */
    public void clear() {
        if (mDatas != null) {
            mDatas.clear();
        } else {
            Log.d(TAG, "clear: mDatas==null");
        }
    }

    /**
     * 移除指定mDatas对应position上的数据
     *
     * @param position
     */
    public void remove(int position) {
        if (position >= 0 && position < mDatas.size()) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

    public abstract void convert(RCommenViewHolder vh, int position);

    public RCommenAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        if (datas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas = datas;
        }
    }


    @Override
    public RCommenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            if (headerView == null) {
                return null;
            }
            return new RCommenViewHolder(headerView);
        }
        if (viewType == TYPE_FOOTER) {
            if (footerView == null) {
                return null;
            }
            return new RCommenViewHolder(footerView);
        }
        return new RCommenViewHolder(View.inflate(mContext, mLayoutId, null));
    }

    @Override
    public void onBindViewHolder(RCommenViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        if (getItemViewType(position) == TYPE_FOOTER) return;
        if (hasHeader) {//有header时，header对应的position为0，其他从1开始
            convert(holder, position - 1);
        } else {
            convert(holder, position);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (headerView != null && hasHeader && position == 0) return TYPE_HEADER;
        if (footerView != null && hasFooter && position == getItemCount() - 1) return TYPE_FOOTER;
        return TYPE_COMMON;
    }


    /**
     * GridManager时设置header、footer跨列
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)) {
                        case TYPE_FOOTER:
                        case TYPE_HEADER:
                            //设置跨列
                            return gridManager.getSpanCount();
                        default:
                            return 1;
                    }
                }
            });
        }
    }

}
