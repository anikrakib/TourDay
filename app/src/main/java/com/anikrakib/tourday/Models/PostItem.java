package com.anikrakib.tourday.Models;

public class PostItem {

    public String Title;
    public String Body;
    public String Date;
    public int likecount = 0;
    public boolean like ;
    public int commentCount = 0;
    public int imageUrl;


    public PostItem() {
        // Default constructor required
    }

    public PostItem(String title, String body, String date, int likecount, int commentCount, boolean like, int imageUrl) {
        Title = title;
        Body = body;
        Date = date;
        this.likecount = likecount;
        this.like = like;
        this.commentCount = commentCount;
        this.imageUrl = imageUrl;
    }

}

