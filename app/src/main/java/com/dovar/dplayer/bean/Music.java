package com.dovar.dplayer.bean;

/**
 * Created by ${dovar} on 2016/6/20.
 */
public class Music {


    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String mSong_id) {
        song_id = mSong_id;
    }

    public String getLrclink() {
        return lrclink;
    }

    public void setLrclink(String mLrclink) {
        lrclink = mLrclink;
    }
    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String mPic_small) {
        pic_small = mPic_small;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String mPic_big) {
        pic_big = mPic_big;
    }

    private String pic_small;
    private String pic_big;
    private String lrclink;//歌词链接
    private String song_id;//歌曲id
    private String name;//歌曲名称
    private String singer;//演唱者
    private String albumpic;//专辑图片
    private String durationtime;//播放时长
    private String description;//用户描述
    private String url;//歌曲地址
    private int curPosition=0;//当前播放时间
    private int duration=0;//播放总时长
    //    private String album;//专辑名称
//    private String author;//作词
//    private String composer;//作曲
//    private String downcount;//下载数量
//    private String favcount;//点赞数量
    private int id;//歌曲编号
    private String musicpath;//歌曲路径

    public int getDuration() {
        return duration;
    }

    public void setDuration(int mDuration) {
        duration = mDuration;
    }

    public int getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(int mCurPosition) {
        curPosition = mCurPosition;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String mUrl) {
        url = mUrl;
    }

    public void setName(String mName) {
        name = mName;
    }

    public void setSinger(String mSinger) {
        singer = mSinger;
    }

    public void setAlbumpic(String mAlbumpic) {
        albumpic = mAlbumpic;
    }

    public void setDurationtime(String mDurationtime) {
        durationtime = mDurationtime;
    }

    public void setDescription(String mDescription) {
        description = mDescription;
    }

    public void setId(int mId) {
        id = mId;
    }

    public void setMusicpath(String mMusicpath) {
        musicpath = mMusicpath;
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public String getAlbumpic() {
        return albumpic;
    }

    public String getDurationtime() {
        return durationtime;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getMusicpath() {
        return musicpath;
    }

}
