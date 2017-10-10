package com.dovar.dplayer.module.music;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.Music;
import com.dovar.dplayer.bean.MusicBean;
import com.dovar.dplayer.bean.MusicByIdBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
    }

    /**
     * 获取歌单
     *
     * @param page 歌单当前页码
     * @param flag 是否清除adapter中数据
     */
    private void queryData(int page, final boolean flag) {
        String offset = String.valueOf(page * 20);

        MusicBean mMusicBean = new Gson().fromJson(mS, MusicBean.class);
        List<Music> mMusicList = new ArrayList<Music>();
        for (MusicBean.Songlist song : mMusicBean.getSong_list()
                ) {
            final Music music = new Music();
            music.setName(song.getTitle());//歌曲名
            music.setSinger(formatSingers(song.getArtist_name()));//歌手名
            music.setSong_id(song.getSong_id());//歌曲id
            music.setLrclink(song.getLrclink());//歌词链接
            music.setPic_big(song.getPic_big());//大图标
            music.setPic_small(song.getPic_small());//小图标
            mMusicList.add(music);


            MusicByIdBean musicById = new Gson().fromJson(mS, MusicByIdBean.class);
            music.setUrl(musicById.getData().getSongList().get(0).getSongLink());

        }
    }

    /**
     * 合唱歌曲时最多只显示两个歌手名
     *
     * @param singers 歌手名
     */
    private String formatSingers(String singers) {
        String[] singerArray = singers.split(",");
        if (singerArray.length > 2) {
            return singerArray[0] + "," + singerArray[1];
        }
        return singers;
    }


    public static void getMusicList(String offset) {
        //获取新歌榜
        String url = BAIDU_HOT + "&offset=" + offset + "&size=20";

        //根据歌曲ID获取歌曲播放信息
        String url2 = "http://ting.baidu.com/data/music/links?songIds=" + mMusic.getSong_id();
    }

    //百度新歌榜
    public static final String BAIDU_HOT = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=1";
}
