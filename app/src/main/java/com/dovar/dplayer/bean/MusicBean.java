package com.dovar.dplayer.bean;

import java.util.List;

/**
 * Created by ${dovar} on 2016/7/28.
 */
public class MusicBean {
    List<Songlist> song_list;

    @Override
    public String toString() {
        return "MusicBean{" +
                "song_list=" + song_list +
                '}';
    }

    public List<Songlist> getSong_list() {
        return song_list;
    }

    public void setSong_list(List<Songlist> mSong_list) {
        song_list = mSong_list;
    }

    public class Songlist{
        String artist_id;
        String language;
        String pic_big;
        String pic_small;
        String country;
        String area;
        String publishtime;
        String album_no;
        String lrclink;
        String copy_type;
        String hot;
        String all_artist_ting_uid;
        String resource_type;
        String is_new;
        String rank_change;
        String rank;
        String all_artist_id;
        String style;
        String del_status;
        String relate_status;
        String toneid;
        String all_rate;
        String sound_effect;
        String file_duration;
        String has_mv_mobile;
        String versions;
        String bitrate_fee;
        String song_id;
        String title;
        String ting_uid;
        String author;
        String album_id;
        String album_title;
        String is_first_publish;
        String havehigh;
        String charge;
        String has_mv;
        String learn;
        String song_source;
        String piao_id;
        String korean_bb_song;
        String resource_type_ext;
        String mv_provider;
        String artist_name;

        @Override
        public String toString() {
            return "Songlist{" +
                    "artist_id='" + artist_id + '\'' +
                    ", language='" + language + '\'' +
                    ", pic_big='" + pic_big + '\'' +
                    ", pic_small='" + pic_small + '\'' +
                    ", country='" + country + '\'' +
                    ", area='" + area + '\'' +
                    ", publishtime='" + publishtime + '\'' +
                    ", album_no='" + album_no + '\'' +
                    ", lrclink='" + lrclink + '\'' +
                    ", copy_type='" + copy_type + '\'' +
                    ", hot='" + hot + '\'' +
                    ", all_artist_ting_uid='" + all_artist_ting_uid + '\'' +
                    ", resource_type='" + resource_type + '\'' +
                    ", is_new='" + is_new + '\'' +
                    ", rank_change='" + rank_change + '\'' +
                    ", rank='" + rank + '\'' +
                    ", all_artist_id='" + all_artist_id + '\'' +
                    ", style='" + style + '\'' +
                    ", del_status='" + del_status + '\'' +
                    ", relate_status='" + relate_status + '\'' +
                    ", toneid='" + toneid + '\'' +
                    ", all_rate='" + all_rate + '\'' +
                    ", sound_effect='" + sound_effect + '\'' +
                    ", file_duration='" + file_duration + '\'' +
                    ", has_mv_mobile='" + has_mv_mobile + '\'' +
                    ", versions='" + versions + '\'' +
                    ", bitrate_fee='" + bitrate_fee + '\'' +
                    ", song_id='" + song_id + '\'' +
                    ", title='" + title + '\'' +
                    ", ting_uid='" + ting_uid + '\'' +
                    ", author='" + author + '\'' +
                    ", album_id='" + album_id + '\'' +
                    ", album_title='" + album_title + '\'' +
                    ", is_first_publish='" + is_first_publish + '\'' +
                    ", havehigh='" + havehigh + '\'' +
                    ", charge='" + charge + '\'' +
                    ", has_mv='" + has_mv + '\'' +
                    ", learn='" + learn + '\'' +
                    ", song_source='" + song_source + '\'' +
                    ", piao_id='" + piao_id + '\'' +
                    ", korean_bb_song='" + korean_bb_song + '\'' +
                    ", resource_type_ext='" + resource_type_ext + '\'' +
                    ", mv_provider='" + mv_provider + '\'' +
                    ", artist_name='" + artist_name + '\'' +
                    '}';
        }

        public void setArtist_id(String mArtist_id) {
            artist_id = mArtist_id;
        }

