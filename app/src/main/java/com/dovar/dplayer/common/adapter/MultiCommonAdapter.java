package com.dovar.dplayer.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * recyclerView多布局通用适配器
 * Created by heweizong on 2017/2/28.
 */

public abstract class MultiCommonAdapter<T> extends RCommonAdapter<T> {
    public MultiCommonAdapter(Context context, List<T> datas) {
        super(context, -1, datas);
    }

    public MultiCommonAdapter(Context context) {
        super(context, -1);
    }

    @Override
    public int getItemViewType(int position) {
        if (getHeaderView() != null && isHasHeader() && position == 0) return TYPE_HEADER;
        if (getFooterView() != null && isHasFooter() && position == getItemCount() - 1)
            return TYPE_FOOTER;
        if (isHasHeader()) return getItemType(position - 1);
        return getItemType(position);
    }

    public abstract int getItemType(int position);

    //利用viewType返回layoutId,直接生成viewHolder
    @Override
    public RCommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            if (getHeaderView() == null) return null;
            return new RCommonViewHolder(getHeaderView());
        }
        if (viewType == TYPE_FOOTER) {
            if (getFooterView() == null) return null;
            return new RCommonViewHolder(getFooterView());
        }
        return new RCommonViewHolder(View.inflate(mContext, viewType, null));
    }

}
