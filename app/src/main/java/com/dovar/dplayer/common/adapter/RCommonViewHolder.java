package com.dovar.dplayer.common.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dovar.dplayer.R;
import com.dovar.dplayer.common.utils.PhotoUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Dovar on 2016/12/28 0028.
 * Email:xiaohe0949@163.com
 * 通用RecyclerView的ViewHolder
 */
public class RCommonViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;


    public RCommonViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }


    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public RCommonViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            if (!TextUtils.isEmpty(text)) {
                tv.setText(text);
            } else {
                tv.setText("");
            }
        }
        return this;
    }

    public RCommonViewHolder setTextColor(int viewId, int color) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setTextColor(color);
        }
        return this;
    }

    public RCommonViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setImageResource(resId);
        }
        return this;
    }

    /**
     * 使用glide加载网络图片
     */
    public void setImageUrl(int viewId, String url) {
        ImageView iv = getView(viewId);
        if (iv != null) {
            if (!TextUtils.isEmpty(url)) {
                PhotoUtil.loadPhoto(iv, url);
            } else {
                setImageResource(viewId, R.mipmap.ic_launcher);
            }
        }
    }

    public void setImageUrl(int viewId, String url, int defaultResId) {
        ImageView iv = getView(viewId);
        if (iv != null) {
            try {
                if (!TextUtils.isEmpty(url)) {
                    PhotoUtil.loadPhoto(iv, url, defaultResId, defaultResId);
                } else {
                    setImageResource(viewId, defaultResId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载圆形头像
     */
    public void loadCircleImage(int viewId, String url) {
        ImageView iv = getView(viewId);
        if (iv != null) {
            if (!TextUtils.isEmpty(url)) {
                PhotoUtil.loadCirclePhoto(iv, url, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            } else {
                PhotoUtil.loadLocalCirclePhoto(iv, R.mipmap.ic_launcher);
            }
        }
    }

    public void setBackgroundResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setBackgroundResource(resId);
        }
    }

    public void setBackgroundDrawable(int viewId, Drawable drawable) {
        if (drawable != null) {
            ImageView view = getView(viewId);
            if (view != null) {
                view.setBackgroundDrawable(drawable);
            }
        }
    }

    public void setVisible(int viewId, boolean visible) {
        View child = getView(viewId);
        if (child != null) {
            if (visible) {
                child.setVisibility(View.VISIBLE);
            } else {
                child.setVisibility(View.GONE);
            }
        }
    }

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface visibility {
    }

    public void setVisible(int viewId, @visibility int visibility) {
        View child = getView(viewId);
        if (child != null) {
            child.setVisibility(visibility);
        }
    }

    /**
     * 给itemView中的子View添加点击事件
     */
    public RCommonViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

}