        public void setLanguage(String mLanguage) {
            language = mLanguage;
        }

        public void setPic_big(String mPic_big) {
            pic_big = mPic_big;
        }

        public void setPic_small(String mPic_small) {
            pic_small = mPic_small;
        }

        public void setCountry(String mCountry) {
            country = mCountry;
        }

        public void setArea(String mArea) {
            area = mArea;
        }

        public void setPublishtime(String mPublishtime) {
            publishtime = mPublishtime;
        }

        public void setAlbum_no(String mAlbum_no) {
            album_no = mAlbum_no;
        }

        public void setLrclink(String mLrclink) {
            lrclink = mLrclink;
        }

        public void setCopy_type(String mCopy_type) {
            copy_type = mCopy_type;
        }

        public void setHot(String mHot) {
            hot = mHot;
        }

        public void setAll_artist_ting_uid(String mAll_artist_ting_uid) {
            all_artist_ting_uid = mAll_artist_ting_uid;
        }

        public void setResource_type(String mResource_type) {
            resource_type = mResource_type;
        }

        public void setIs_new(String mIs_new) {
            is_new = mIs_new;
        }

        public void setRank_change(String mRank_change) {
            rank_change = mRank_change;
        }

        public void setRank(String mRank) {
            rank = mRank;
        }

        public void setAll_artist_id(String mAll_artist_id) {
            all_artist_id = mAll_artist_id;
        }

        public void setStyle(String mStyle) {
            style = mStyle;
        }

        public void setDel_status(String mDel_status) {
            del_status = mDel_status;
        }

        public void setRelate_status(String mRelate_status) {
            relate_status = mRelate_status;
        }

        public void setToneid(String mToneid) {
            toneid = mToneid;
        }

        public void setAll_rate(String mAll_rate) {
            all_rate = mAll_rate;
        }

        public void setSound_effect(String mSound_effect) {
            sound_effect = mSound_effect;
        }

        public void setFile_duration(String mFile_duration) {
            file_duration = mFile_duration;
        }

        public void setHas_mv_mobile(String mHas_mv_mobile) {
            has_mv_mobile = mHas_mv_mobile;
        }

        public void setVersions(String mVersions) {
            versions = mVersions;
        }

        public void setBitrate_fee(String mBitrate_fee) {
            bitrate_fee = mBitrate_fee;
        }

        public void setSong_id(String mSong_id) {
            song_id = mSong_id;
        }

        public void setTitle(String mTitle) {
            title = mTitle;
        }

        public void setTing_uid(String mTing_uid) {
            ting_uid = mTing_uid;
        }

        public void setAuthor(String mAuthor) {
            author = mAuthor;
        }

        public void setAlbum_id(String mAlbum_id) {
            album_id = mAlbum_id;
        }

        public void setAlbum_title(String mAlbum_title) {
            album_title = mAlbum_title;
        }

        public void setIs_first_publish(String mIs_first_publish) {
            is_first_publish = mIs_first_publish;
        }

        public void setHavehigh(String mHavehigh) {
            havehigh = mHavehigh;
        }

        public void setCharge(String mCharge) {
            charge = mCharge;
        }

        public void setHas_mv(String mHas_mv) {
            has_mv = mHas_mv;
        }

        public void setLearn(String mLearn) {
            learn = mLearn;
        }

        public void setSong_source(String mSong_source) {
            song_source = mSong_source;
        }

        public void setPiao_id(String mPiao_id) {
            piao_id = mPiao_id;
        }

        public void setKorean_bb_song(String mKorean_bb_song) {
            korean_bb_song = mKorean_bb_song;
        }

        public void setResource_type_ext(String mResource_type_ext) {
            resource_type_ext = mResource_type_ext;
        }

        public void setMv_provider(String mMv_provider) {
            mv_provider = mMv_provider;
        }

        public void setArtist_name(String mArtist_name) {
            artist_name = mArtist_name;
        }

        public String getArtist_id() {
            return artist_id;
        }

        public String getLanguage() {
            return language;
        }

        public String getPic_big() {
            return pic_big;
        }

