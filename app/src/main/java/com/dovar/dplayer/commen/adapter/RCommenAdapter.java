package com.dovar.dplayer.commen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.dovar.dplayer.commen.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dovar on 2016/12/28 0028.
 * Email:xiaohe0949@163.com
 * 通用RecyclerView适配器
 * 本类只适用于三种布局类型：TYPE_HEADER、TYPE_FOOTER、TYPE_COMMON
 * 多布局类型请使用{@link MultiCommonAdapter}
 */
public abstract class RCommenAdapter<T> extends RecyclerView.Adapter<RCommenViewHolder> {

    private static final String TAG = "adapter";
    protected Context mContext;
    private int mLayoutId;
    protected List<T> mDatas;

    private static final int TYPE_COMMON = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_HEADER = 2;
    private boolean hasFooter = false;//是否已添加尾布局
    private boolean hasHeader = false;//是否已添加头布局
    private View headerView;
    private View footerView;

    public boolean isHasFooter() {
        return hasFooter;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }

    public View getFooterView() {
        return footerView;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
    }

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
            LogUtil.d(TAG, "addData: " + data);
            mDatas.add(data);
            if (isNotify) {
                //1.适用于调用addData()之前mDatas.size()=0的情况
                //2.传入的position>mDatas.size() - 1时，效果与position=mDatas.size() - 1相同
                if (hasHeader) {
                    notifyItemInserted(mDatas.size());
                } else {
                    notifyItemInserted(mDatas.size() - 1);
                }
            }
        } else {
            LogUtil.d(TAG, "addData: data==null");
        }
    }

    /**
     * 添加数据集合,去除其中的null数据再添加
     *
     * @param datas   数据list
     * @param isClear 是否需要先清空原有数据再添加
     */
    public void addDatas(List<T> datas, boolean isClear) {
        if (isClear) {
            this.mDatas.clear();
        }

        if (datas != null && datas.size() > 0) {
            ArrayList<T> nononList = new ArrayList<>();
            for (T data : datas
                    ) {
                if (data != null) {
                    nononList.add(data);
                }
            }

            if (nononList.size() > 0) {
                int preSize = mDatas.size();
                this.mDatas.addAll(nononList);
                if (!isClear) {
                    if (hasHeader) {
                        preSize++;
                    }
                    notifyItemRangeInserted(preSize, nononList.size());
                    return;
                }
            }
        } else {
            LogUtil.d(TAG, "addDatas: datas==null");
        }

        if (isClear) {
            notifyDataSetChanged();
        }
    }

    /**
     * 插入数据到pos=0,去除其中的null数据再添加
     *
     * @param datas 数据list
     */
    public void insertDatas(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            ArrayList<T> nononList = new ArrayList<>();
            for (T data : datas
                    ) {
                if (data != null) {
                    nononList.add(data);
                }
            }
            if (nononList.size() > 0) {
                this.mDatas.addAll(0, nononList);
                int notifyPosStart = 0;
                if (hasHeader) {
                    notifyPosStart++;
                }
                notifyItemRangeInserted(notifyPosStart, nononList.size());
            }
        } else {
            LogUtil.d(TAG, "insertDatas: datas==null");
        }

    }


    /**
     * @param data
     * @param pos  data在数据集中的位置，不是在adapter中实际展示的位置
     */
    public void insertData(T data, int pos) {
        if (data != null && pos >= 0) {
            if (mDatas.size() == 0) {
                pos = 0;
            } else if (pos > mDatas.size() - 1) {
                pos = mDatas.size() - 1;
            }
            mDatas.add(pos, data);
            if (hasHeader) {
                pos++;
            }
            notifyItemInserted(pos);
        }
    }

    public void updateData(T data, int pos) {
        if (data != null && pos >= 0 && pos < getDataCount()) {
            mDatas.remove(pos);
            mDatas.add(pos, data);
            if (hasHeader) {
                pos++;
            }
            notifyItemChanged(pos);
        }
    }

    /**
     * 添加数据数组
     *
     * @param datas   数据collection
     * @param isClear 是否需要先清空原有数据再添加
     */
    public void addDatas(T[] datas, boolean isClear) {
        if (datas != null && datas.length > 0) {
            List<T> list = Arrays.asList(datas);
            addDatas(list, isClear);
        }
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
        LogUtil.d(TAG, "getItem: getItem==null");
        return null;
    }

    public int getPosInList(int pos) {
        if (headerView != null && hasHeader) return pos - 1;
        return pos;
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
    public int getDataCount() {
        if (mDatas.size() == 0) {
            LogUtil.d(TAG, "getItemCount: count==0");
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
            LogUtil.d(TAG, "clear: mDatas==null");
        }
    }

    /**
     * 移除指定mDatas对应position上的数据,并刷新
     *
     * @param position
     */
    public void remove(int position) {
        if (position >= 0 && position < mDatas.size()) {
            mDatas.remove(position);
//            if (mDatas.size() == 0) return;
            int notifyPos = position;
            if (hasHeader) {
                notifyPos++;
            }
            notifyItemRemoved(notifyPos);//mDatas.size()==0时此方法会报下标溢出//待测试
        }
    }

    public void remove(T data) {
        int notifyPos = -1;
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i) == data) {
                notifyPos = i;
                break;
            }
        }
        mDatas.remove(data);
        //局部刷新
        if (notifyPos != -1) {
            if (hasHeader) {
                notifyPos++;
            }
            notifyItemRemoved(notifyPos);
        }
    }

    public abstract void convert(RCommenViewHolder vh, int position);

    public RCommenAdapter(Context context, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = new ArrayList<>();
    }

    public RCommenAdapter(Context context, int layoutId,@NonNull List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
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
    public void onBindViewHolder(final RCommenViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        if (getItemViewType(position) == TYPE_FOOTER) return;
        int realPos;
        if (hasHeader) {//有header时，header对应的position为0，其他从1开始
            realPos = position - 1;
        } else {
            realPos = position;
        }
        convert(holder, realPos);
        //普通item点击事件
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //必须使用holder.getAdapterPosition()动态获取正确的position,否则在数据发生变化时，点击事件还是响应原来的position
                    onItemClickListener.onItemClick(hasHeader ? holder.getAdapterPosition() - 1 : holder.getAdapterPosition());
                }
            });
        }
        //给childView设置onclick事件时，在onClick方法中的position必须是使用holder获取
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


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
