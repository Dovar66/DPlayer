package com.dovar.dplayer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gdtv.common.util.PhotoUtil;
import com.gdtv.common.view.imagelistviewpager.TouchImageView;
import com.gdtv.module.live.liveShow.adapter.AnchorAdapter;

import java.util.List;

/**
 * Created by heweizong on 2017/9/14.
 */

public class TouchImageAdapter extends PagerAdapter {
    private List<String> imagelist;
    private Context context;
    private PopupWindow mPopupWindow;

    public TouchImageAdapter(List<String> imagelist, Context context, PopupWindow mPopupWindow) {
        this.imagelist = imagelist;
        this.context = context;
        this.mPopupWindow = mPopupWindow;
    }

    @Override
    public int getCount() {
        return imagelist.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewpager_imagelist_item, null);
        TouchImageView img = (TouchImageView) view.findViewById(R.id.img);
        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.
                LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow != null&&mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
        if (imagelist.get(position).equals(AnchorAdapter.IMG_OFFLINE)) {
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setImageResource(R.drawable.img_offline);
        } else {
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            PhotoUtil.loadPhoto(img, PhotoUtil.setPhotoSize(imagelist.get(position), 100),
                    R.drawable.empty_picture, R.drawable.empty_picture, DiskCacheStrategy.SOURCE);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        PhotoUtil.clearMemory();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}