        public String getPic_small() {
            return pic_small;
        }

        public String getCountry() {
            return country;
        }

        public String getArea() {
            return area;
        }

        public String getPublishtime() {
            return publishtime;
        }

        public String getAlbum_no() {
            return album_no;
        }

        public String getLrclink() {
            return lrclink;
        }

        public String getCopy_type() {
            return copy_type;
        }

        public String getHot() {
            return hot;
        }

        public String getAll_artist_ting_uid() {
            return all_artist_ting_uid;
        }

        public String getResource_type() {
            return resource_type;
        }

        public String getIs_new() {
            return is_new;
        }

        public String getRank_change() {
            return rank_change;
        }

        public String getRank() {
            return rank;
        }

        public String getAll_artist_id() {
            return all_artist_id;
        }

        public String getStyle() {
            return style;
        }

        public String getDel_status() {
            return del_status;
        }

        public String getRelate_status() {
            return relate_status;
        }

        public String getToneid() {
            return toneid;
        }

        public String getAll_rate() {
            return all_rate;
        }

        public String getSound_effect() {
            return sound_effect;
        }

        public String getFile_duration() {
            return file_duration;
        }

        public String getHas_mv_mobile() {
            return has_mv_mobile;
        }

        public String getVersions() {
            return versions;
        }

        public String getBitrate_fee() {
            return bitrate_fee;
        }

        public String getSong_id() {
            return song_id;
        }

        public String getTitle() {
            return title;
        }

        public String getTing_uid() {
            return ting_uid;
        }

        public String getAuthor() {
            return author;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public String getAlbum_title() {
            return album_title;
        }

        public String getIs_first_publish() {
            return is_first_publish;
        }

        public String getHavehigh() {
            return havehigh;
        }

        public String getCharge() {
            return charge;
        }

        public String getHas_mv() {
            return has_mv;
        }

        public String getLearn() {
            return learn;
        }

        public String getSong_source() {
            return song_source;
        }

        public String getPiao_id() {
            return piao_id;
        }

        public String getKorean_bb_song() {
            return korean_bb_song;
        }

        public String getResource_type_ext() {
            return resource_type_ext;
        }

        public String getMv_provider() {
            return mv_provider;
        }

        public String getArtist_name() {
            return artist_name;
        }
    }

    /**
     *
     "artist_id": "15",
     "language": "国语",
     "pic_big": "http://musicdata.baidu.com/data2/pic/ed06f769e043d98f87443036b2c5b909/267778306/267778306.jpg",
     "pic_small": "http://musicdata.baidu.com/data2/pic/8100fdceeaed71940819d0593185be7f/267778310/267778310.jpg",
     "country": "港台",
     "area": "1",
     "publishtime": "2016-07-22",
     "album_no": "1",
     "lrclink": "http://musicdata.baidu.com/data2/lrc/45b7a43e127b6e0fe034b07a2e7ec276/267900666/267900666.lrc",
     "copy_type": "1",
     "hot": "928687",
     "all_artist_ting_uid": "45561",
     "resource_type": "0",
     "is_new": "1",
     "rank_change": "0",
     "rank": "1",
     "all_artist_id": "15",
     "style": "流行",
     "del_status": "0",
     "relate_status": "0",
     "toneid": "0",
     "all_rate": "64,128,256,320,flac",
     "sound_effect": "0",
     "file_duration": 0,
     "has_mv_mobile": 0,
     "versions": "",
     "bitrate_fee": "{\"0\":\"0|0\",\"1\":\"0|0\"}",
     "song_id": "267778516",
     "title": "尘埃",
     "ting_uid": "45561",
     "author": "王菲",
     "album_id": "267778552",
     "album_title": "尘埃",
     "is_first_publish": 0,
     "havehigh": 2,
     "charge": 0,
     "has_mv": 0,
     "learn": 0,
     "song_source": "web",
     "piao_id": "0",
     "korean_bb_song": "0",
     "resource_type_ext": "0",
     "mv_provider": "0000000000",
     "artist_name": "王菲"

     */

}
