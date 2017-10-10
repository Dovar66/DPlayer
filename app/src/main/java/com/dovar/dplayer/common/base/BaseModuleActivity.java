package com.dovar.dplayer.common.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by heweizong on 2017/9/11.
 */

public abstract class BaseModuleActivity extends AppCompatActivity {
    protected Context mContext = this;

    protected abstract void initUI();

    protected abstract void initData();

    /**
     * 通过viewId获取控件
     */
    public <V extends View> V findView(int viewId) {
        return (V) findViewById(viewId);
    }

    public <V extends View> V findView(int viewId, View parent) {
        if (parent == null) return null;
        return (V) parent.findViewById(viewId);
    }

    public <V extends View> V addViewClickEvent(int viewId, View.OnClickListener onClickImp) {
        V view = findView(viewId);
        if (view == null) return null;
        view.setOnClickListener(onClickImp);
        return view;
    }
}
