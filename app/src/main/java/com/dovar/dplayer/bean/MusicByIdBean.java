package com.dovar.dplayer.bean;

import java.util.List;

/**
 * Created by ${dovar} on 2016/7/31.
 */
public class MusicByIdBean {
    String errorCode;
    Data data;

    @Override
    public String toString() {
        return "MusicByIdBean{" +
                "errorCode='" + errorCode + '\'' +
                ", data=" + data +
                '}';
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String mErrorCode) {
        errorCode = mErrorCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data mData) {
        data = mData;
    }

    public class Data {
        String xcode;
        List<Songlist> songList;

        @Override
        public String toString() {
            return "Data{" +
                    "xcode='" + xcode + '\'' +
                    ", songList=" + songList +
                    '}';
        }

        public String getXcode() {
            return xcode;
        }

        public void setXcode(String mXcode) {
            xcode = mXcode;
        }

        public List<Songlist> getSongList() {
            return songList;
        }

        public void setSongList(List<Songlist> mSongList) {
            songList = mSongList;
        }

        public class Songlist {
            String queryId;
            String songId;
            String songName;
            String artistId;
            String artistName;
            String albumId;
            String albumName;
            String songPicSmall;
            String songPicBig;
            String songPicRadio;
            String lrcLink;
            String version;
            String copytype;
            String time;
            String linkCode;
            String songLink;
            String showLink;
            String format;
            String rate;
            String size;
            String relateStatus;
            String resourceType;
            String source;

            @Override
            public String toString() {
                return "Songlist{" +
                        "queryId='" + queryId + '\'' +
                        ", songId='" + songId + '\'' +
                        ", songName='" + songName + '\'' +
                        ", artistId='" + artistId + '\'' +
                        ", artistName='" + artistName + '\'' +
                        ", albumId='" + albumId + '\'' +
                        ", albumName='" + albumName + '\'' +
                        ", songPicSmall='" + songPicSmall + '\'' +
                        ", songPicBig='" + songPicBig + '\'' +
                        ", songPicRadio='" + songPicRadio + '\'' +
                        ", lrcLink='" + lrcLink + '\'' +
                        ", version='" + version + '\'' +
                        ", copytype='" + copytype + '\'' +
                        ", time='" + time + '\'' +
                        ", linkCode='" + linkCode + '\'' +
                        ", songLink='" + songLink + '\'' +
                        ", showLink='" + showLink + '\'' +
                        ", format='" + format + '\'' +
                        ", rate='" + rate + '\'' +
                        ", size='" + size + '\'' +
                        ", relateStatus='" + relateStatus + '\'' +
                        ", resourceType='" + resourceType + '\'' +
                        ", source='" + source + '\'' +
                        '}';
            }

            public String getQueryId() {
                return queryId;
            }

            public void setQueryId(String mQueryId) {
                queryId = mQueryId;
            }

            public String getSongId() {
                return songId;
            }

            public void setSongId(String mSongId) {
                songId = mSongId;
            }

            public String getSongName() {
                return songName;
            }

            public void setSongName(String mSongName) {
                songName = mSongName;
            }

            public String getArtistId() {
                return artistId;
            }

            public void setArtistId(String mArtistId) {
                artistId = mArtistId;
            }

            public String getArtistName() {
                return artistName;
            }

            public void setArtistName(String mArtistName) {
                artistName = mArtistName;
            }

            public String getAlbumId() {
                return albumId;
            }

            public void setAlbumId(String mAlbumId) {
                albumId = mAlbumId;
            }

            public String getAlbumName() {
                return albumName;
            }

            public void setAlbumName(String mAlbumName) {
                albumName = mAlbumName;
            }

            public String getSongPicSmall() {
                return songPicSmall;
            }

            public void setSongPicSmall(String mSongPicSmall) {
                songPicSmall = mSongPicSmall;
            }

            public String getSongPicBig() {
                return songPicBig;
            }

            public void setSongPicBig(String mSongPicBig) {
                songPicBig = mSongPicBig;
            }

            public String getSongPicRadio() {
                return songPicRadio;
            }

            public void setSongPicRadio(String mSongPicRadio) {
                songPicRadio = mSongPicRadio;
            }

            public String getLrcLink() {
                return lrcLink;
            }

            public void setLrcLink(String mLrcLink) {
                lrcLink = mLrcLink;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String mVersion) {
                version = mVersion;
            }

            public String getCopytype() {
                return copytype;
            }

            public void setCopytype(String mCopytype) {
                copytype = mCopytype;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String mTime) {
                time = mTime;
            }

            public String getLinkCode() {
                return linkCode;
            }

            public void setLinkCode(String mLinkCode) {
                linkCode = mLinkCode;
            }

            public String getSongLink() {
                return songLink;
            }

            public void setSongLink(String mSongLink) {
                songLink = mSongLink;
            }

            public String getShowLink() {
                return showLink;
            }

            public void setShowLink(String mShowLink) {
                showLink = mShowLink;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String mFormat) {
                format = mFormat;
            }

            public String getRate() {
                return rate;
            }

            public void setRate(String mRate) {
                rate = mRate;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String mSize) {
                size = mSize;
            }

            public String getRelateStatus() {
                return relateStatus;
            }

            public void setRelateStatus(String mRelateStatus) {
                relateStatus = mRelateStatus;
            }

            public String getResourceType() {
                return resourceType;
            }

            public void setResourceType(String mResourceType) {
                resourceType = mResourceType;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String mSource) {
                source = mSource;
            }
        }
    }
}
/**}
"errorCode":22000,
        "data":{
        "xcode":"aa4f489c778d0eb8e9546c5248e367e2",
        "songList":[
        {
        "format":"mp3",
        "rate":128,
        "size":4818699,
        "relateStatus":"0",
        "resourceType":"0",
        "source":"web"

        }
        "queryId":"267778516",
        "songId":267778516,
        "songName":"尘埃",
        "artistId":"45561",
        "artistName":"王菲",
        "albumId":267778552,
        "albumName":"尘埃",
        "version":"",
        "copyType":0,
        "time":301,
        "linkCode":22000,
        "songLink":"http://yinyueshiting.baidu.com/data2/music/d433fe42856300fb81d91ea8f95213a0/267901211/2677785161469818861128.mp3?xcode=aa4f489c778d0eb8cc3224dc97e4d7a9",
        "showLink":"http://yinyueshiting.baidu.com/data2/music/d433fe42856300fb81d91ea8f95213a0/267901211/2677785161469818861128.mp3?xcode=aa4f489c778d0eb8cc3224dc97e4d7a9",

            */