package com.anikrakib.tourday.Models;

public class PostItem {

    public String Title;
    public String Body;
    public String Date;
    public int likecount = 0;
    public boolean like ;
    public int commentCount = 0;


    public PostItem() {
        // Default constructor required
    }

    public PostItem(String Title, String Body, String Date, int likecount , int commentCount ,boolean like ) {
        this.Title = Title;
        this.Body = Body;
        this.Date = Date;
        this.like = like;
        this.likecount = likecount;
        this.commentCount = commentCount;

    }

}

