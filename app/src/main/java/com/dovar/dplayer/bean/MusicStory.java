package com.dovar.dplayer.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ${dovar} on 2016/8/8.
 */
public class MusicStory extends BmobObject {
    private String music_name;//歌曲名称
    private String singer;//演唱者
    private String singer_style;//歌手类别
    private String story_title;//文章标题
    private String story_author;//文章作者
    private String music_story;//歌曲配文
    private String lrclink;//歌词链接
    private String song_id;//歌曲id
    private String albumpic;//专辑图片
    private String singer_avatar;//歌手头像
    private String durationtime;//播放时长
    private String url;//歌曲地址
    private int duration;//播放总时长

    public String getSinger_avatar() {
        return singer_avatar;
    }

    public void setSinger_avatar(String mSinger_avatar) {
        singer_avatar = mSinger_avatar;
    }

    public String getStory_title() {
        return story_title;
    }

    public void setStory_title(String mStory_title) {
        story_title = mStory_title;
    }

    public String getSinger_style() {
        return singer_style;
    }

    public void setSinger_style(String mSinger_style) {
        singer_style = mSinger_style;
    }

    public String getStory_author() {
        return story_author;
    }

    public void setStory_author(String mStory_author) {
        story_author = mStory_author;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String mMusic_name) {
        music_name = mMusic_name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String mSinger) {
        singer = mSinger;
    }

    public String getMusic_story() {
        return music_story;
    }

    public void setMusic_story(String mMusic_story) {
        music_story = mMusic_story;
    }

    public String getLrclink() {
        return lrclink;
    }

    public void setLrclink(String mLrclink) {
        lrclink = mLrclink;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String mSong_id) {
        song_id = mSong_id;
    }

    public String getAlbumpic() {
        return albumpic;
    }

    public void setAlbumpic(String mAlbumpic) {
        albumpic = mAlbumpic;
    }

    public String getDurationtime() {
        return durationtime;
    }

    public void setDurationtime(String mDurationtime) {
        durationtime = mDurationtime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String mUrl) {
        url = mUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int mDuration) {
        duration = mDuration;
    }
}
