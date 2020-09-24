package com.anikrakib.tourday.Models;

public class PostItem {

        private String mImageUrl;
        private String mPost;
        private String mLocation;
        private String mDate;
        private int mLikes;
        private boolean selfLike;


        public PostItem(String mImageUrl, String mPost, String mLocation, String mDate, int mLikes,boolean selfLike) {
            this.mImageUrl = mImageUrl;
            this.mPost = mPost;
            this.mLocation = mLocation;
            this.mDate = mDate;
            this.mLikes = mLikes;
            this.selfLike = selfLike;
        }

        public String getImageUrl() {
            return mImageUrl;
        }
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

    }


