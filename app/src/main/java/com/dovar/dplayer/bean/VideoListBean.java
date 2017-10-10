package com.dovar.dplayer.bean;

import java.util.List;

/**
 * Created by heweizong on 2017/10/10.
 */

public class VideoListBean {
    private String nextPageUrl;
    private long nextPublishTime;
    private String newestIssueType;
    private String dialog;
    private List<IssueListBean> issueList;

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String mNextPageUrl) {
        nextPageUrl = mNextPageUrl;
    }

    public long getNextPublishTime() {
        return nextPublishTime;
    }

    public void setNextPublishTime(long mNextPublishTime) {
        nextPublishTime = mNextPublishTime;
    }

    public String getNewestIssueType() {
        return newestIssueType;
    }

    public void setNewestIssueType(String mNewestIssueType) {
        newestIssueType = mNewestIssueType;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String mDialog) {
        dialog = mDialog;
    }

    public List<IssueListBean> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<IssueListBean> mIssueList) {
        issueList = mIssueList;
    }

    public static class IssueListBean {
        private long releaseTime;
        private String type;
        private long date;
        private long publishTime;
        private int count;
        private List<ItemListBean> itemList;

        public long getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(long mReleaseTime) {
            releaseTime = mReleaseTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String mType) {
            type = mType;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long mDate) {
            date = mDate;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long mPublishTime) {
            publishTime = mPublishTime;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int mCount) {
            count = mCount;
        }

        public List<ItemListBean> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemListBean> mItemList) {
            itemList = mItemList;
        }

        public static class ItemListBean {
            private String type;
            private DataBean data;

            public String getType() {
                return type;
            }

            public void setType(String mType) {
                type = mType;
            }

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean mData) {
                data = mData;
            }

            public static class DataBean {
                private String dataType;
                private int id;
                private String title;
                private String description;
                private String image;
                private String actionUrl;
                private String category;
                long duration;
                private String playUrl;
                private CoverBean cover;
                private AuthorBean author;
                long releaseTime;
                private ConsumptionBean consumption;


                public String getDataType() {
                    return dataType;
                }

                public void setDataType(String mDataType) {
                    dataType = mDataType;
                }

                public int getId() {
                    return id;
                }

                public void setId(int mId) {
                    id = mId;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String mTitle) {
                    title = mTitle;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String mDescription) {
                    description = mDescription;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String mImage) {
                    image = mImage;
                }

                public String getActionUrl() {
                    return actionUrl;
                }

                public void setActionUrl(String mActionUrl) {
                    actionUrl = mActionUrl;
                }

                public String getCategory() {
                    return category;
                }

                public void setCategory(String mCategory) {
                    category = mCategory;
                }

                public long getDuration() {
                    return duration;
                }

                public void setDuration(long mDuration) {
                    duration = mDuration;
                }

                public String getPlayUrl() {
                    return playUrl;
                }

                public void setPlayUrl(String mPlayUrl) {
                    playUrl = mPlayUrl;
                }

                public CoverBean getCover() {
                    return cover;
                }

                public void setCover(CoverBean mCover) {
                    cover = mCover;
                }

                public AuthorBean getAuthor() {
                    return author;
                }

                public void setAuthor(AuthorBean mAuthor) {
                    author = mAuthor;
                }

                public long getReleaseTime() {
                    return releaseTime;
                }

                public void setReleaseTime(long mReleaseTime) {
                    releaseTime = mReleaseTime;
                }

                public ConsumptionBean getConsumption() {
                    return consumption;
                }

                public void setConsumption(ConsumptionBean mConsumption) {
                    consumption = mConsumption;
                }

                public static class CoverBean {
                    private String feed;
                    private String detail;
                    private String blurred;
                    private String sharing;
                    private String homepage;

                    public String getFeed() {
                        return feed;
                    }

                    public void setFeed(String mFeed) {
                        feed = mFeed;
                    }

                    public String getDetail() {
                        return detail;
                    }

                    public void setDetail(String mDetail) {
                        detail = mDetail;
                    }

                    public String getBlurred() {
                        return blurred;
                    }

                    public void setBlurred(String mBlurred) {
                        blurred = mBlurred;
                    }

                    public String getSharing() {
                        return sharing;
                    }

                    public void setSharing(String mSharing) {
                        sharing = mSharing;
                    }

                    public String getHomepage() {
                        return homepage;
                    }

                    public void setHomepage(String mHomepage) {
                        homepage = mHomepage;
                    }
                }

                public static class AuthorBean {
                    private String icon;

                    public String getIcon() {
                        return icon;
                    }

                    public void setIcon(String mIcon) {
                        icon = mIcon;
                    }
                }

                public static class ConsumptionBean {
                    private int collectionCount;
                    private int shareCount;
                    private int replyCount;

                    public int getCollectionCount() {
                        return collectionCount;
                    }

                    public void setCollectionCount(int mCollectionCount) {
                        collectionCount = mCollectionCount;
                    }

                    public int getShareCount() {
                        return shareCount;
                    }

                    public void setShareCount(int mShareCount) {
                        shareCount = mShareCount;
                    }

                    public int getReplyCount() {
                        return replyCount;
                    }

                    public void setReplyCount(int mReplyCount) {
                        replyCount = mReplyCount;
                    }
                }
            }
        }
    }
}
