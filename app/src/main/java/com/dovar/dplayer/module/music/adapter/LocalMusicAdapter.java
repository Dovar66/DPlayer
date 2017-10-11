package com.dovar.dplayer.module.music.adapter;

import android.content.Context;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.LocalTrack;
import com.dovar.dplayer.common.adapter.RCommonAdapter;
import com.dovar.dplayer.common.adapter.RCommonViewHolder;

/**
 * Created by Administrator on 2017-10-11.
 */

public class LocalMusicAdapter extends RCommonAdapter<LocalTrack> {
    public LocalMusicAdapter(Context context) {
        super(context, R.layout.list_item_2);
    }

    @Override
    public void convert(RCommonViewHolder vh, int position) {
        LocalTrack track = getItem(position);
        vh.setText(R.id.title_2,track.getTitle());
        vh.setText(R.id.url_2,track.getArtist());
        vh.setImageUrl(R.id.img_2,track.getPath());
    }
}
