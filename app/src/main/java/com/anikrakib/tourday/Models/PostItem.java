package com.anikrakib.tourday.Models;

public class PostItem {

        private String mImageUrl;
        private String mPost;
        private String mLocation;
        private String mDate;
        private int mLikes;
        private String mId;
        private boolean selfLike;


        public PostItem(String mImageUrl, String mPost, String mLocation, String mDate, int mLikes, String mId, boolean selfLike) {
            this.mImageUrl = mImageUrl;
            this.mPost = mPost;
            this.mLocation = mLocation;
            this.mDate = mDate;
            this.mLikes = mLikes;
            this.mId = mId;
            this.selfLike = selfLike;
        }


        public String getImageUrl() {
            return mImageUrl;
        }
        public String getmId() { return mId; }
        public String getPost() {
            return mPost;
        }
        public String getDate() {
            return mDate;
        }
        public String getLocation() {
            return mLocation;
        }
        public int getLikeCount() {
            return mLikes;
        }
        public boolean getSelfLike() {
            return selfLike;
        }

        public void setSelfLike(boolean userSelfLike) {
            this.selfLike = userSelfLike;
        }
        public void setLikes(int mLikes) {
            this.mLikes = mLikes;
        }
}


